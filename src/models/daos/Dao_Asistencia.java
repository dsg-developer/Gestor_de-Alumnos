package models.daos;

import controllers.singletons.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import main.Start;
import models.Conector;
import models.DaoException;
import models.GenericMethodSQL;
import models.GenericObject;
import models.dtos.DtoAlumno;
import models.dtos.Dto_Asistencia;
import models.dtos.Dto_Fecha;
import views.RegistroError;

/**
 * @author Work
 */

public class Dao_Asistencia extends Conector implements GenericMethodSQL<Dto_Asistencia>{
    private final String createQuery = "INSERT INTO asistencias(id_alumno, id_fecha) values(?,?)";
    
    private final String readQuery = "SELECT asistencias.id, asistencias.id_fecha, asistencias.id_alumno, "
                                   + "alumnos.numero, alumnos.apellido, alumnos.nombre, "
                                   + "asistencias.asistencia, fechas.fecha FROM asistencias "
                                   + "INNER JOIN alumnos ON asistencias.id_alumno = alumnos.id "
                                   + "INNER JOIN fechas ON asistencias.id_fecha = fechas.id "
                                   + "INNER JOIN alumno_cay ON asistencias.id_alumno = alumno_cay.id_alumno "
                                   + "WHERE alumno_cay.id_curso=? AND alumno_cay.id_asignatura=? AND "
                                   + "alumno_cay.id_year_escolar=? AND fechas.fecha = ? "
                                   + "ORDER BY alumnos.numero";
    
    private final String read_Fecha = "SELECT asistencias.id_fecha, fechas.fecha FROM asistencias "
                                    + "INNER JOIN fechas ON asistencias.id_fecha = fechas.id "
                                    + "INNER JOIN alumno_cay ON asistencias.id_alumno = "
                                    + "alumno_cay.id_alumno "
                                    + "WHERE alumno_cay.id_curso=? AND alumno_cay.id_asignatura=? AND "
                                    + "alumno_cay.id_year_escolar=? ";
    
    private final String searchQuery = "SELECT asistencias.id FROM asistencias "
                                     + "INNER JOIN alumno_cay ON asistencias.id_alumno = alumno_cay.id_alumno "
                                     + "WHERE asistencias.id_alumno=? AND alumno_cay.id_curso=? AND "
                                     + "alumno_cay.id_asignatura=? AND alumno_cay.id_year_escolar=? AND "
                                     + "asistencias.id_fecha=? ";
    
    private final String sumaQuery = "SELECT count(asistencias.asistencia), sum(asistencias.asistencia) "
                                   + "FROM asistencias "
                                   + "INNER JOIN alumno_cay ON asistencias.id_alumno = alumno_cay.id_alumno "
                                   + "WHERE asistencias.id_alumno = ? AND alumno_cay.id_curso=? AND "
                                   + "alumno_cay.id_asignatura=? AND alumno_cay.id_year_escolar=? ";
        
    public boolean isExists = false;
    Singleton_Interfaces interfaces;
    SingletonDtos dtos;
    
    public Dao_Asistencia() {
        dtos = SingletonDtos.getInstance();
        interfaces = Singleton_Interfaces.getInstance();
        Start.setConteo(Start.getConteo()+0.1f);
    }

    @Override
    public void create(Dto_Asistencia t) throws DaoException {
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery)){
            pst.setInt(1, t.getAlumno().getID());
            pst.setInt(2, t.getFecha().getID());
            pst.execute();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear una "
                                                        + "asistencia\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            JOptionPane.showMessageDialog(null, "Se ha generado un error al tratar de almacenar las asistencias\n"+ex,
                                                "Error",JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public List<Dto_Asistencia> read(Dto_Asistencia t) throws DaoException {
        ResultSet rs = null;
        Singleton_Interfaces interfaces = Singleton_Interfaces.getInstance();
        String fecha = interfaces.principal.getFecha((byte)0);
        List<Dto_Asistencia> respuesta = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(readQuery);){
            pst.setInt(1, dtos.curso.getID());
            pst.setInt(2, dtos.asignatura.getID());
            pst.setInt(3, dtos.yearEscolar.getID());
            pst.setString(4, interfaces.principal.getFecha((byte)1));
            
            rs = pst.executeQuery();
            if(rs.next()){
                respuesta = new ArrayList<>();
                do{
                    respuesta.add(new Dto_Asistencia(rs.getInt(1), new DtoAlumno(rs.getInt(3), 
                                         rs.getString(5), rs.getString(6), rs.getByte(4)),
                                        dtos.fillFecha(rs.getInt(2), rs.getString(8)), rs.getByte(7)));
                }while(rs.next());
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer las "
                                                        + "asistencias\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)  rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo read de la clase asistencia\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }return (respuesta);
    }

    public List<Dto_Asistencia> readFechas(){
        List<Dto_Asistencia> respuesta = null;
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(read_Fecha);){
            pst.setInt(1, dtos.curso.getID());
            pst.setInt(2, dtos.asignatura.getID());
            pst.setInt(3, dtos.yearEscolar.getID());
            
            rs = pst.executeQuery();
            if(rs.next()){
                respuesta = new ArrayList<>();
                do {                    
                    respuesta.add(new Dto_Asistencia(0, null, new Dto_Fecha(rs.getInt(1), 
                                                      rs.getString(2)), (byte)0));
                } while(rs.next());
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer las "
                                                        + "fechas\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo readfecha de la clase asistencia\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
        return respuesta;
    }
    
    @Override
    public void update(Dto_Asistencia t) throws DaoException {
        String[] cay = interfaces.principal.getCay();
        String UpdateQuery = "UPDATE asistencias SET asistencia=? WHERE id = "+t.getID();
        
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(UpdateQuery)){
            pst.setInt(1, t.getAsitencia());
            pst.executeUpdate();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al actualizar una "
                                                        + "asistencia\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            JOptionPane.showMessageDialog(null, "Se ha generado un error al tratar de modificar la asistencia"
                                              + "\n"+ex, "Error",JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void delete(Dto_Asistencia t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(Dto_Asistencia t) throws DaoException {
        ResultSet rs = null;
        if(!isExists){
            try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(searchQuery)){
                pst.setInt(1, t.getAlumno().getID());
                pst.setInt(2, dtos.curso.getID());
                pst.setInt(3, dtos.asignatura.getID());
                pst.setInt(4, dtos.yearEscolar.getID());
                pst.setInt(5, t.getFecha().getID());

                rs = pst.executeQuery();
                if(rs.next()){
                    isExists = true;
                }else{
                    isExists = false;
                    create(t);
                }
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar una "
                                                        + "asistencia\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }finally{
                try {
                    if(rs != null)   rs.close();
                } catch (SQLException ex) {
                    interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo search de la clase asistencia\n");
                    interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
                }
            }
        }
    }
    
    public double sacarPociento(int idAlumno){
        double respuesta = 0;
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(sumaQuery);){
            pst.setInt(1, idAlumno);
            pst.setInt(2, dtos.curso.getID());
            pst.setInt(3, dtos.asignatura.getID());
            pst.setInt(4, dtos.yearEscolar.getID());
            
            rs = pst.executeQuery();
            if(rs.next())
                respuesta = (rs.getDouble(2)/rs.getDouble(1))*100;
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al sacar el "
                                                        + "porcentaje\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.next();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo que saca el porcentaje en la clase"
                                                        + "asistencia\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
        return respuesta;
    }
    
}
