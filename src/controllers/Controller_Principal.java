package controllers;

import controllers.singletons.Singleton_Class;
import controllers.singletons.Singleton_Controllers;
import controllers.singletons.SingletonDtos;
import controllers.singletons.Singleton_Interfaces;
import controllers.singletons.SingletonModels;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import main.Start;
import models.ClassIDs;
import models.DaoException;
import models.GenericObject;
import models.dtos.DtoAlumnoCAY;
import models.dtos.DtoAlumno;
import models.dtos.Dto_Asistencia;
import models.dtos.DtoParametrosDeRP;
import views.Carga;
import views.CellEditor_table;
import static views.Principal.setPropiedades;
import views.RegistroError;
import views.Renderer_table;

/**
 * @author Work
 */
public class Controller_Principal {
    private Singleton_Class clases;
    private Singleton_Interfaces interfaces;
    private SingletonModels modelos;
    private SingletonDtos dtos;
    private Singleton_Controllers controladores;
    
    private DefaultTableModel boceto;
    private Thread hiloAddAsistencia;
    public Thread hiloUpdateAlumno;
    
    private List<ClassIDs> IDs;
    private int row=0, column=0, index=0;
    private int seccion;
    private byte unaVez=0;
    private JMenuItem Transferir;
    private JMenuItem nuevoAlumno;
    private JPopupMenu subMenu;
    private JFileChooser elegirFile;
    private String rutaSelecionada;

    public Controller_Principal() {
        clases = Singleton_Class.getInstance();
        interfaces = Singleton_Interfaces.getInstance();
        modelos = SingletonModels.getInstance();
        dtos = SingletonDtos.getInstance();
        IDs = new ArrayList<>();
        seccion = 0;
        Transferir = new JMenuItem("Transferir");
        nuevoAlumno = new JMenuItem("Nuevo Alumno");
        subMenu = new JPopupMenu("Sub-Menu");
        elegirFile = new JFileChooser();
        eventos();
        Start.setConteo(Start.getConteo()+0.1f);
    }
    
    private void cargarAlumnos(){
        try {
            interfaces.principal.JComboB.setSelectedIndex(0);
            interfaces.principal.JComboB.setEnabled(false);
            dtos.fillAlumnoCAY(null, dtos.curso, dtos.asignatura, dtos.yearEscolar);
            List<DtoAlumnoCAY> listado = modelos.alumno_CAY.read(dtos.alumno_CAY);
            if(listado != null){
                IDs.clear();
                boceto = interfaces.modelos.alumnos();
                listado.forEach(a -> {
                    IDs.add(new ClassIDs(a.getAlumno().getID()));
                    boceto.addRow(new Object[]{a.getAlumno().getNumero(), a.getAlumno().getApellido(), 
                                               a.getAlumno().getNombre(), (int) a.getPromedio(),
                                               (int) a.getPorcentaje_asistencia()+"%"});
                                    
                });
                interfaces.principal.Tabla_derecha.setModel(boceto);
                interfaces.principal.scroll_tabla_derecha.setVisible(true);
            }else{
                interfaces.principal.scroll_tabla_derecha.setVisible(false);
            }
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cargar los "
                                                        + "alumnos\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    private void cargarAsistencia(){
        try {
            List<Dto_Asistencia> listado = modelos.asistencias.read(dtos.asistencia);
            if(listado != null){
                boceto = interfaces.modelos.asistencia();
                IDs.clear();
                for(Dto_Asistencia a : listado){
                    IDs.add(new ClassIDs(a.getID(), a.getFecha().getID(), a.getAlumno().getID()));
                    boceto.addRow(new Object[]{a.getAlumno().getNumero(), a.getAlumno().getApellido(),
                                               a.getAlumno().getNombre(), a.getAsitencia()});
                }                    
                interfaces.principal.Tabla_derecha.setModel(boceto);
                interfaces.principal.Tabla_derecha.getColumnModel().getColumn(3).setCellRenderer(new Renderer_table());
                interfaces.principal.Tabla_derecha.getColumnModel().getColumn(3).setCellEditor(new CellEditor_table());
                interfaces.principal.scroll_tabla_derecha.setVisible(true);
            }else{
                interfaces.principal.scroll_tabla_derecha.setVisible(false);
            }
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cargar las "
                                                        + "asistencias\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    private void cargarFechas(boolean asistencia){
        interfaces.principal.resetCBB();
        List<Dto_Asistencia> fecha1 = null;
        List<DtoParametrosDeRP> fecha2 = null;
        IDs.clear();
        IDs.add(null);
        if(asistencia)  fecha1 = modelos.asistencias.readFechas();
        else    fecha2 = modelos.parametro_de_RP.readFechas();
        if(fecha1 != null){
            interfaces.principal.JComboB.setEnabled(true);
            boolean validar = false;
            String a, b;
            for(Dto_Asistencia f : fecha1){
                validar = false;
                a = f.getFecha().getFecha();
                IDs.add(new ClassIDs(f.getFecha().getID()));
                for(int x=0; x < interfaces.principal.JComboB.getItemCount(); x++){
                    b = interfaces.principal.JComboB.getItemAt(x);
                    if(b.equals(a))
                        validar = true;
                }
                if(!validar)    interfaces.principal.JComboB.addItem(f.getFecha().getFecha());
            }
        }else if(fecha2 != null){  
            interfaces.principal.JComboB.setEnabled(true);
            boolean validar = false;
            String a, b;
            for(DtoParametrosDeRP f : fecha2){
                validar = false;
                a = f.getRegistro_Personal().getFecha().getFecha();
                IDs.add(new ClassIDs(f.getRegistro_Personal().getFecha().getID()));
                for(int x=0; x < interfaces.principal.JComboB.getItemCount(); x++){
                    b = interfaces.principal.JComboB.getItemAt(x);
                    if(b.equals(a))
                        validar = true;
                }
                if(!validar)    interfaces.principal.JComboB.addItem(f.getRegistro_Personal().getFecha().getFecha());
            }
        }else   interfaces.principal.JComboB.setEnabled(false);
    }
    
    private void cargarNotas(){
        interfaces.principal.JComboB.setSelectedIndex(0);
        interfaces.principal.JComboB.setEnabled(false);
        List<DtoAlumno> listado = modelos.alumno.readNotas();
        if(listado != null){
            boceto = interfaces.modelos.notas();            
            listado.forEach(a->{
                dtos.alumno.setID(a.getID());
                byte p1 = modelos.Calificaciones.notaFinal((byte)1);
                byte p2 = modelos.Calificaciones.notaFinal((byte)2);
                byte p3 = modelos.Calificaciones.notaFinal((byte)3);
                byte p4 = modelos.Calificaciones.notaFinal((byte)4);
                boceto.addRow(new Object[]{a.getNumero(), a.getApellido(), a.getNombre(), 
                                           p1, p2, p3, p4});
            });
            interfaces.principal.Tabla_derecha.setModel(boceto);
            interfaces.principal.scroll_tabla_derecha.setVisible(true);
        }
    }
    
    private void determinarPocientoAsistencia(){
        hiloUpdateAlumno = new Thread(()->{
            try {
                List<DtoAlumno> alumnos = modelos.alumno.read(dtos.alumno);
                if(alumnos != null){
                    alumnos.forEach(a -> {
                        try {
                            double porciento = modelos.asistencias.sacarPociento(a.getID());
                            dtos.alumno_CAY.setEdicion("asistencia");
                            dtos.alumno_CAY.setPorcentaje_asistencia(porciento);
                            dtos.fillAlumnoCAY(a, dtos.curso, dtos.asignatura, dtos.yearEscolar);
                            modelos.alumno_CAY.update(dtos.alumno_CAY);
                            if (interfaces.principal.Tabla_derecha.isVisible() && seccion == 0 ) {
                                controladores = Singleton_Controllers.getInstance();
                                controladores.principal.cargarAlumnos();
                            }
                        } catch (DaoException ex) {
                            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al "
                                                            + "determinar el porciento de las asistencias\n");
                            interfaces.registro_error.registrarError(Level.SEVERE,
                                                                        RegistroError.getStackTrace(ex));
                        }
                    });
                }
                hiloUpdateAlumno.join();
            } catch (DaoException | InterruptedException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al determinar "
                                                        + "el porcentaje de las asistencias\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        });
    }
    
    private void editarAlumno(Object value){
        try {
            dtos.alumno.setID(IDs.get(row).getId1());
            switch(column){
                case 0: dtos.alumno.setEdicion("numero");    dtos.alumno.setNumero((byte)value);
                        break;
                
                case 1: dtos.alumno.setEdicion("apellido"); dtos.alumno.setApellido(value.toString());
                        break;
                
                case 2: dtos.alumno.setEdicion("nombre"); dtos.alumno.setNombre(value.toString());
                        break;
            }
            modelos.alumno.update(dtos.alumno);
        } catch (Exception ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al editar un "
                                                        + "alumno\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    private void editarAsistencia(Object value){
        try {
            row = interfaces.principal.Tabla_derecha.getSelectedRow();
            dtos.asistencia.setID(IDs.get(row).getId1());
            dtos.asistencia.setAsitencia(Byte.parseByte(String.valueOf(value)));
            dtos.asistencia.setFecha(dtos.fecha);            
            modelos.asistencias.update(dtos.asistencia);
        } catch (NumberFormatException | DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al editar una "
                                                        + "asistencia\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
    
    private void eventos() {
        determinarPocientoAsistencia();
        eventoCallBack();
        eventoHiloAddAsistencia();
        eventoJcmbb();
        eventoLeftTabla();
        eventoMIs();
        eventoRightTabla();
    }
    
    private void eventoCallBack(){
        interfaces.modelos.setCallBackListener(new CallBack() {
            @Override
            public void avisoListener(Object t) {
                switch(seccion){
                    case 0: editarAlumno(t); break;
                    case 1: editarAsistencia(t); break;
                    case 4: updateCalificacion(t); break;
                    default:JOptionPane.showMessageDialog(null, "Error en el evento del callback");
                }
            }
        });
    }

    private void eventoHiloAddAsistencia() {
        hiloAddAsistencia = new Thread(()->{
            try {
                List<DtoAlumno> listado = modelos.alumno.read(dtos.alumno);
                if(listado != null){
                    Start.setMaxConteo(listado.size());
                    listado.forEach(a -> {
                        try {
                            if(!modelos.asistencias.isExists){
                                dtos.fillAsistencia(0, dtos.fillAlumno(a.getID(), null, null,  (byte) 0),
                                                    dtos.fecha, (byte)0);
                                modelos.fechas.search();
                                modelos.asistencias.search(dtos.asistencia);
                                if (modelos.asistencias.isExists)   Start.setConteo(Start.getMaxConteo());
                                Start.setConteo(Start.getConteo()+1.0f);
                            }
                        } catch (DaoException ex) {
                            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado "
                                                                + "en el hilo de agregar la asistencia\n");
                            interfaces.registro_error.registrarError(Level.SEVERE,
                                                                        RegistroError.getStackTrace(ex));
                        }
                    
                    });
                    if (modelos.asistencias.isExists) {
                        JOptionPane.showMessageDialog(null, "Ya se encuentra en el almacen un listado del "
                                            + "dia de hoy", "información", JOptionPane.INFORMATION_MESSAGE);
                    }
                    if(seccion == 1 && interfaces.principal.scroll_tabla_derecha.isVisible()){
                        cargarAsistencia();
                    }
                    Start.setCargando(false);
                }else   Start.setCargando(false);
                hiloAddAsistencia.join();
            } catch (DaoException | InterruptedException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado en el "
                                                                    + "hilo de agregar la asistencia\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
            new Thread(controladores.principal.hiloUpdateAlumno).start();
        });
    }
        
    private void eventoJcmbb(){
        interfaces.principal.JComboB.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(interfaces.principal.JComboB.getSelectedItem() != null &&
                   !interfaces.principal.JComboB.getSelectedItem().toString().equals("Fecha:")){
                    if(seccion == 1){
                        interfaces.principal.setFecha(
                                                 interfaces.principal.JComboB.getSelectedItem().toString());
                        cargarAsistencia();                        
                    }else{
                        if(interfaces.principal.JComboB.getItemCount() > 1){
                            int index = interfaces.principal.JComboB.getSelectedIndex();
                            dtos.fecha.setID(IDs.get(index).getId1());
                            Start.identificador = -12;
                            interfaces.indicador_Logro.JLIndicador.setText("Seleccione el tema");
                            interfaces.indicador_Logro.JBAdd.setEnabled(false);
                            interfaces.indicador_Logro.JBSelect.setText("Seleccionar");
                            interfaces.indicador_Logro.JBSelect.setEnabled(true);
                            interfaces.indicador_Logro.JTAVista.setEditable(false);
                            interfaces.indicador_Logro.scroll_tabla.setVisible(false);
                            interfaces.indicador_Logro.setVisible(true);
                        }
                    }
                }
            }
        });
    }
    
    private void eventoLeftTabla() {
        interfaces.principal.Tabla_izquierda.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seccion = interfaces.principal.Tabla_izquierda.getSelectedRow();
                switch(seccion){
                    case 0: cargarAlumnos();    break;                    
                    
                    case 1: interfaces.principal.scroll_tabla_derecha.setVisible(false);
                            cargarFechas(true); break;
                    
                    case 2: Start.identificador = -1;
                            interfaces.indicador_Logro.JComboBPerriodos.setEnabled(true);
                            interfaces.principal.scroll_tabla_derecha.setVisible(false);
                            interfaces.principal.JComboB.setSelectedIndex(0);
                            interfaces.principal.JComboB.setEnabled(false);
                            interfaces.indicador_Logro.JLIndicador.setText("Seleccione su indicador");
                            interfaces.indicador_Logro.JBAdd.setEnabled(false);
                            interfaces.indicador_Logro.JBSelect.setText("Seleccionar");
                            interfaces.indicador_Logro.JBSelect.setEnabled(true);
                            interfaces.indicador_Logro.JTAVista.setEditable(false);
                            interfaces.indicador_Logro.setVisible(true);    break;
                    
                    case 3: cargarNotas(); break;
                    
                    case 4: interfaces.principal.scroll_tabla_derecha.setVisible(false);
                            interfaces.indicador_Logro.JComboBPerriodos.setEnabled(true);
                            Start.identificador = -7;
                            cargarFechas(false);                            
                            break;
                    
                    case 5: interfaces.principal.scroll_tabla_derecha.setVisible(false);
                            interfaces.indicador_Logro.JComboBPerriodos.setEnabled(false);
                            interfaces.principal.JComboB.setEnabled(false);
                            JOptionPane.showMessageDialog(null, "Esta opción no está disponible por el "
                                                          + "momenrto", "Información", 
                                                          JOptionPane.INFORMATION_MESSAGE);
                            break;
                    
                    default:    JOptionPane.showMessageDialog(null, "Se ha insertado una clave de sección "
                                                        + "no registrada en la tabla de secciones.");break;
                }
            }
        });
    }
    
    private void eventoMIs(){
        interfaces.principal.JMIActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 clases.conector.actuaizar();
            }
        });
        
        interfaces.principal.JMIAddAlumnIndicador.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Start.identificador = 8;
                seccion = -2;
                interfaces.indicador_Logro.setLocationRelativeTo(interfaces.principal);
                interfaces.indicador_Logro.JLIndicador.setText("Seleecione el indicador");
                interfaces.indicador_Logro.JBSelect.setEnabled(false);
                interfaces.indicador_Logro.setVisible(true);
            }
        });
        
        interfaces.principal.JMIAddCompetencia.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                controladores = Singleton_Controllers.getInstance();
                interfaces.indicador_Logro.JComboBPerriodos.setEnabled(false);
                interfaces.indicador_Logro.JLIndicador.setText("Agregue su Competencia");
                interfaces.indicador_Logro.JBAdd.setEnabled(true);
                interfaces.indicador_Logro.JBSelect.setText("Seleccionar");
                interfaces.indicador_Logro.JBSelect.setEnabled(false);
                interfaces.indicador_Logro.JTAVista.setEditable(true);
                interfaces.indicador_Logro.setVisible(true);
            }
        });
        
        interfaces.principal.JMIAsistencia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controladores = Singleton_Controllers.getInstance();
                Start.setConteo(0.0f);
                Start.setCargando(true);
                Start.identificador = 1;
                new Carga(hiloAddAsistencia).setVisible(true);
                
            }
        });
    
        interfaces.principal.JMIAsignatura.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                controladores = Singleton_Controllers.getInstance();
                Start.identificador = 4;
                interfaces.add_select.enabledComponents(false);
                interfaces.add_select.JTFCurso.setEditable(false);
                interfaces.add_select.JLCurso.setEnabled(false);
                interfaces.add_select.JLTitulo.setText("Edite su Asignatura");
                interfaces.add_select.JLAgregar.setText("No Disponible");
                interfaces.add_select.JBAdd.setEnabled(false);
                controladores.add_select.cargador();
                interfaces.add_select.setVisible(true);
            }
        });
        
        interfaces.principal.JMICompetencia.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                controladores = Singleton_Controllers.getInstance();
                Start.identificador = 3;
                seccion = -seccion;
                interfaces.indicador_Logro.JComboBPerriodos.setEnabled(false);
                interfaces.indicador_Logro.JLIndicador.setText("Edite su Competencia");
                interfaces.indicador_Logro.JBAdd.setEnabled(false);
                interfaces.indicador_Logro.JBSelect.setText("Editar");
                interfaces.indicador_Logro.JBSelect.setEnabled(true);
                interfaces.indicador_Logro.JTAVista.setEditable(true);
                controladores.indicadores.cargador((byte)0);
                interfaces.indicador_Logro.setVisible(true);
            }
        });
        
        interfaces.principal.JMICurso.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                controladores = Singleton_Controllers.getInstance();
                Start.identificador = 5;
                interfaces.add_select.enabledComponents(false);
                interfaces.add_select.JTFCurso.setEditable(false);
                interfaces.add_select.JLCurso.setEnabled(false);
                interfaces.add_select.JLTitulo.setText("Edite su Curso");
                interfaces.add_select.JLAgregar.setText("No Disponible");
                interfaces.add_select.JBAdd.setEnabled(false);
                controladores.add_select.cargador();
                interfaces.add_select.setVisible(true);
            }
        });
        
        interfaces.principal.JMIExcelAlumnos.addActionListener(new ActionListener(){
             @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Para la correcta importación de datos es necesario que"
                                                  + " su\ndocumento tenga el nombre y apellido separado,"
                                                  + " tambien\nlos encabezados deben estar en singular.",
                                                    "Importante", JOptionPane.INFORMATION_MESSAGE);
                FileNameExtensionFilter filtro = new FileNameExtensionFilter(".xlsx", "xlsx");
                elegirFile.setFileFilter(filtro);
                int seleccion = elegirFile.showOpenDialog(interfaces.principal);
                
                if(seleccion == JFileChooser.APPROVE_OPTION){
                    rutaSelecionada = elegirFile.getSelectedFile().getPath();
                    modelos.importar.importarExcel(new File(rutaSelecionada));
                }
            }
        });
        
        interfaces.principal.JMIIndicador.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                controladores = Singleton_Controllers.getInstance();
                Start.identificador = 9;   
                controladores.principal.setSeccion(-1);
                controladores.indicadores.i = true;
                interfaces.indicador_Logro.JLIndicador.setText("Edite su Indicador.");
                interfaces.indicador_Logro.JBSelect.setText("Editar");
                interfaces.indicador_Logro.JBSelect.setEnabled(true);
                interfaces.indicador_Logro.JBAdd.setEnabled(false);
                interfaces.indicador_Logro.JTAVista.setEditable(true);
                controladores.indicadores.cargador((byte)0);
                interfaces.indicador_Logro.setVisible(true);
            }
        });
        
        interfaces.principal.JMITema.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                controladores = Singleton_Controllers.getInstance();
                Start.identificador = 6;
                controladores.principal.setSeccion(-1);
                controladores.indicadores.i = false;
                interfaces.indicador_Logro.JLIndicador.setText("Edite su tema.");
                interfaces.indicador_Logro.JBSelect.setText("Editar");
                interfaces.indicador_Logro.JBSelect.setEnabled(true);
                interfaces.indicador_Logro.JBAdd.setEnabled(false);
                interfaces.indicador_Logro.JTAVista.setEditable(true);
                controladores.indicadores.cargador((byte)0);
                interfaces.indicador_Logro.setVisible(true);
            }
        });
    
        interfaces.principal.JMIUnidad.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                Start.identificador = 7;
                controladores = Singleton_Controllers.getInstance();
                controladores.add_select.cargador();
                interfaces.add_select.JLTitulo.setText("Edite su Unidad");
                interfaces.add_select.JLAgregar.setText("No Disponible");
                interfaces.add_select.JLCurso.setText("Escribir Curso...");
                interfaces.add_select.enabledComponents(false);
                interfaces.add_select.JTFCurso.setEditable(false);
                interfaces.add_select.JLCurso.setEnabled(false);
                interfaces.add_select.JBAdd.setEnabled(false);
                interfaces.add_select.setVisible(true);
            }
                
        });
        
        Transferir.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                controladores = Singleton_Controllers.getInstance();
                Start.identificador = 11;
                interfaces.add_select.enabledComponents(false);
                interfaces.add_select.JTFCurso.setEditable(false);
                interfaces.add_select.JLCurso.setEnabled(false);
                interfaces.add_select.JLTitulo.setText("Seleccione el curso a transferir");
                interfaces.add_select.JLAgregar.setText("No Disponible");
                interfaces.add_select.JBAdd.setEnabled(false);
                controladores.add_select.cargador();
                interfaces.add_select.setVisible(true);
                /*JOptionPane.showMessageDialog(null, "Esta opción no esta disponible por el momento.",
                                              "información", JOptionPane.INFORMATION_MESSAGE);*/
            }
        });
        
        nuevoAlumno.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                interfaces = Singleton_Interfaces.getInstance();
                interfaces.add_alumno.setLocationRelativeTo(interfaces.principal);
                interfaces.add_alumno.JTFCurso.setText(dtos.curso.getCurso());
                interfaces.add_alumno.JTFAsignatura.setText(dtos.asignatura.getAsignatura());
                interfaces.add_alumno.JTFYearEscolar.setText(dtos.yearEscolar.getYear());
                interfaces.add_alumno.setVisible(true);
            }
        });
        
    }
    
    private void eventoRightTabla(){
        interfaces.principal.Tabla_derecha.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me){ 
                row = interfaces.principal.Tabla_derecha.getSelectedRow();
                column = interfaces.principal.Tabla_derecha.getSelectedColumn();
            }
            
            @Override
            public void mouseReleased(MouseEvent me){
                if(me.isPopupTrigger()){
                    if(seccion == 0){
                        subMenu.add(Transferir);
                        subMenu.add(nuevoAlumno);
                        row = interfaces.principal.Tabla_derecha.rowAtPoint(me.getPoint());
                        interfaces.principal.Tabla_derecha.changeSelection(row, column, false, false);
                        subMenu.show(me.getComponent(), me.getX(), me.getY());
                    }
                }
            }
        });
    }
    
    public int getIDSelected(){
        return IDs.get(row).getId1();
    }
    
    public String getPathChoose(){
        return rutaSelecionada;
    }
    
    public int getSeccion() {
        return seccion;
    }
    
    public void setCarga(){
        if(seccion == 0 && interfaces.principal.Tabla_derecha.isVisible()) cargarAlumnos();
    }
    
    public void setLineas(byte set){
        interfaces = Singleton_Interfaces.getInstance();
        if(interfaces.principal.JMICHBHorizontal.isSelected() || set == 0){
            interfaces.add_select.jTable1.setShowHorizontalLines(true);
            interfaces.indicador_Logro.jTable1.setShowHorizontalLines(true);
            interfaces.principal.Tabla_izquierda.setShowHorizontalLines(true);
            interfaces.principal.Tabla_derecha.setShowHorizontalLines(true);
            interfaces.principal.setPropiedades("LHorizontal", "true");
        }else{
            interfaces.add_select.jTable1.setShowHorizontalLines(false);
            interfaces.indicador_Logro.jTable1.setShowHorizontalLines(false);
            interfaces.principal.Tabla_izquierda.setShowHorizontalLines(false);
            interfaces.principal.Tabla_derecha.setShowHorizontalLines(false);
            interfaces.principal.setPropiedades("LHorizontal", "false");
        }
        
        if(interfaces.principal.JMICHBVertical.isSelected() || set == 1){
            interfaces.add_select.jTable1.setShowVerticalLines(true);
            interfaces.indicador_Logro.jTable1.setShowVerticalLines(true);
            interfaces.principal.Tabla_derecha.setShowVerticalLines(true);
            interfaces.principal.setPropiedades("LVertical", "true");
        }else{
            interfaces.add_select.jTable1.setShowVerticalLines(false);
            interfaces.indicador_Logro.jTable1.setShowVerticalLines(false);
            interfaces.principal.Tabla_izquierda.setShowVerticalLines(false);
            interfaces.principal.Tabla_derecha.setShowVerticalLines(false);
            interfaces.principal.setPropiedades("LVertical", "false");
        }
        
    }
    
    public void setSeccion(int seccion) {
        this.seccion = seccion;
    }
    
    private void updateCalificacion(Object calificacion){
        try {
            dtos.calificaciones.setCalificacion(Byte.parseByte(calificacion.toString()));
            modelos.Calificaciones.search(dtos.calificaciones);
            dtos.fillParametros_de_RP(dtos.calificaciones, dtos.prametroRP, dtos.registro_Personal, 
                                      dtos.tema);
            modelos.parametro_de_RP.update(dtos.parametros_de_RP);
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al actualizar una "
                                                        + "calificacion\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }
        
}
