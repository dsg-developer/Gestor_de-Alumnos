package models.daos;

import controllers.Controller_Principal;
import controllers.singletons.Singleton_Class;
import controllers.singletons.Singleton_Controllers;
import controllers.singletons.SingletonDtos;
import controllers.singletons.Singleton_Interfaces;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import main.Start;
import models.Conector;
import models.DaoException;
import models.GenericMethodSQL;
import models.GenericObject;
import models.dtos.DtoAlumno;
import views.RegistroError;

/**
 * @author Work
 */

public class Dao_Alumno extends Conector implements GenericMethodSQL<DtoAlumno>{

    public Dao_Alumno() {
        dtos = SingletonDtos.getInstance();
        interfaces = Singleton_Interfaces.getInstance();
        Start.setConteo(Start.getConteo()+0.1f);
    }
    
    private final SingletonDtos dtos;
    private Singleton_Controllers controladores;
    private Singleton_Interfaces interfaces;
    
    private final String createQuery = "INSERT INTO alumnos (apellido, nombre, numero)"
                                     + "values(?,?,?)";
    
    private final String readQuery = "SELECT id_alumno FROM alumno_cay "
                                   + "WHERE id_curso=? AND id_asignatura=? "
                                   + "AND id_year_escolar=? ";
    
    private final String searchQuery = "SELECT id FROM alumnos WHERE nombre=? and apellido=?";
    
    private final String notasQuery = "SELECT alumnos.id, alumnos.numero, alumnos.apellido, alumnos.nombre "
                                    + "FROM alumno_cay "
                                    + "INNER JOIN alumnos ON alumno_cay.id_alumno = alumnos.id "
                                    + "WHERE alumno_cay.id_curso=? AND alumno_cay.id_asignatura=? "
                                    + "AND alumno_cay.id_year_escolar=? ORDER BY alumnos.numero ";

    @Override
    public void create(DtoAlumno t) throws DaoException {
        byte error = 0;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery);){
            pst.setString(1, t.getApellido());
            pst.setString(2, t.getNombre());
            pst.setInt(3, t.getNumero());
            pst.execute();
        } catch (SQLException ex) {
            error++;
            Start.setCargando(false);
            System.err.println(ex.getMessage());
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear un "
                                                        + "alumno\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            JOptionPane.showMessageDialog(null, "Se ha generado un error al tratar de almacenar al alumno\n"+ex,
                                                "Error",JOptionPane.WARNING_MESSAGE);
        }
        if(error == 0){
            dtos.alumno.setID(lastID("alumnos"));
        }
    }

    @Override
    public List<DtoAlumno> read(DtoAlumno t) throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(readQuery);){
            pst.setInt(1, dtos.curso.getID());
            pst.setInt(2, dtos.asignatura.getID());
            pst.setInt(3, dtos.yearEscolar.getID());
            
            rs = pst.executeQuery();
            if(rs.next()){
                List<DtoAlumno> respuesta = new ArrayList<>();
                do {                    
                    respuesta.add(new DtoAlumno(rs.getInt(1), null, null, (byte)0));
                } while (rs.next());
                return  respuesta;
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer los "
                                                        + "alumnos\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo read de la clase DaoAlumno\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
        return (null);
    }

    @Override
    public void update(DtoAlumno t) throws DaoException {
        String updateQuery = null;
        controladores = Singleton_Controllers.getInstance();
        switch(t.getEdicion()){
            case "numero": updateQuery = "update alumnos set numero=? where id = '"+t.getID()+"'";
                    break;

            case "apellido": updateQuery = "update alumnos set apellido=? where id = '"+t.getID()+"'";
                    break;

            case "nombre": updateQuery = "update alumnos set nombre=? where id = '"+t.getID()+"'";
                    break;
        }      
        
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(updateQuery);){
            switch(t.getEdicion()){
                case "numero":  pst.setInt(1, t.getNumero());
                                break;

                case "apellido": pst.setString(1, t.getApellido());
                                 break;

                case "nombre": pst.setString(1, t.getNombre());
                               break;
            } 
            pst.executeUpdate();
        } catch (Exception ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al actualizar al "
                                                        + "alumno\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }

    @Override
    public void delete(DtoAlumno t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void search(DtoAlumno t) throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(searchQuery)){
            pst.setString(1, String.valueOf(t.getNombre()));
            pst.setString(2, String.valueOf(t.getApellido()));
            
            rs = pst.executeQuery();
            if(rs.next()){
                dtos.alumno.setID(rs.getInt(1));
            }else   create(t);
        } catch (SQLException ex) {
            Start.setCargando(false);
            System.err.println(ex.getMessage());
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar al "
                                                        + "alumno\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            JOptionPane.showMessageDialog(null, "Se ha generado un error al tratar de almacenar al alumno\n"+ex,
                                                "Error",JOptionPane.WARNING_MESSAGE);
        }finally{
            try {
                if(rs != null)  rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo search de la clase DaoAlumno\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
    }

    public List<DtoAlumno>readNotas(){
        List<DtoAlumno> respuesta = null;
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(notasQuery);){
            pst.setInt(1, dtos.curso.getID());
            pst.setInt(2, dtos.asignatura.getID());
            pst.setInt(3, dtos.yearEscolar.getID());
            
            rs = pst.executeQuery();
            if(rs.next()){
                respuesta = new ArrayList<>();
                do {                    
                    respuesta.add(new DtoAlumno(rs.getInt(1), rs.getString(3), rs.getString(4),
                                                 rs.getByte(2)));
                } while (rs.next());
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer las "
                                                        + "notas\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo readNotas d la clase Daoalumno\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
        return respuesta;
    }
    
}
