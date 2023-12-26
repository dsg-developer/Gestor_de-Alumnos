package controllers;

import controllers.singletons.Singleton_Controllers;
import controllers.singletons.SingletonDtos;
import controllers.singletons.Singleton_Interfaces;
import controllers.singletons.SingletonModels;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;
import main.Start;
import models.ClassIDs;
import models.DaoException;
import models.dtos.DtoAlumno;
import models.dtos.DtoIndicadorLogro;
import models.dtos.DtoParametrosDeRP;
import models.dtos.DtoParametroRP;
import models.dtos.DtosTemas;
import views.Carga;
import views.RegistroError;

/**
 * @author Work-Game
 */

public class Controller_Registro_Personal {
    private Singleton_Controllers controladores;
    private final SingletonDtos dtos;
    private final Singleton_Interfaces interfaces;
    private final SingletonModels modelos;
    
    private DefaultListModel lisModel;
    private DefaultTableModel boceto;
    private Thread addRPHilo; 
    public Thread putNotaIndecadores; 
    private List<ClassIDs>IDs;
    private List<Integer> lParametro;
    int index;
    private ListModel l = null;
    
    public Controller_Registro_Personal() {
        dtos = SingletonDtos.getInstance();
        interfaces = Singleton_Interfaces.getInstance();
        modelos = SingletonModels.getInstance();
        
        IDs = new ArrayList<>();
        
        eventoBTNAdd();
        eventoPrincipalTabla();
        setNotaIndicadores();
        agregarRP();
        Start.setConteo(Start.getConteo()+0.1f);
    }
    
    private void agregarRP(){
        addRPHilo = new Thread(()->{
            try {
                Start.setMaxConteo(l.getSize());
                modelos.fechas.search();
                dtos.tema.setID(IDs.get(index).getId1());
                dtos.indicador_Logro.setTema(dtos.tema);
                List<DtoAlumno> lista = modelos.alumno.read(dtos.alumno);
                for(int x=0; x<l.getSize(); x++){
                    dtos.prametroRP.setParametro(l.getElementAt(x).toString());
                    modelos.parametroRP.search(dtos.prametroRP);                        
                    if(lista != null){
                        float suma = Start.getMaxConteo()+lista.size();
                        Start.setMaxConteo(suma);

                        lista.forEach(a -> {
                            try {
                                dtos.alumno.setID(a.getID());
                                dtos.fillRegistroPersonal(0, dtos.alumno, dtos.indicador_Logro, dtos.fecha
                                );
                                modelos.addRegistro_Personal.search(dtos.registro_Personal);
                                dtos.fillParametros_de_RP(null, dtos.prametroRP, dtos.registro_Personal,
                                                            dtos.tema);
                                modelos.parametro_de_RP.search(dtos.parametros_de_RP);
                                Start.setConteo(Start.getConteo()+1.0f);
                            } catch (DaoException ex) {
                                interfaces.registro_error.registrarError(Level.SEVERE, 
                                                            "Nuevo error detectado al crear un rp\n");
                                interfaces.registro_error.registrarError(Level.SEVERE, 
                                                                    RegistroError.getStackTrace(ex));
                            }
                        });
                    }
                    Start.setConteo(Start.getConteo()+1.0f);
                }                
            } catch (DaoException ex) {
                    interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear un"
                                                        + " rp\n");
                    interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
            Start.setCargando(false);
            interfaces.addRegistro_Personal.resetComponents((byte)1);
        });
    }
    
    public void cargarParametro(){
        try {
            List<DtoParametroRP> parametro = modelos.parametroRP.read(dtos.prametroRP);
            if(parametro != null){
                List<String> parametros = new ArrayList<>();
                boolean entrada = false;
                for(DtoParametroRP prp : parametro){
                    entrada = false;                    
                    for(int x=0; x<parametros.size(); x++){
                        if(parametros.get(x).equals(prp.getParametro())){
                            entrada = true;
                        }
                    }
                    if(!entrada){
                        parametros.add(prp.getParametro());
                        interfaces.addRegistro_Personal.jComboBox1.addItem(prp.getParametro());
                    }
                }
            }
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cargar los "
                                                        + "parametros\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    public void cargarRP(){
        try {
            List<DtoParametrosDeRP> p = modelos.parametro_de_RP.read(dtos.parametros_de_RP);
            IDs.clear();
            lParametro = new ArrayList<>();
            lParametro.add(Integer.SIZE);   lParametro.add(Integer.SIZE);
            lParametro.add(Integer.SIZE);
            if(p != null){
                List<String> parametros = new ArrayList<>();
                boolean entrada = false;
                for(DtoParametrosDeRP prp : p){//filtrando los parametros
                    entrada = false;                    
                    for(int x=0; x<parametros.size(); x++){
                        if(parametros.get(x).equals(prp.getPrametroRP().getParametro())){
                            entrada = true;
                        }
                    }
                    if(!entrada){
                        parametros.add(prp.getPrametroRP().getParametro());
                        lParametro.add(prp.getPrametroRP().getID());
                    }
                }
                boceto = interfaces.modelos.RegistroPersonal();
                //parametros.sort();
                for(String rp : parametros)  boceto.addColumn(rp);//agregar columnas a la tabla
                
                List<Integer> idsA = new ArrayList<>();
                int nParametro = parametros.size();
                for(DtoParametrosDeRP prp : p){
                    entrada = false;
                    
                    for(int x=0; x<idsA.size(); x++){
                        if(idsA.get((x)).equals(prp.getRegistro_Personal().getAlumno().getID())){
                            entrada = true;
                        }
                    }
                    if(!entrada){
                        idsA.add(prp.getRegistro_Personal().getAlumno().getID());
                        IDs.add(new ClassIDs(prp.getRegistro_Personal().getID()));
                        byte[] calificaciones = ordenarCalificaciones(p, parametros, prp.getRegistro_Personal().getAlumno().getID());
                        llenarFinlas(prp, boceto, nParametro, calificaciones);
                    }                  
                }
                interfaces.indicador_Logro.dispose();
                interfaces.indicador_Logro.resetComponentes();
            }else{
                //interfaces.principal.Tabla_derecha.setVisible(false);
                JOptionPane.showMessageDialog(null, "No se encontraron Registros Personales asociados a este"
                                               + " indicador", "Infomación", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cargar los "
                                                        + "rp\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    public void cargarTema(){
        try {
            List<DtosTemas> temas = modelos.tema.read(dtos.tema);
            if(temas != null){
                IDs.clear();
                lisModel = new DefaultListModel();
                temas.forEach(t -> {
                    IDs.add(new ClassIDs(t.getID()));
                    lisModel.addElement(t.getTema());                    
                });
                interfaces.addRegistro_Personal.JListTemas.setModel(lisModel);
            }        
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cargar los "
                                                        + "temas\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    private void eventoBTNAdd(){
        interfaces.addRegistro_Personal.JBAddRegistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controladores = Singleton_Controllers.getInstance();
                l = interfaces.addRegistro_Personal.JListCalificadores.getModel();
                ListModel m = interfaces.addRegistro_Personal.JListTemas.getModel();
                index = interfaces.addRegistro_Personal.JListTemas.getSelectedIndex();
                if(m.getSize() == 0){
                    JOptionPane.showMessageDialog(null, "Para poder agregar un registro personal al almacen "
                                                   + "es necesario\nque enliste los parametros de dicho "
                                                   + "registro.", "No se ha completado la información",
                                                   JOptionPane.INFORMATION_MESSAGE);
                }else if(index == -1 ||
                         interfaces.addRegistro_Personal.JListTemas.isSelectionEmpty()){
                    JOptionPane.showMessageDialog(null, "Para poder agregar un registro personal al almacen "
                                                  + "es necesario\nque selecione un tema para dicho registro.",
                                                  "No se ha competado la información", 
                                                  JOptionPane.INFORMATION_MESSAGE);
                }else{
                    Start.setConteo(0.0f);
                    Start.setCargando(true);
                    interfaces.addRegistro_Personal.resetComponents((byte)1);
                    new Carga(addRPHilo).setVisible(true);
                }
                new Thread(controladores.addRegistro_Personal.putNotaIndecadores).start();
            }
        });
    }

    private void eventoPrincipalTabla(){
        interfaces.principal.Tabla_derecha.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me){ 
                controladores = Singleton_Controllers.getInstance();
                if(controladores.principal.getSeccion() == 4){
                    int row = interfaces.principal.Tabla_derecha.getSelectedRow();
                    int column = interfaces.principal.Tabla_derecha.getSelectedColumn();
                    dtos.prametroRP.setID(lParametro.get(column));
                    dtos.registro_Personal.setID(IDs.get(row).getId1());
                }
            }
        });
    }
    
    private void llenarFinlas(DtoParametrosDeRP p, DefaultTableModel boceto, int nParametro, byte[] calificaciones) {
        switch(nParametro){
            case 1: boceto.addRow(new Object[]{p.getRegistro_Personal().getAlumno().getNumero(), 
                    p.getRegistro_Personal().getAlumno().getApellido(), p.getRegistro_Personal().getAlumno().getNombre(), 
                    calificaciones[0]});  break;
            
            case 2: boceto.addRow(new Object[]{p.getRegistro_Personal().getAlumno().getNumero(), 
                    p.getRegistro_Personal().getAlumno().getApellido(), p.getRegistro_Personal().getAlumno().getNombre(), 
                    calificaciones[0], calificaciones[1]});  break;
            
            case 3: boceto.addRow(new Object[]{p.getRegistro_Personal().getAlumno().getNumero(), 
                    p.getRegistro_Personal().getAlumno().getApellido(), p.getRegistro_Personal().getAlumno().getNombre(), 
                    calificaciones[0], calificaciones[1], calificaciones[2]});  break;
            
            case 4: boceto.addRow(new Object[]{p.getRegistro_Personal().getAlumno().getNumero(), 
                    p.getRegistro_Personal().getAlumno().getApellido(), p.getRegistro_Personal().getAlumno().getNombre(), 
                    calificaciones[0], calificaciones[1], calificaciones[2], calificaciones[3]});  break;
            
            case 5: boceto.addRow(new Object[]{p.getRegistro_Personal().getAlumno().getNumero(), 
                    p.getRegistro_Personal().getAlumno().getApellido(), p.getRegistro_Personal().getAlumno().getNombre(), 
                    calificaciones[0], calificaciones[1], calificaciones[2], calificaciones[3], calificaciones[4]});  break;
            
            case 6: boceto.addRow(new Object[]{p.getRegistro_Personal().getAlumno().getNumero(), 
                    p.getRegistro_Personal().getAlumno().getApellido(), p.getRegistro_Personal().getAlumno().getNombre(), 
                    calificaciones[0], calificaciones[1], calificaciones[2], calificaciones[3], calificaciones[4],
                    calificaciones[5]});    break;
            
            case 7: boceto.addRow(new Object[]{p.getRegistro_Personal().getAlumno().getNumero(), 
                    p.getRegistro_Personal().getAlumno().getApellido(), p.getRegistro_Personal().getAlumno().getNombre(), 
                    calificaciones[0], calificaciones[1], calificaciones[2], calificaciones[3], calificaciones[4],
                    calificaciones[5], calificaciones[6]});    break;
        }
        interfaces.principal.Tabla_derecha.setModel(boceto);
        interfaces.principal.scroll_tabla_derecha.setVisible(true);
    }   

    private byte[] ordenarCalificaciones(List<DtoParametrosDeRP> p, List<String> parametros, int IDAlumno) {
        int x=0;
        byte[] respuesta = new byte[parametros.size()];
        List<Byte> señuelo = new ArrayList<>();
        for(DtoParametrosDeRP pr : p){    
            if(pr.getRegistro_Personal().getAlumno().getID() == IDAlumno){
                if(señuelo.size() == parametros.size()){
                   return respuesta;
                }else{
                    respuesta[x] = pr.getCalificaciones().getCalificacion();
                    x++;
                    señuelo.add(pr.getCalificaciones().getCalificacion());
                }
            }
        }
        return respuesta;
    }
    
    private void setNotaIndicadores(){
        putNotaIndecadores = new Thread(()->{
            try {
                List<DtoAlumno> alumno = modelos.alumno.read(dtos.alumno);
                if(alumno != null){
                    alumno.forEach(a->{
                        try {//sumar registros personales y sacar la nota de los periodos                           
                            dtos.alumno.setID(a.getID());
                            byte nota = modelos.parametro_de_RP.sumarRPs();
                            if(nota > 0){
                                dtos.calificaciones.setCalificacion(nota);
                                modelos.Calificaciones.search(dtos.calificaciones);
                                dtos.fillCalificacion_indicador(dtos.alumno, dtos.calificaciones, 
                                                                                dtos.indicador_Logro);
                                modelos.calificaciones_Indicador.update(dtos.calificaciones_indicador);
                                
                                //sacar promedio de los indicadores
                                byte p1 = modelos.Calificaciones.notaFinal((byte)1);
                                byte p2 = modelos.Calificaciones.notaFinal((byte)2);
                                byte p3 = modelos.Calificaciones.notaFinal((byte)3);
                                byte p4 = modelos.Calificaciones.notaFinal((byte)4);
                                byte promedio = Byte.parseByte(String.valueOf((p1+p2+p3+p4)/4));
                                if(promedio > 0){
                                    dtos.alumno_CAY.setPromedio(promedio);
                                    dtos.alumno_CAY.setEdicion("promedio");
                                    dtos.fillAlumnoCAY(a, dtos.curso, dtos.asignatura, dtos.yearEscolar);
                                    modelos.alumno_CAY.update(dtos.alumno_CAY);
                                }
                                controladores = Singleton_Controllers.getInstance();
                                if (interfaces.principal.Tabla_derecha.isVisible() && 
                                    controladores.principal.getSeccion() == 0) {
                                    controladores.principal.setCarga();
                                }
                            }
                            putNotaIndecadores.join();
                        } catch (DaoException | InterruptedException ex) {
                            interfaces.registro_error.registrarError(Level.SEVERE,
                                    "Nuevo error detectado al establecer la nota de los indicadores\n");
                            interfaces.registro_error.registrarError(Level.SEVERE, 
                                    RegistroError.getStackTrace(ex));
                        }
                    });
                }
            } catch (DaoException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al establecer "
                                                        + "la nota de los indicadores\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        });
    }
    
}
