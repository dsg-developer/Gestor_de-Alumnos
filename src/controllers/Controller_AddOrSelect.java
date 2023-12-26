package controllers;

import controllers.singletons.Singleton_Class;
import controllers.singletons.Singleton_Controllers;
import controllers.singletons.SingletonDtos;
import controllers.singletons.Singleton_Interfaces;
import controllers.singletons.SingletonModels;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import main.Start;
import models.ClassIDs;
import models.DaoException;
import models.GenericObject;
import java.util.logging.*;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import models.dtos.DtoAsignatura;
import models.dtos.DtoCurso;
import models.dtos.DtoYearEscolar;
import models.dtos.Dto_Unidad;
import views.RegistroError;

/**
 * @author Work
 */

public class Controller_AddOrSelect {
    private Singleton_Interfaces interfaces;
    private SingletonModels models;
    private Singleton_Class clases;
    private SingletonDtos s_dtos;
    
    private List<ClassIDs> IDs;
    private DefaultTableModel modelo;
    private int column;
    private int row;
    private JMenuItem eliminar;
    private JPopupMenu subMenu;

    public Controller_AddOrSelect() {
        interfaces = Singleton_Interfaces.getInstance();
        models = SingletonModels.getInstance();
        clases = Singleton_Class.getInstance();  
        s_dtos = SingletonDtos.getInstance();
        IDs = new ArrayList<>();
        eliminar = new JMenuItem("Eliminar");
        subMenu = new JPopupMenu("SubMenu");
        subMenu.add(eliminar);
        eventos();
        Start.setConteo(Start.getConteo()+0.1f);
    }
    
    private void agregarCAY(){
        try {
            clases.objecto.clearDatos();
            //llenar lista para el objeto generico..............................
            clases.objecto.getDatos().add(interfaces.add_select.JTFCurso.getText());
            clases.objecto.getDatos().add(interfaces.add_select.JTFAsignatura.getText());
            clases.objecto.getDatos().add(interfaces.add_select.JTFYear.getText());
            //agregar cay......................................................
            models.curso.search(clases.objecto);
            models.asignatura.search(clases.objecto);
            models.year_escolar.search(clases.objecto);
            models.cay.search(clases.objecto);
            interfaces.add_select.resetTextField();
            cargador();
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al agregar el "
                                                        + "CAY\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    private void agregarUnidad(){
        try {
            s_dtos.unidad.setUnidad(interfaces.add_select.JTFCurso.getText());
            models.unidad.search(s_dtos.unidad);
            cargador();
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al agregar una "
                                                        + "unidad\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    public void cargador(){
        switch(Start.identificador){
            case 0: cargarCAY();    break;
            
            case 2: cargarUnidades(); break;
            
            case 4: cargarAsignaturas(); break;
            
            case 5: cargarCurso(); break;
            
            case 7: cargarUnidades(); break;
            
            case 11: cargarCAY(); break;
            
            case 12: cargarCAY(); break;
            
            default:    JOptionPane.showMessageDialog(null, "Este numero de identifecador no ha sido registrado en el cargador",
                                                            "información", JOptionPane.INFORMATION_MESSAGE);    break;
        }
    }
    
    private void cargarAsignaturas(){
        clases.objecto.clearDatos();
        List<DtoAsignatura> asignaturas = models.asignatura.read1();
        if(asignaturas != null){
            modelo = interfaces.modelos.asignatura();
            IDs.clear();
            asignaturas.forEach(a->{
                IDs.add(new ClassIDs(a.getID()));
                modelo.addRow(new Object[]{a.getAsignatura()});
            });
            interfaces.add_select.jTable1.setModel(modelo);
        }
    }
    
    private void cargarCAY(){
        try {//el orden de los datos se encuentran en la clase daocay
            List<Object> listado = models.cay.read2();
            if (listado != null) {
                Start.maxConteo += listado.size();
                IDs.clear();
                modelo = interfaces.modelos.addOrSelect();
                for(int x=0; x<listado.size(); x+=6){
                    IDs.add(new ClassIDs(Integer.parseInt(String.valueOf(listado.get(x))), 
                                         Integer.parseInt(String.valueOf(listado.get((x+2)))),
                                         Integer.parseInt(String.valueOf(listado.get((x+4))))));
                    modelo.addRow(new Object[]{listado.get((x+1)), listado.get((x+3)), listado.get((x+5))});
                }
                Start.setConteo(Start.getConteo()+0.1f);
                interfaces.add_select.jTable1.setModel(modelo);
                interfaces.add_select.JLAviso.setVisible(false);
                interfaces.add_select.scrollTable.setVisible(true);
            }else{
                interfaces.add_select.scrollTable.setVisible(false);
                interfaces.add_select.JLAviso.setVisible(true);
            }
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cargar el CAY\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    private void cargarCurso(){
        clases.objecto.clearDatos();
        List<DtoCurso> cursos = models.curso.read1();
        if(cursos != null){
            modelo = interfaces.modelos.curso();
            IDs.clear();
            cursos.forEach(c->{
                IDs.add(new ClassIDs(c.getID()));
                modelo.addRow(new Object[]{c.getCurso()});
            });
            interfaces.add_select.jTable1.setModel(modelo);
        }
    }
    
    private void cargarUnidades(){
        try {
            List<Dto_Unidad> listado = models.unidad.read(s_dtos.unidad);
            if (listado != null) {
                modelo = interfaces.modelos.unidades();
                IDs.clear();
                for(Dto_Unidad u : listado){
                    IDs.add(new ClassIDs(u.getID()));
                    modelo.addRow(new Object[]{u.getUnidad()});
                }
                interfaces.add_select.jTable1.setModel(modelo);
                interfaces.add_select.JLAviso.setVisible(false);
                interfaces.add_select.scrollTable.setVisible(true);
            }else{
                interfaces.add_select.scrollTable.setVisible(false);
                interfaces.add_select.JLAviso.setVisible(true);
                
            }
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear las "
                                                        + "unidades\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    private void click(){
        switch(Start.identificador){
            case 0: String[] cay = {IDs.get(row).getId1().toString(), interfaces.add_select.jTable1.getValueAt(row,
                                                                    0).toString(),
                                            IDs.get(row).getId2().toString(), interfaces.add_select.jTable1.getValueAt(row,
                                                    1).toString(),
                                            IDs.get(row).getId3().toString(), interfaces.add_select.jTable1.getValueAt(row,
                                                    2).toString()};
                    interfaces.principal.setCay(cay);
                    interfaces.principal.initLabels();
                    s_dtos.fillCurso(Integer.parseInt(IDs.get(row).getId1().toString()), interfaces.add_select.jTable1.getValueAt(row,
                                                                        0).toString());
                    s_dtos.fillAsignatura(Integer.parseInt(IDs.get(row).getId2().toString()), interfaces.add_select.jTable1.getValueAt(row,
                                                                                               1).toString());
                    s_dtos.fillYearEscolar(Integer.parseInt(IDs.get(row).getId3().toString()), interfaces.add_select.jTable1.getValueAt(row,
                                                                                                2).toString());
                    interfaces.add_select.setVisible(false);    Start.identificador = -1;
                    interfaces.principal.setVisible(true);  break;
            
            case 2: s_dtos.unidad.setID(IDs.get(row).getId1());
                    interfaces.addIndicador_logro.JTFUnidad.setText(interfaces.add_select.jTable1.getValueAt(row, 0).toString());
                    interfaces.addIndicador_logro.JLUnidad.setVisible(false);
                    interfaces.add_select.enabledComponents(true);
                    interfaces.add_select.JTFCurso.setText("");
                    interfaces.add_select.JLCurso.setVisible(true);
                    interfaces.add_select.dispose();    break;
                    
            case 11: transferirAlumno();    break;
            
            case 12: copiarIndicadorLogro();    break;
                    
        }
    }
    
    private void copiarIndicadorLogro(){
        try {
            if(IDs.get(row).getId1() != s_dtos.curso.getID()){
                Singleton_Controllers controladores = Singleton_Controllers.getInstance();
                DtoCurso curso = new DtoCurso();
                DtoAsignatura asignatura = new DtoAsignatura();
                DtoYearEscolar yearEscolar = new DtoYearEscolar();
                curso.setID(IDs.get(row).getId1());
                asignatura.setID(IDs.get(row).getId2());
                yearEscolar.setID(IDs.get(row).getId3());
                IDs = controladores.indicadores.getIDSelected();
                s_dtos.competencia.setID(IDs.get(controladores.indicadores.getRow()).getId2());
                s_dtos.unidad.setID(IDs.get(controladores.indicadores.getRow()).getId3());
                s_dtos.tema.setID(IDs.get(controladores.indicadores.getRow()).getId4());
                s_dtos.indicador.setID(IDs.get(controladores.indicadores.getRow()).getId5());
                s_dtos.fillIndicadorLogro(0, asignatura,  s_dtos.competencia, curso, s_dtos.tema,
                                            s_dtos.unidad, s_dtos.indicador, yearEscolar, 
                                            Controller_Indicadores.periodo);
                models.indicador_logro.create(s_dtos.indicador_Logro);
                interfaces.add_select.dispose();
                JOptionPane.showMessageDialog(null, "La copia del indicador ha sido exitosa!", "Infomación",
                                                JOptionPane.INFORMATION_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "No es posible transferir al alumno ya que ha "
                                              + "seleccionado\nel curso donde se encuentra actualmente, "
                                              + "si desea transferir\nal alumno, favor de seleccionar un "
                                              + "curso distinto", "Información", 
                                                JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al copiar un "
                                                        + "indicador de logro para otro curso\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    public void editarAsignatura(){
        String value = interfaces.add_select.jTable1.getValueAt(row, column).toString();
        clases.objecto.clearDatos();
        clases.objecto.getDatos().add(IDs.get(row).getId1());
        clases.objecto.getDatos().add(value);        
        try {
            models.asignatura.update(clases.objecto);
            interfaces.principal.JLAsignatura.setText(value);
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al editar una "
                                                        + "asignatura\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    public void editarCurso(){
        String value = interfaces.add_select.jTable1.getValueAt(row, column).toString();
        clases.objecto.clearDatos();
        clases.objecto.getDatos().add(IDs.get(row).getId1());
        clases.objecto.getDatos().add(value);        
        try {
            models.curso.update(clases.objecto);
            interfaces.principal.JLCurso.setText(value);
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al editar un "
                                                        + "curso\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    public void editarUnidad(){
        try {
            String value = interfaces.add_select.jTable1.getValueAt(row, 0).toString();
            s_dtos.fillUnidad(IDs.get(row).getId1(), value);
            models.unidad.update(s_dtos.unidad);
            //interfaces.add_select.jTable1.setValueAt(value, row, 0);            
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al editar una "
                                                        + "unidad\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    private void eventos(){
        eventoBTN();
        eventosMI();
        eventoTabla();
    }
    
    private void eventoBTN(){
        interfaces.add_select.JBAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(validar()){
                    switch(Start.identificador){
                        case 0: agregarCAY(); break;
                        
                        case 2: agregarUnidad();    break;
                    }
                }
                else    JOptionPane.showMessageDialog(null, "Es necesario que complete todos los campos, "
                                                          + "y que lo haga correctamente.");
            }
        });
    }
    
    private void eventosMI(){
        eliminar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                
            }
        });        
    }
    
    private void eventoTabla(){
        interfaces.add_select.jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                column = interfaces.add_select.jTable1.getSelectedColumn();
                row = interfaces.add_select.jTable1.getSelectedRow();
                click();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                row = interfaces.add_select.jTable1.rowAtPoint(e.getPoint());
                if(e.isPopupTrigger()){
                    interfaces.add_select.jTable1.changeSelection(row, column, false, false);
                    subMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
    
    private void transferirAlumno(){
        try {
            if(IDs.get(row).getId1() != s_dtos.curso.getID()){
                Singleton_Controllers controladores = Singleton_Controllers.getInstance();
                DtoCurso curso = new DtoCurso();
                DtoAsignatura asignatura = new DtoAsignatura();
                DtoYearEscolar yearEscolar = new DtoYearEscolar();
                curso.setID(IDs.get(row).getId1());
                asignatura.setID(IDs.get(row).getId2());
                yearEscolar.setID(IDs.get(row).getId3());
                s_dtos.alumno.setID(controladores.principal.getIDSelected());
                s_dtos.fillAlumnoCAY(s_dtos.alumno, curso, asignatura, yearEscolar);
                models.alumno_CAY.search(s_dtos.alumno_CAY);
                controladores.principal.setCarga();
                interfaces.add_select.dispose();
            }else{
                JOptionPane.showMessageDialog(null, "No es posible transferir al alumno ya que ha "
                                              + "seleccionado\nel curso donde se encuentra actualmente, "
                                              + "si desea transferir\nal alumno, favor de seleccionar un "
                                              + "curso distinto", "Información", 
                                                JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al transfeir un "
                                                        + "alumno\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
            
    private boolean validar(){
        boolean respuesta = false;
        if(Start.identificador == 0){
            if(!interfaces.add_select.JTFCurso.getText().isEmpty() && 
               !interfaces.add_select.JTFAsignatura.getText().isEmpty()
                && interfaces.add_select.getValidacion()){
                respuesta = true;
            }else respuesta = false;
        }else{
            if(!interfaces.add_select.JTFCurso.getText().isEmpty()) respuesta = true;
            else    respuesta = false;
        }
        return respuesta;
    }
    
}
