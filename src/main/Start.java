/*
 * La variable identificador se utiliza para identifecar las interfaces y ejecutar
 * diferentes acciones en la clase AddorSelect dependiendo del numero, el 0 indica que la app esta recien
 * abierta, el 1 es para el evento que agrega la asistencia, el 2 es para cargar las unidades en la tabla
 * de la clase addOrSelect, el 3 es para la carga de las competencias, el 4 es para cargar las asignaturas
 * el 5 espara la carga de los cursos, el 6 es para evitar conflictos en la carga de los temas cuando se
 * valla a editar un tema en clase indicadores_Logro, el 7 es para poder editar la unidad, el -3 es para
 * cargar las competencias desde la interfaz de addIndecadorLogro, el -5 es para indicar que se va a
 * reaizar una transferencia de alumno entre los cursos, el 8 es para cuando se selecciones el item de
 * agregar los alumnos al indicador el click a la tabla lo permita agregar de una vez, 9 es para que cuando
 * se vsya a cargar los indicadores desde la vista addIndicadorLogro, 10 es para que el boton de la interfaz
 * indicadoresLogro permita seleccionr los datos que va relacionados con la los registros personales, el 11
 * es para transferir los alumnos a otro curso, el 12 es para transferir los indicadores de logro. 
 * 
 * 
 */

package main;

import controllers.singletons.Singleton_Controllers;
import controllers.singletons.SingletonDtos;
import controllers.singletons.Singleton_Interfaces;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import models.Conector;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.*;
import javax.swing.JFrame;
import views.Carga;
import views.RegistroError;

/**
 * @author Work
 */

public class Start extends Conector{
    private static float conteo;
    private static boolean cargando;
    public static byte identificador;
    public static float maxConteo;
    private static Thread hilo_arranque;
    
    
    private static Singleton_Controllers controladores;
    private SingletonDtos dtos;
    
    public Start() {
        conteo = (float) 0.0;
        cargando = false;
        identificador = 0;        
        runMainHilo();
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Start.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        new Start();
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RegistroError re = new RegistroError(new JFrame(), false);
                re.crearLog();
                new Carga(hilo_arranque).setVisible(true);
            }
        });
    }
    
    public static float getMaxConteo() {
        return maxConteo;
    }

    private static void leerPropiedades(){
        InputStream in = null;
        try {
            Properties pr = new Properties();
            final String caracter = System.getProperty("file.separator");
            in = new FileInputStream(System.getProperty("user.home")+caracter+"Documents"+caracter+
                    "gestor_notas_dataBase"+caracter+"config.properties");
            pr.load(in);
            //pr.entrySet().forEach(System.out::println);
            Singleton_Interfaces interfaces = Singleton_Interfaces.getInstance();
            if(pr.getProperty("LHorizontal").equals("true")){
                controladores.principal.setLineas((byte)0);
                interfaces.principal.JMICHBHorizontal.setSelected(true);
            }
            if(pr.getProperty("LVertical").equals("true")){
                controladores.principal.setLineas((byte)1);
                interfaces.principal.JMICHBVertical.setSelected(true);
            }
        } catch (FileNotFoundException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error detectado al "
                                                                  + "intentar leer las prropiedades "
                                                                  + "de la app\n");
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex));
            
        } catch (IOException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error detectado al "
                                                                  + "intentar leer las prropiedades "
                                                                  + "de la app\n");
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex));
        } finally {
            try {
                if(in != null) in.close();
            } catch (IOException ex) {
                new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error detectado al "
                                                                  + "intentar leer las prropiedades "
                                                                  + "de la app\n");
                new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex));
            }
        }
    }
   
    public void runMainHilo(){
        hilo_arranque = new Thread(()->{
            cargando = true;
            if(maxConteo == 0.0 && conteo > maxConteo)    conteo = 0.0f;
            else if(maxConteo == 0.0)    conteo = 0.0f;
            try(Connection cn = conect();){
                conectarDB();
            } catch (SQLException ex) {
                new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error detectado "
                                                                  + "el hilo de nicio de la app\n");
                new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex)); 
            }            
            
            controladores = Singleton_Controllers.getInstance();
            dtos = SingletonDtos.getInstance();
            controladores.add_select.cargador();
            leerPropiedades();
            cargando = false;
        });
    }

    public static void setMaxConteo(float maxConteo) {
        Start.maxConteo = maxConteo;
    }
    
    //Getters and setters
    public static boolean isCargando() {
        return cargando;
    }

    public static void setCargando(boolean cargando) {
        Start.cargando = cargando;
    }

    public static float getConteo() {
        return conteo;
    }

    public static void setConteo(float conteo) {
        Start.conteo = conteo;
    }
    
    
}
