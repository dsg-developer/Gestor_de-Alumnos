/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import config.DB;
import config.PathDB;
import controllers.singletons.SingletonDtos;
import controllers.singletons.SingletonModels;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.*;
import java.sql.*;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import main.Start;
import views.RegistroError;
/**
 *
 * @author Work
 */
public class Conector {
    private static Connection cn = null;
    static Connection cnMySQL = null;
    private static PathDB ruta_DB = new PathDB();
    
    private void actualizarApp(){
        final File ruta = new File("gestor.exe");
        final String query = "select * from actualizar where id = 1";
        try (Connection cn = conectMySQL(); PreparedStatement pst = cn.prepareStatement(query);
             ResultSet rs = pst.executeQuery();){
            if(rs.next()){
                Blob b = rs.getBlob("archivo");
                InputStream in = b.getBinaryStream();                                
                try (OutputStream out = new FileOutputStream(ruta);){
                    
                    byte[] binary = new byte[2097150];
                    int len = 0;
                    while((len = in.read(binary)) > 0){
                        out.write(binary, 0, len);
                    }
                        out.flush();
                        out.close();
                        in.close();
                } catch (IOException ex) {
                    new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error "
                                                                               + "detectado al escribir al "
                                                                                + "escribir la actualizacion "
                                                                            + "de la app\n");
                        new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex));
                }
                Runtime rt = Runtime.getRuntime();
                Process p =rt.exec(ruta.getAbsolutePath());
                System.exit(0);
            }
        } catch (SQLException | IOException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error detectado al "
                                                                               + "escribir al escribir la "
                                                                              + "actualizacion de la app\n");
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex));
        }
    }
    
    private void addCompetencia(){
        final File ruta = new File("competencias.txt");
        final String query = "select * from actualizar where id = 1";
        try (Connection cn = conectMySQL(); PreparedStatement pst = cn.prepareStatement(query);
             ResultSet rs = pst.executeQuery();){
            if(rs.next()){
                Blob b = rs.getBlob("adicional_1");
                InputStream in = b.getBinaryStream();                                
                try (OutputStream out = new FileOutputStream(ruta);){
                    
                    byte[] binary = new byte[2097150];
                    int len = 0;
                    while((len = in.read(binary)) > 0){
                        out.write(binary, 0, len);
                    }
                        out.flush();
                        out.close();
                        in.close();
                } catch (IOException ex) {
                    new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error "
                                                                         + "detectado al escribir el archivo "
                                                                          + "que contiene las competencias "
                                                                            + "de la app\n");
                        new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex));
                }
                Runtime rt = Runtime.getRuntime();
                Process p =rt.exec(ruta.getAbsolutePath());
                System.exit(0);
            }
        } catch (SQLException | IOException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error "
                                                                  + "detectado al escribir el archivo "
                                                                  + "que contiene las competencias "
                                                                            + "de la app\n");
                        new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex));
        }
        addIndicador();
    }
    
    private void addIndicador(){
        final File ruta = new File("indicadores.txt");
        final String query = "select * from actualizar where id = 1";
        try (Connection cn = conectMySQL(); PreparedStatement pst = cn.prepareStatement(query);
             ResultSet rs = pst.executeQuery();){
            if(rs.next()){
                Blob b = rs.getBlob("adicional_2");
                InputStream in = b.getBinaryStream();                                
                try (OutputStream out = new FileOutputStream(ruta);){
                    
                    byte[] binary = new byte[2097150];
                    int len = 0;
                    while((len = in.read(binary)) > 0){
                        out.write(binary, 0, len);
                    }
                        out.flush();
                        out.close();
                        in.close();
                } catch (IOException ex) {
                    new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error "
                                                                         + "detectado al escribir el archivo "
                                                                          + "que contiene los indicadores "
                                                                            + "de la app\n");
                        new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex));
                }
                Runtime rt = Runtime.getRuntime();
                Process p =rt.exec(ruta.getAbsolutePath());
                System.exit(0);
            }
        } catch (SQLException | IOException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error "
                                                                     + "detectado al escribir el archivo "
                                                                      + "que contiene los indicadores "
                                                                        + "de la app\n");
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex));
        }
        insertarCompetencias();
        insertarIdicadores();
    }
    
    public void actuaizar(){
        actualizarApp();
    }
    
    protected void conectarDB(){
        if(ruta_DB.archivo.exists() && ruta_DB.archivo.length() == 0){
            Start.setMaxConteo((float)93.699135);
            DB db = new DB();
            db.createDB();
            insertarCompetencias();
            insertarIdicadores();
            crearPropiedades(ruta_DB.carpeta.getAbsolutePath());
        }else{
            if(ruta_DB.archivo.length() < 266240){
                ruta_DB.archivo.delete();
                Start.setMaxConteo((float)72.299515);
                DB db = new DB();
                db.createDB();
                addCompetencia();
            }
            Start.maxConteo = 1.6000001f;
        }
    }
    
    private Connection conectMySQL(){
        try {             
            cnMySQL = DriverManager.getConnection("jdbc:mysql://"+ruta_DB.getDB(), ruta_DB.getUsuario()
                                                  ,ruta_DB.getPassword());
            return cnMySQL;
        } catch (SQLException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error detectado al "
                                                                  + "intentar conectarse a la db remota\n");
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex));
            JOptionPane.showMessageDialog(null, "No se pudo estableser la conexión con el servidor, "+
                                                  "por favor\nintentelo de nuevo más tarde o revise su "
                                                + "conexión a internet", 
                                                  "Informacion de error", JOptionPane.INFORMATION_MESSAGE);
        }
        return (null);
    }
    
    protected Connection conect(){
        try {             
            cn = DriverManager.getConnection("jdbc:sqlite:"+ruta_DB.archivo.getAbsolutePath());
            return cn;
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Se ha generado un error al intentar cominicarse con el almacen de datos\n"+
                                            ex, "Prolemas en la comunicacion con el almacen", JOptionPane.ERROR_MESSAGE);
        }
        return (null);
    }
    
    private void crearPropiedades(String ruta){
        final String caracter = System.getProperty("file.separator");
        FileOutputStream path = null;
        try {
            Properties p = new Properties();
            path = new FileOutputStream(ruta+caracter+"config.properties");
            propiedades(p);
            p.store(path, "");            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (path != null) path.close();
            } catch (IOException ex) {
                Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void insertarCompetencias(){
        try {
            final File ruta = new File("competencias.txt");
            final FileReader leer = new FileReader(ruta);
            final BufferedReader leerRam = new BufferedReader(leer);
            String fila = null;
            
            final SingletonDtos dtos = SingletonDtos.getInstance();
            final SingletonModels modelos = SingletonModels.getInstance();
            
            dtos.calificaciones.setCalificacion((byte)0);
            modelos.Calificaciones.create(dtos.calificaciones);
            Start.setConteo(Start.getConteo()+0.1f);
                      
            while((fila = leerRam.readLine()) != null){
                dtos.competencia.setCompetencia(fila);
                modelos.competencia.create(dtos.competencia);
                Start.setConteo(Start.getConteo()+0.1f);
            }
            leer.close();
            leerRam.close();
        } catch (FileNotFoundException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error detectado al "
                                                                  + "intentar insertar las competencias en "
                                                                  + "el almacen de registros de la app\n");
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex));
        } catch (IOException | DaoException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error detectado al "
                                                                  + "intentar insertar las competencias en "
                                                                  + "el almacen de registros de la app\n");
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex));
        }
        Start.setConteo(Start.getConteo()+1.0f);
    }
    
    private static void insertarIdicadores(){
        final File ruta = new File("indicadores.txt");
        try {
            final FileReader leer = new FileReader(ruta);
            final BufferedReader leerRam = new BufferedReader(leer);
            String fila = null;
            
            final SingletonDtos dtos = SingletonDtos.getInstance();
            final SingletonModels modelos = SingletonModels.getInstance();
                                  
            while((fila = leerRam.readLine()) != null){
                dtos.indicador.setIndicador(fila);
                modelos.indicador.create(dtos.indicador);
                Start.setConteo(Start.getConteo()+0.1f);
            }
            leer.close();
            leerRam.close();
        } catch (FileNotFoundException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error detectado al "
                                                                  + "intentar insertar los indicadores en "
                                                                  + "el almacen de registros de la app\n");
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex));
        } catch (IOException | DaoException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error detectado al "
                                                                  + "intentar insertar los indicadores en "
                                                                  + "el almacen de registros de la app\n");
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex));
        }
        Start.setConteo(Start.getConteo()+1.0f);
    }
    
    protected int lastID(String tabla){
        String query = "select id from "+tabla+" where id = (select max(id) from "+tabla+")";
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(query); ResultSet rs = pst.executeQuery();){
            if(rs.next()) return rs.getInt(1);  else return 0;
        } catch (SQLException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error detectado al "
                                                                  + "intentar obtener el ultimo ID del "
                                                                  + "almacen de registros de la app\n");
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                          RegistroError.getStackTrace(ex));
            return 0;
        }
    }
    
    private void propiedades(Properties propiedad){
        propiedad.setProperty("LHorizontal", "false");
        propiedad.setProperty("LVertical", "false");
        //propiedad.setProperty("", "");
    }
    
    
}
