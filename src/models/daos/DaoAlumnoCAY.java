package models.daos;

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
import models.dtos.DtoAlumnoCAY;
import models.dtos.DtoAlumno;
import models.dtos.DtoCurso;
import views.RegistroError;

/**
 * @author Work
 */

public class DaoAlumnoCAY extends Conector implements GenericMethodSQL<DtoAlumnoCAY>{

    public DaoAlumnoCAY() {
        interfaces = Singleton_Interfaces.getInstance();
        Start.setConteo(Start.getConteo()+0.1f);
    }
        
    private final String createQuery = "INSERT INTO alumno_cay(id_alumno, id_curso, "+
                                       "id_asignatura, id_year_escolar ) values(?,?,?,?)";
   
    private final String searchQuery = "SELECT * FROM alumno_cay WHERE id_alumno=? AND "+
                                       "id_curso=? AND id_asignatura=? AND id_year_escolar=?";
   
    private final String readQuery = "SELECT alumnos.id, alumnos.numero, alumnos.apellido, alumnos.nombre, "
                                   + "alumno_cay.promedio, alumno_cay.porsentaje_Asistencia FROM alumno_cay "
                                   + "INNER JOIN alumnos ON alumno_cay.id_alumno = alumnos.id "
                                   + "WHERE alumno_cay.id_curso=? AND alumno_cay.id_asignatura=? AND "
                                   + "alumno_cay.id_year_escolar=? ORDER BY alumnos.numero asc";
      
    private final Singleton_Interfaces interfaces;

    @Override
    public void create(DtoAlumnoCAY t) throws DaoException {
        try (Connection cn = conect();PreparedStatement pst = cn.prepareStatement(createQuery)){
            pst.setInt(1, t.getAlumno().getID());
            pst.setInt(2, t.getCurso().getID());
            pst.setInt(3, t.getAsignatura().getID());
            pst.setInt(4, t.getYear_Escolar().getID());
            pst.execute();
        } catch (SQLException ex) {
            Start.setCargando(false);
            System.err.println(ex.getMessage());
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear un "
                                                        + "alumnoCAY\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            JOptionPane.showMessageDialog(null, "Se ha generado un error al tratar de almacenar el alumno en la asignatura correspondiente\n"+ex, 
                                                "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public List<DtoAlumnoCAY> read(DtoAlumnoCAY t) throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(readQuery)){
            pst.setInt(1, t.getCurso().getID());
            pst.setInt(2, t.getAsignatura().getID());
            pst.setInt(3, t.getYear_Escolar().getID());
           
            rs = pst.executeQuery();
            if(rs.next()){
                List<DtoAlumnoCAY> respuesta = new ArrayList<>();
                do {                    
                    respuesta.add(new DtoAlumnoCAY(new DtoAlumno(rs.getInt(1), rs.getString(3), 
                                  rs.getString(4), rs.getByte(2)), null, null, null, rs.getDouble(5), 
                                  rs.getDouble(6), ""));
                } while (rs.next());
                return respuesta;
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer los "
                                                        + "alumnoCAY\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar "
                                                        + "el rs del metodo read de la clase DAOalumnoCAY\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
        return (null);
    }
    
    @Override
    public void update(DtoAlumnoCAY t) throws DaoException {
        String updateQuery = null;
        switch(t.getEdicion()){
            case "asistencia":  updateQuery = "UPDATE alumno_cay SET porsentaje_Asistencia = ? "
                                + "WHERE id_alumno = ? AND id_curso=? AND id_asignatura=? AND "
                                + "id_year_escolar=?";  break;
                
            case "promedio":    updateQuery = "UPDATE alumno_cay SET promedio = ? "
                                + "WHERE id_alumno = ? AND id_curso=? AND id_asignatura=? AND "
                                + "id_year_escolar=?";  break;
        }
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(updateQuery)) {
            if("asistencia".equals(t.getEdicion()))    pst.setDouble(1, t.getPorcentaje_asistencia());
            else    pst.setDouble(1, t.getPromedio());
            pst.setInt(2, t.getAlumno().getID());
            pst.setInt(3, t.getCurso().getID());
            pst.setInt(4, t.getAsignatura().getID());
            pst.setInt(5, t.getYear_Escolar().getID());
            pst.executeUpdate();
        }catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al actualizar un "
                                                        + "alumnoCAY\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }

    @Override
    public void delete(DtoAlumnoCAY t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(DtoAlumnoCAY t) throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect();PreparedStatement pst = cn.prepareStatement(searchQuery)){
            pst.setInt(1, t.getAlumno().getID());
            pst.setInt(2, t.getCurso().getID());
            pst.setInt(3, t.getAsignatura().getID());
            pst.setInt(4, t.getYear_Escolar().getID());
            
            rs = pst.executeQuery();
            if(rs.next()){
                JOptionPane.showMessageDialog(null, "Este alumno ya se encuentra en el almacen, por lo tanto "
                                              + "no se agregará nuevamente.");
            }else   create(t);
        } catch (SQLException ex) {
            Start.setCargando(false);
            System.err.println(ex.getMessage());
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar un "
                                                        + "alumnoCAY\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo search de la clase alumnoCAY\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
    }
}
