package controllers;

import controllers.singletons.Singleton_Controllers;
import controllers.singletons.SingletonDtos;
import controllers.singletons.Singleton_Interfaces;
import controllers.singletons.SingletonModels;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import main.Start;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import models.ClassIDs;
import models.DaoException;
import models.dtos.DtoIndicador;
import models.dtos.DtoAlumno;
import models.dtos.Dto_Competencia;
import models.dtos.DtoIndicadorLogro;
import models.dtos.Dto_calificaciones_indicador;
import views.Carga;
import views.RegistroError;

/**
 * @author Work-Game
 */

public class Controller_Indicadores {
    private Singleton_Controllers controladores;
    private Singleton_Interfaces interfaces;
    private SingletonDtos dtos;
    private SingletonModels modelos;
    
    private List<ClassIDs> IDs;
    private DefaultTableModel modelo;
    private Thread addCalificaciones;
    private Thread hiloAddAlumnos;
    private JPopupMenu subMenu;
    private JMenuItem CopiarA;
    private JMenuItem addAlumnos;
    private JMenuItem addIndicadorLogro;
    private int row;
    public boolean i = false;
    
    public static byte periodo;

    public Controller_Indicadores() {
        IDs = new ArrayList<>();
        dtos = SingletonDtos.getInstance();
        interfaces = Singleton_Interfaces.getInstance();
        modelos = SingletonModels.getInstance();
        subMenu = new JPopupMenu("Sub-menu");
        CopiarA = new JMenuItem("Copiar A...");
        addAlumnos = new JMenuItem("Agregar Alumnos");
        addIndicadorLogro = new JMenuItem("Agregar Indicador de Logro");
        eventos();
        Start.setConteo(Start.getConteo()+0.1f);
    }
    
    private void actualizarCompetencia(){
        try {
            String value = interfaces.indicador_Logro.JTAVista.getText();
            dtos.fillCompetencia(IDs.get(row).getId1(), value);
            modelos.competencia.update(dtos.competencia);
            interfaces.indicador_Logro.JTAVista.setText("");
            interfaces.indicador_Logro.jTable1.setValueAt(value, row, 0);
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al actualizar una "
                                                        + "competencia\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    private void actualizarIndicador(){
        try {
            dtos.fillIndicador(IDs.get(row).getId1(), interfaces.indicador_Logro.JTAVista.getText());
            modelos.indicador.update(dtos.indicador);
            interfaces.indicador_Logro.JTAVista.setText("");
            interfaces.indicador_Logro.jTable1.setValueAt(dtos.indicador.getIndicador(), row, 0);
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al actualizar un "
                                                        + "indicador de logro\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    private void actualizarTema(){
        try {
            dtos.fillTema(IDs.get(row).getId1(), interfaces.indicador_Logro.JTAVista.getText());
            modelos.tema.update(dtos.tema);
            interfaces.indicador_Logro.JTAVista.setText("");
            interfaces.indicador_Logro.jTable1.setValueAt(dtos.tema.getTema(), row, 0);
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al actualizar un "
                                                        + "tema\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    private void agregarIndicador(){
            dtos.tema.setTema(interfaces.addIndicador_logro.JTFTema.getText());
            Start.setConteo(0.0f);
            Start.setCargando(true);
            new Carga(addCalificaciones).setVisible(true);
    }
    
    public void cargador(byte edicion){
        switch(Start.identificador){
            case 3: cargarCompetencias();
                    break;
            
            case -3: cargarCompetencias();
                    break;
            
            case 9: cargarIndicadores();
                    break;
                        
            default:    cargarPeriodo(edicion);
        }
    }
    
    private void cargarCalificaciones(){
        try {
            List<Dto_calificaciones_indicador> ic = modelos.calificaciones_Indicador.read(
                                                                               dtos.calificaciones_indicador);
            if(ic != null){
                modelo = interfaces.modelos.calificacionIndicador();
                ic.forEach(ci->{
                    modelo.addRow(new Object[]{ci.getAlumno().getNumero(), ci.getAlumno().getApellido(), 
                                               ci.getAlumno().getNombre(), ci.getCalificacion().
                                               getCalificacion()});
                });
                interfaces.principal.Tabla_derecha.setModel(modelo);
                interfaces.principal.scroll_tabla_derecha.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(null, "No se puede el mostrar el registro porque no se han "
                                              + "encontrado\nalumnos relacionados a este indicador de logro",
                                              "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cargar las "
                                                        + "calificaciones\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    private void cargarCompetencias() {
        try {
            List<Dto_Competencia> lista = modelos.competencia.read(dtos.competencia);
            if(lista != null){
                modelo = interfaces.modelos.competencia();
                IDs.clear();
                for(Dto_Competencia c : lista){
                    IDs.add(new ClassIDs(c.getID()));
                    modelo.addRow(new Object[]{c.getCompetencia()});
                }
                interfaces.indicador_Logro.jTable1.setModel(modelo);
                interfaces.indicador_Logro.scroll_tabla.setVisible(true);
            }else   interfaces.indicador_Logro.scroll_tabla.setVisible(false);
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cargar las "
                                                        + "competencias\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    private void cargarIndicadores() {
        try {
            List<DtoIndicador> lista = modelos.indicador.read(dtos.indicador);
            if(lista != null){
                modelo = interfaces.modelos.indicador();
                IDs.clear();
                lista.forEach(i -> {
                    IDs.add(new ClassIDs(i.getID()));
                    modelo.addRow(new Object[]{i.getIndicador()});
                });                
                interfaces.indicador_Logro.jTable1.setModel(modelo);
                interfaces.indicador_Logro.scroll_tabla.setVisible(true);
            }else   interfaces.indicador_Logro.scroll_tabla.setVisible(false);
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cargar los "
                                                        + "indicadores de logro\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
        
    private void cargarPeriodo(byte edicion) {
        try {
            List<DtoIndicadorLogro> listado = modelos.indicador_logro.read(dtos.indicador_Logro);
            if(listado != null){
                IDs.clear();
                controladores = Singleton_Controllers.getInstance();
                if(controladores.principal.getSeccion() == 2 && Start.identificador != 6 || 
                    Start.identificador == 8){
                    modelo = interfaces.modelos.indicador_logro(edicion);
                    listado.forEach(i -> {
                        IDs.add(new ClassIDs(i.getID(), i.getCompetencia().getID(),
                                             i.getUnidad().getID(), i.getTema().getID(), 
                                             i.getIndicador().getID() ));

                        if(edicion == 0){
                            modelo.addRow(new Object[]{i.getUnidad().getUnidad(), 
                                                       i.getCompetencia().getCompetencia(),
                                                       i.getIndicador().getIndicador()});
                        }
                        else{
                            modelo.addRow(new Object[]{i.getUnidad().getUnidad(), 
                                                       i.getCompetencia().getCompetencia(), 
                                                       i.getIndicador().getIndicador(),
                                                       i.getPeriodo()});
                        }
                    });
                }else if (controladores.principal.getSeccion() == 4){
                    modelo = interfaces.modelos.IndicadorRP();
                    listado.forEach(i -> {
                        IDs.add(new ClassIDs(i.getID(), i.getIndicador().getID(), i.getTema().getID()));

                        modelo.addRow(new Object[]{i.getIndicador().getIndicador(), i.getTema().getTema()});
                        
                    });
                }else if (i || Start.identificador == 10){
                    modelo = interfaces.modelos.indicador();
                    listado.forEach(i -> {
                        IDs.add(new ClassIDs(i.getIndicador().getID()));
                        modelo.addRow(new Object[]{i.getIndicador().getIndicador()}); 
                    });
                }else{
                    modelo = interfaces.modelos.temasRP();
                    listado.forEach(i -> {
                        IDs.add(new ClassIDs(i.getTema().getID()));
                        modelo.addRow(new Object[]{i.getTema().getTema()}); 
                    });
                }interfaces.indicador_Logro.jTable1.setModel(modelo);
                    interfaces.indicador_Logro.scroll_tabla.setVisible(true);
            }else   interfaces.indicador_Logro.scroll_tabla.setVisible(false);
        } catch (NumberFormatException | DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al ejecutaar el "
                                                        + "metodo cargarPeriodo\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
        
    private void eventos() {
        eventoAddAlumnos();
        eventoAddCalificaciones();
        eventoBTN();
        eventoJComboBox();
        eventosMI();
        eventoTabla();
    }
     
    private void eventoAddAlumnos(){
        hiloAddAlumnos = new Thread(() -> {
            hiloAddAlumnos.setName("agregar alumnos a indicador");
            try {
                List<DtoAlumno> alumnos = modelos.alumno.read(dtos.alumno);
                if(alumnos != null){
                    Start.setMaxConteo(alumnos.size());
                    dtos.indicador_Logro.setID(IDs.get(row).getId1());
                    alumnos.forEach(a -> {
                        try {
                            dtos.alumno.setID(a.getID());
                            dtos.fillCalificacion_indicador(dtos.alumno,dtos.calificaciones, 
                                                            dtos.indicador_Logro);
                            modelos.calificaciones_Indicador.search(dtos.calificaciones_indicador);
                            Start.setConteo(Start.getConteo()+1.0f);
                        } catch (DaoException ex) {
                            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al"
                                                                     + "agregar un nuevo alumno\n");
                            interfaces.registro_error.registrarError(Level.SEVERE, 
                                                                        RegistroError.getStackTrace(ex));
                        }
                    });
                    Start.setCargando(false);
                }
            } catch (DaoException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al agregar un "
                                                                                + "nuevo alumno\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        });
    }
    
    private void eventoAddCalificaciones(){
        addCalificaciones = new Thread(() -> {
            addCalificaciones.setName("agregar calificaciones al indicador");
            try {
                List<DtoAlumno> alumnos = modelos.alumno.read(dtos.alumno);
                if(alumnos != null){
                    modelos.competencia.search(dtos.competencia);
                    modelos.indicador.search(dtos.indicador);
                    modelos.tema.search(dtos.tema);
                    dtos.fillIndicadorLogro(0, dtos.asignatura, dtos.competencia, dtos.curso, dtos.tema,
                                               dtos.unidad, dtos.indicador, dtos.yearEscolar, periodo);
                    modelos.indicador_logro.create(dtos.indicador_Logro);
                    interfaces.addIndicador_logro.resetComponents();
                    Start.setMaxConteo(alumnos.size());
                    alumnos.forEach(a -> {
                        try {
                            dtos.alumno.setID(a.getID());
                            dtos.fillCalificacion_indicador(dtos.alumno,dtos.calificaciones, 
                                                            dtos.indicador_Logro);
                            modelos.calificaciones_Indicador.create(dtos.calificaciones_indicador);
                            Start.setConteo(Start.getConteo()+1.0f);
                        } catch (DaoException ex) {
                            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al "
                                                                        + "agregar las calificaciones\n");
                            interfaces.registro_error.registrarError(Level.SEVERE,
                                                                            RegistroError.getStackTrace(ex));
                        }
                    });
                    Start.setCargando(false);
                }else{
                    Start.setCargando(false);
                    JOptionPane.showMessageDialog(null, "No se puede agregar este indicador porque no se "
                                                  + "han\nencontrado alumnos registrados en el almacen", 
                                                    "Informe", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (DaoException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al agregar las "
                                                        + "calificaciones\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        });
    }
    
    private void eventoBTN(){
        interfaces.indicador_Logro.JBAdd.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(!interfaces.indicador_Logro.JTAVista.getText().isEmpty()){
                    try {
                        String competencia = interfaces.indicador_Logro.JTAVista.getText();
                        dtos.competencia.setCompetencia(competencia);
                        modelos.competencia.search(dtos.competencia);
                        interfaces.indicador_Logro.JTAVista.setText("");
                        JOptionPane.showMessageDialog(null, "Agregado exitiso!");
                    } catch (DaoException ex) {
                        interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado en el "
                                                        + "evento de botones\n");
                        interfaces.registro_error.registrarError(Level.SEVERE,
                                                                    RegistroError.getStackTrace(ex));
                    }
                }
            }
        });
        
        interfaces.indicador_Logro.JBSelect.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                controladores = Singleton_Controllers.getInstance();
                if(controladores.principal.getSeccion() == 4 && Start.identificador != -3){
                    if(interfaces.indicador_Logro.JTAVista.getText().isEmpty()){
                        JOptionPane.showMessageDialog(null, "Es necesario que selccione un tema para poder "
                                                          + "continuar.", "No se Puede Continuar",
                                                             JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        dtos.indicador.setID(IDs.get(row).getId2());
                        dtos.tema.setID(IDs.get(row).getId3());
                        dtos.fillIndicadorLogro(IDs.get(row).getId1(), dtos.asignatura, 
                                                null, dtos.curso, dtos.tema, null, dtos.indicador, 
                                                dtos.yearEscolar, periodo);
                        controladores.addRegistro_Personal.cargarRP();
                    }
                }else if(controladores.principal.getSeccion() == 2){
                    if(interfaces.indicador_Logro.JTAVista.getText().isEmpty()){
                        JOptionPane.showMessageDialog(null, "Es necesario que selccione un indicador para "
                                                      + "poder continuar.", "No se Puede Continuar",
                                                         JOptionPane.INFORMATION_MESSAGE);
                    }else{
                        dtos.competencia.setID(IDs.get(row).getId2());
                        dtos.unidad.setID(IDs.get(row).getId3());
                        dtos.indicador.setIndicador(interfaces.indicador_Logro.jTable1.getValueAt(row, 
                                                    2).toString());
                        dtos.fillIndicadorLogro(IDs.get(row).getId1(), dtos.asignatura, dtos.competencia,
                                                dtos.curso, dtos.tema, dtos.unidad, dtos.indicador, 
                                                dtos.yearEscolar, periodo);
                        cargarCalificaciones();
                        interfaces.indicador_Logro.dispose();
                        interfaces.indicador_Logro.resetComponentes();
                    }
                }else{
                    if(interfaces.indicador_Logro.JTAVista.getText().isEmpty()){
                        if(Start.identificador == 6){
                            JOptionPane.showMessageDialog(null, "Es necesario que selccione un tema para "
                                                              + "poder continuar.", "No se Puede Continuar",
                                                                JOptionPane.INFORMATION_MESSAGE);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Es necesario que selccione una competencia "
                                                          + "para poder continuar.", "No se Puede Continuar",
                                                          JOptionPane.INFORMATION_MESSAGE);
                        }
                    }else if(Start.identificador == 3){
                        actualizarCompetencia();
                    }else if(Start.identificador == 6){
                        actualizarTema();
                    }else if(Start.identificador == 10){
                        controladores.addRegistro_Personal.cargarParametro();
                        controladores.addRegistro_Personal.cargarTema();
                        dtos.indicador.setID(IDs.get(row).getId1());
                        dtos.fillIndicadorLogro(0, null, null, null, null,
                                                null, dtos.indicador, null, periodo);
                        interfaces.addRegistro_Personal.setVisible(true);
                        interfaces.indicador_Logro.dispose();
                        interfaces.indicador_Logro.resetComponentes();
                    }else{
                        if(Start.identificador == 9){
                            if(i){
                                try {
                                    dtos.fillIndicador(IDs.get(row).getId1(), interfaces.
                                                        indicador_Logro.JTAVista.getText());
                                    modelos.indicador.update(dtos.indicador);
                                    interfaces.indicador_Logro.JTAVista.setText("");
                                    interfaces.indicador_Logro.jTable1.setValueAt(dtos.indicador.
                                                                                  getIndicador(), row, 0);
                                } catch (DaoException ex) {
                                    interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error "
                                                    + "detectado en el metodo de los eventos de botones\n");
                                    interfaces.registro_error.registrarError(Level.SEVERE,
                                                                            RegistroError.getStackTrace(ex));
                                }
                            }else{
                                String indicador = interfaces.indicador_Logro.JTAVista.getText();
                                interfaces.indicador_Logro.JTAVista.setText("");
                                if(IDs.size() > 0)  dtos.fillIndicador(IDs.get(row).getId1(), indicador);
                                interfaces.addIndicador_logro.JTAIndicador.setText(indicador);
                                interfaces.addIndicador_logro.JTAIndicador.setForeground(Color.black);
                                interfaces.indicador_Logro.dispose();
                                interfaces.indicador_Logro.resetComponentes();
                            }
                        }else{
                            String competencia = interfaces.indicador_Logro.JTAVista.getText();
                            interfaces.indicador_Logro.JTAVista.setText("");
                            if(IDs.size() > 0)  dtos.competencia.setID(IDs.get(row).getId1());
                            dtos.competencia.setCompetencia(competencia);
                            interfaces.addIndicador_logro.JTACompetencia.setText(competencia);
                            interfaces.addIndicador_logro.JTACompetencia.setForeground(Color.black);
                            interfaces.indicador_Logro.dispose();
                            interfaces.indicador_Logro.resetComponentes();
                        }
                    }
                }
            }
        });
    
        interfaces.addIndicador_logro.jButton1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(interfaces.addIndicador_logro.JLUnidad.isVisible()){
                    JOptionPane.showMessageDialog(null, "Es necesario que seleccione una unidad para poder "
                                                    + "continuar", "Informacion de Requisitos",
                                                    JOptionPane.INFORMATION_MESSAGE);
                }else if(interfaces.addIndicador_logro.JTACompetencia.getText().equals("Competencia...")){
                    JOptionPane.showMessageDialog(null, "Es necesario que seleccione una competencia para "
                                                      + "poder continuar","Informacion de Requisitos",
                                                        JOptionPane.INFORMATION_MESSAGE);
                }else if(interfaces.addIndicador_logro.JTAIndicador.getText().equals("Indicador de logro...")){
                    JOptionPane.showMessageDialog(null, "Es necesario que selecione un indicador para poder "
                                                  + "continuar","Informacion de Requisitos",
                                                  JOptionPane.INFORMATION_MESSAGE);
                }else if(interfaces.addIndicador_logro.JTFTema.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Es necesario que digite un tema para poder continuar"
                                                    , "Informacion de Requisitos",
                                                    JOptionPane.INFORMATION_MESSAGE);
                }
                else if(interfaces.addIndicador_logro.JCmbPeriodo.getSelectedItem() != null && 
                        interfaces.addIndicador_logro.JCmbPeriodo.getSelectedItem().
                        toString().equals("Periodos :")){
                    JOptionPane.showMessageDialog(null, "Es necesario que seleccione un periodo para poder "
                                                    + "continuar", "Informacion de Requisitos",
                                                    JOptionPane.INFORMATION_MESSAGE);
                }else agregarIndicador();
            }
        
        });
    
    }
    
    private void eventoJComboBox(){
        interfaces.indicador_Logro.JComboBPerriodos.addItemListener((e) -> {
            if (interfaces.indicador_Logro.JComboBPerriodos.getSelectedItem() != null
                    && !"Periodos".equals(interfaces.indicador_Logro.JComboBPerriodos.getSelectedItem().toString())) {
                switch(interfaces.indicador_Logro.JComboBPerriodos.getSelectedItem().toString()){
                    case "Periodo 1":    periodo = 1;   break;
                    case "Periodo 2":    periodo = 2;   break;
                    case "Periodo 3":    periodo = 3;   break;
                    case "Periodo 4":    periodo = 4;   break;
                }
                cargarPeriodo((byte)0);
            }
        });
        
        interfaces.addIndicador_logro.JCmbPeriodo.addItemListener((ev) -> {
            if (interfaces.addIndicador_logro.JCmbPeriodo.getSelectedItem() != null
                    && !"Periodos".equals(interfaces.addIndicador_logro.JCmbPeriodo.getSelectedItem().toString())) {
                switch(interfaces.addIndicador_logro.JCmbPeriodo.getSelectedItem().toString()){
                    case "Periodo 1":    periodo = 1;   break;
                    case "Periodo 2":    periodo = 2;   break;
                    case "Periodo 3":    periodo = 3;   break;
                    case "Periodo 4":    periodo = 4;   break;
                }
            }
        });
    }

    private void eventosMI(){
        CopiarA.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent ae){
                controladores = Singleton_Controllers.getInstance();
                Start.identificador = 12;
                interfaces.add_select.enabledComponents(false);
                interfaces.add_select.JTFCurso.setEditable(false);
                interfaces.add_select.JLCurso.setEnabled(false);
                interfaces.add_select.JLTitulo.setText("Seleccione el curso a transferir");
                interfaces.add_select.JLAgregar.setText("No Disponible");
                interfaces.add_select.JBAdd.setEnabled(false);
                controladores.add_select.cargador();
                interfaces.add_select.setVisible(true);
           }
        });
        
        addAlumnos.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent ae){
                Start.setConteo(0.0f);
                Start.setCargando(true);
                new Carga(hiloAddAlumnos).setVisible(true);
           }
        });
        
        addIndicadorLogro.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent ae){
                interfaces = Singleton_Interfaces.getInstance();
                interfaces.addIndicador_logro.JLIndicadorArea.setText(dtos.curso.getCurso()+" -> "+
                                                                        dtos.asignatura.getAsignatura());
                interfaces.addIndicador_logro.resetComponents();
                interfaces.addIndicador_logro.setVisible(true);
           }
        });
    }
    
    private void eventoTabla(){
        interfaces.indicador_Logro.jTable1.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                row = interfaces.indicador_Logro.jTable1.getSelectedRow();
                if(Start.identificador == 8){
                    Start.setConteo(0.0f);
                    Start.setCargando(true);
                    new Carga(hiloAddAlumnos).setVisible(true);
                }
            }
            
             @Override
            public void mouseReleased(MouseEvent me){
                if(me.isPopupTrigger()){
                    controladores = Singleton_Controllers.getInstance();
                    if(controladores.principal.getSeccion() == 2){
                        subMenu.add(addAlumnos);
                        subMenu.add(addIndicadorLogro);
                        subMenu.add(CopiarA);
                        //subMenu.add(nuevoAlumno);
                        row = interfaces.indicador_Logro.jTable1.rowAtPoint(me.getPoint());
                        interfaces.indicador_Logro.jTable1.changeSelection(row, 0, false, false);
                        subMenu.show(me.getComponent(), me.getX(), me.getY());
                    }
                }
            }
        });
    }
    
    public List<ClassIDs> getIDSelected(){
        return IDs;
    }
    
    public int getRow(){
        return row;
    }
}
