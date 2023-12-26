package models;

import config.PathDB;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import views.RegistroError;

/**
 *
 * @author Work-Game
 */
public class ConectDBRemote{
    private static PathDB ruta_DB = new PathDB();
    static Connection cnMySQL = null;
    
    public static void main(String[] args) {
        //createDB();
        //createTables();
        subirDato();
        //readTables();
        //actualizarApp();
    }
    
    private static void createDB(){
        String create = "create database updates";
        try (Connection cn = conectMySQL(); PreparedStatement pst = cn.prepareStatement(create);){
            pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ConectDBRemote.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void createTables(){
        final String create = "create table if not exists actualizar("
                                                            + "id integer primary key auto_increment, "
                                                            + "version varchar(12) not null,"
                                                            + "archivo blob not null, "
                                                            + "fecha datetime not null)";
        final String editar = "alter table archivo change column archivo longblob not null";
        try (Connection cn = conectMySQL(); PreparedStatement pst = cn.prepareStatement(editar);){
            pst.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ConectDBRemote.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void subirDato(){
        final String create = "update actualizar set archivo=?, fecha= current_timestamp() where id = 1";
        final String caracter = System.getProperty("file.separator");
        final String ruta = System.getProperty("user.home")+caracter+"Documents"+caracter+"NetBeansProjects"
                            +caracter+"gestor_alumnos"+caracter+"dist"+caracter+"gestor.exe";
        try (Connection cn = conectMySQL(); PreparedStatement pst = cn.prepareStatement(create);){
            File rutaFile = new File(ruta);
            FileInputStream archivo = new FileInputStream(rutaFile);
            pst.setBlob(1, archivo, rutaFile.length());
            pst.executeUpdate();
            archivo.close();
        } catch (SQLException | IOException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error "
                                                                               + "detectado al subir datos "
                                                                                + "a la db remota\n");
                        new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                  RegistroError.getStackTrace(ex));
        }
    }
    
    private static void readTables(){
        final String create = "select * from actualizar";
        try (Connection cn = conectMySQL(); PreparedStatement pst = cn.prepareStatement(create);
                ResultSet rs = pst.executeQuery();){
            if(rs.next()){
                do {                    
                    System.out.println(rs.getString("version")+", "+rs.getString("fecha"));
                } while (rs.next());
            }
        } catch (SQLException ex) {
            new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, "Nuevo error "
                                                                               + "detectado al leer las "
                                                                                + "tablas\n");
                        new RegistroError(new JFrame(), false).registrarError(Level.SEVERE, 
                                                                  RegistroError.getStackTrace(ex));
        }
    }
    
    private static void actualizarApp(){
        final String caracter = "file.separator";
        final File ruta = new File("escanor.jpg");
        final String query = "select * from actualizar where id = 1";
        try (Connection cn = conectMySQL(); PreparedStatement pst = cn.prepareStatement(query);
             ResultSet rs = pst.executeQuery();){
            if(rs.next()){
                Blob b = rs.getBlob("archivo");
                InputStream in = b.getBinaryStream();                                
                try {
                    OutputStream out = new FileOutputStream(ruta);
                    byte[] binary = new byte[1024];
                    int len = 0;
                    System.err.println(ruta.getAbsolutePath());
                    while((len = in.read(binary)) > 0){
                        out.write(binary, 0, len);
                    }
                        out.flush();
                        out.close();
                        in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static Connection conectMySQL(){
        try {             
            cnMySQL = DriverManager.getConnection("jdbc:mysql://"+ruta_DB.getDB(),ruta_DB.getUsuario(),
                                                  ruta_DB.getPassword());
            return cnMySQL;
        } catch (SQLException ex) {
            Logger.getLogger(ConectDBRemote.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Se ha generado un error al intentar cominicarse con el almacen de datos remoto\n"+
                                            ex, "Prolemas en la comunicacion con el almacen", JOptionPane.ERROR_MESSAGE);
        }
        return (null);
    }
}
