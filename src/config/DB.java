package config;

import controllers.singletons.Singleton_Interfaces;
import java.sql.*;
import main.Start;
import java.util.logging.*;
import javax.swing.JOptionPane;
import models.Conector;
import views.RegistroError;

/**
 * @author Work
 */

public class DB extends Conector{
    private Singleton_Interfaces interfaces = Singleton_Interfaces.getInstance();
    
    private void asistencia(Connection cn) {
        try {
            PreparedStatement p = cn.prepareStatement("CREATE TABLE IF NOT EXISTS asistencias("+
                                                      "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                      "id_alumno INTEGER NOT NULL," +
                                                      "id_fecha INTEGER DEFAULT null," +
                                                      "asistencia INTEGER DEFAULT 0," +
                                                      "FOREIGN KEY(id_alumno) REFERENCES alumnos(id)," +
                                                      "FOREIGN KEY(id_fecha) REFERENCES fechas(id) )");
            p.execute();    p.close();  Start.setConteo(Start.getConteo()+1.0f);
            
            registroPersonal(cn);
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear la tabla "
                                                        + "de asistencia\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            JOptionPane.showMessageDialog(null, "Se ha generado un error en la construccion del almacen \n"+ex.getMessage(),
                                                "Error al contruir el almacen", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void createDB(){
        try (Connection cn = conect();){
            PreparedStatement p = cn.prepareStatement("CREATE TABLE IF NOT EXISTS alumnos("+
                                                      "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                      "apellido varchar(28) DEFAULT NULL," +
                                                      "nombre varchar(21) NOT NULL," +
                                                      "numero INTEGER DEFAULT 0 )");
            p.execute();    p.close();  Start.setConteo(Start.getConteo()+1.0f);
            
            PreparedStatement pr = cn.prepareStatement("CREATE TABLE IF NOT EXISTS alumno_cay("+
                                                       "id_alumno INTEGER NOT NULL,"+
                                                       "id_curso INTEGER NOT NULL," +
                                                       "id_asignatura INTEGER NOT NULL,"+
                                                       "id_year_escolar INTEGER NOT NULL," +
                                                       "promedio REAL DEFAULT 0.00," +
                                                       "porsentaje_Asistencia REAL DEFAULT 0.00," +
                                                       "PRIMARY KEY(id_alumno, id_curso, id_asignatura, "+
                                                       "id_year_escolar),"+
                                                       "FOREIGN KEY(id_alumno) REFERENCES alumnos(id),"+
                                                       "FOREIGN KEY(id_curso) REFERENCES cursos(id),"+
                                                       "FOREIGN KEY(id_asignatura) REFERENCES "+
                                                       "asignaturas(id) "+
                                                       "FOREIGN KEY(id_year_escolar) REFERENCES "+
                                                       "years_escolar(id) )");
            
            pr.execute();   pr.close(); Start.setConteo(Start.getConteo()+1.0f);
            
            PreparedStatement pre = cn.prepareStatement("CREATE TABLE IF NOT EXISTS years_escolar(" +
                                                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                        "year_escolar varchar(9) NOT NULL)");
            pre.execute();  pre.close();    Start.setConteo(Start.getConteo()+1.0f);
            
            curso(cn);             
         } catch (SQLException ex) {
             interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear las "
                                                        + "tablas del metodo createDB\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
             JOptionPane.showMessageDialog(null, "Se ha generado un error en la construccion del almacen \n"+ex.getMessage(),
                                                "Error al contruir el almacen", JOptionPane.ERROR_MESSAGE);
         } 
     }

    private void curso(Connection cn) {
        try {
            PreparedStatement p = cn.prepareStatement("CREATE TABLE IF NOT EXISTS asignaturas(" +
                                                      "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                      "asignatura VARCHAR(42) NOT NULL)");
            p.execute();    p.close();  Start.setConteo(Start.getConteo()+1.0f);
            
            PreparedStatement pr = cn.prepareStatement("CREATE TABLE IF NOT EXISTS calificaciones(" +
                                                       "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                       "calificacion INTEGER NOT NULL)");
            pr.execute();    pr.close();    Start.setConteo(Start.getConteo()+1.0f);
            
            PreparedStatement pre = cn.prepareStatement("CREATE TABLE IF NOT EXISTS cursos(" +
                                                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                        "curso varchar(32) NOT NULL)");
            pre.execute();    pre.close();  Start.setConteo(Start.getConteo()+1.0f);
            
            PreparedStatement prep = cn.prepareStatement("CREATE TABLE IF NOT EXISTS cay(" +
                                                        "id_curso INTEGER NOT NULL," +
                                                        "id_asignatura INTEGER NOT NULL," +
                                                        "id_year INTEGER NOT NULL," +
                                                        "PRIMARY KEY(id_curso, id_asignatura, id_year)," +
                                                        "FOREIGN KEY(id_asignatura) REFERENCES agisnaturas(id)," +
                                                        "FOREIGN KEY(id_curso) REFERENCES cursos(id)," +
                                                        "FOREIGN KEY(id_year) REFERENCES years(id) )");
            prep.execute();   prep.close(); Start.setConteo(Start.getConteo()+1.0f);
            
            indicadoresLogros(cn);            
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear las "
                                                        + "tablas del metodo curso\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            JOptionPane.showMessageDialog(null, "Se ha generado un error en la construccion del almacen \n"+ex.getMessage(),
                                                "Error al contruir el almacen", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void indicadoresLogros(Connection cn) {
        try {
            PreparedStatement p = cn.prepareStatement("CREATE TABLE IF NOT EXISTS calificacion_indicadores(" +
                                                      "id_alumno INTEGER NOT NULL," +
                                                      "id_calificacion INTEGER NOT NULL," +
                                                      "id_indicador_logro INTEGER NOT NULL," +
                                                      "FOREIGN KEY(id_alumno) REFERENCES alumnos(id)," +
                                                      "FOREIGN KEY(id_alumno) REFERENCES calificaciones(id)," +
                                                      "FOREIGN KEY(id_indicador_logro) REFERENCES "+
                                                      "indicador_logro(id) )");
            p.execute();   p.close();   Start.setConteo(Start.getConteo()+1.0f);
            
            PreparedStatement pr = cn.prepareStatement("CREATE TABLE IF NOT EXISTS competencias(" +
                                                       "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                       "competencia text NOT NULL)");
            pr.execute();   pr.close(); Start.setConteo(Start.getConteo()+1.0f);
            
            PreparedStatement pre = cn.prepareStatement("CREATE TABLE IF NOT EXISTS indicadores(" +
                                                       "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                       "indicador text NOT NULL)");
            pre.execute();   pre.close(); Start.setConteo(Start.getConteo()+1.0f);
            
            PreparedStatement prep = cn.prepareStatement("CREATE TABLE IF NOT EXISTS indicador_logro(" +
                                                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                        "id_asignatura INTEGER NOT NULL," +
                                                        "id_competencia INTEGER NOT NULL," +
                                                        "id_curso INTEGER NOT NULL," +
                                                        "id_indicador INTEGER NOT NULL," +
                                                        "id_tema INTEGER NOT NULL," +
                                                        "id_unidad INTEGER NOT NULL," +
                                                        "id_year_escolar INTEGER NOT NULL," +
                                                        "periodo INTEGER DEFAULT NULL," +
                                                        "FOREIGN KEY(id_competencia) REFERENCES "
                                                      + "competencias(id)," +
                                                        "FOREIGN KEY(id_curso) REFERENCES "
                                                      + "cursos(id)," +
                                                        "FOREIGN KEY(id_indicador) REFERENCES "
                                                      + "indicadores(id)," +
                                                        "FOREIGN KEY(id_asignatura) REFERENCES "
                                                      + "asignaturas(id)," +
                                                        "FOREIGN KEY(id_year_escolar) REFERENCES "
                                                      + "years_escolar(id)," +
                                                        "FOREIGN KEY(id_unidad) REFERENCES unidades(id) )");
            prep.execute();   prep.close();   Start.setConteo(Start.getConteo()+1.0f);
                       
            PreparedStatement prepa = cn.prepareStatement("CREATE TABLE IF NOT EXISTS temas(" +
                                                         "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                         "tema varchar(175) NOT NULL)");
            prepa.execute();   prepa.close();   Start.setConteo(Start.getConteo()+1.0f);
            
            PreparedStatement prepar = cn.prepareStatement("CREATE TABLE IF NOT EXISTS unidades(" +
                                                         "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                         "unidad varchar(175) NOT NULL)");
            prepar.execute();   prepar.close(); Start.setConteo(Start.getConteo()+1.0f);
            
            asistencia(cn);
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear las "
                                                        + "tablas del metodo indicadoresLogro\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            JOptionPane.showMessageDialog(null, "Se ha generado un error en la construccion del almacen \n"+ex.getMessage(),
                                                "Error al contruir el almacen", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registroPersonal(Connection cn) {
        try {
            PreparedStatement p =cn.prepareStatement("CREATE TABLE IF NOT EXISTS fechas(" +
                                                     "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                     "fecha text DEFAULT null)");
            p.execute();    p.close();  Start.setConteo(Start.getConteo()+1.0f);
            
            PreparedStatement pr = cn.prepareStatement("CREATE TABLE IF NOT EXISTS parametros_rp(" +
                                                      "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                      "parametro varchar(52) NOT NULL)");
            pr.execute();   pr.close(); Start.setConteo(Start.getConteo()+1.0f);
            
            PreparedStatement pre = cn.prepareStatement("CREATE TABLE IF NOT EXISTS parametro_de_rp(" +
                                                        "id_calificacion INTEGER NOT NULL," +
                                                        "id_parametro_rp INTEGER NOT NULL," +
                                                        "id_rp INTEGER NOT NULL," +
                                                        "id_tema INTEGER NOT NULL," +
                                                        "PRIMARY KEY(id_calificacion, id_parametro_rp, "
                                                        + "id_rp, id_tema)," +
                                                        "FOREIGN KEY(id_rp) REFERENCES registro_personal(id),"+
                                                        "FOREIGN KEY(id_parametro_rp) REFERENCES "+
                                                        "parametro_rp(id)," +
                                                        "FOREIGN KEY(id_tema) REFERENCES temas(id)," +
                                                        "FOREIGN KEY(id_calificacion) REFERENCES "+
                                                        "calificaciones(id))");
            pre.execute();   pre.close();   Start.setConteo(Start.getConteo()+1.0f);
            
            PreparedStatement prep = cn.prepareStatement("CREATE TABLE IF NOT EXISTS registro_personal(" +
                                                         "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                                         "id_alumno INTEGER NOT NULL," +
                                                         "id_fecha INTEGER NOT NULL," +
                                                         "id_indicador_logro INTEGER NOT NULL," +
                                                         "FOREIGN KEY(id_alumno) REFERENCES alumnos(id)," +
                                                         "FOREIGN KEY(id_indicador_logro) REFERENCES "+
                                                         "indicador_logro(id), " +
                                                         "FOREIGN KEY(id_fecha) REFERENCES fechas(id))");
            prep.execute();    prep.close();    Start.setConteo(Start.getConteo()+1.0f);     
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear las "
                                                        + "tablas del metodo registroPersonal\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            JOptionPane.showMessageDialog(null, "Se ha generado un error en la construccion del almacen \n"+ex.getMessage(),
                                                "Error al contruir el almacen", JOptionPane.ERROR_MESSAGE);
        }
    }
}
