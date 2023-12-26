package models.daos;

import controllers.Controller_Indicadores;
import controllers.singletons.SingletonDtos;
import controllers.singletons.Singleton_Interfaces;
import java.util.List;
import java.util.logging.*;
import java.sql.*;
import java.util.ArrayList;
import main.Start;
import models.Conector;
import models.DaoException;
import models.GenericMethodSQL;
import models.dtos.DtoAlumno;
import models.dtos.Dto_Calificaciones;
import models.dtos.DtoIndicadorLogro;
import models.dtos.Dto_calificaciones_indicador;
import views.RegistroError;

/**
 * @author Work-Game
 */

public class DaoCalificacionesIndicador extends Conector implements GenericMethodSQL<Dto_calificaciones_indicador>{
    private final String createQuery = "INSERT INTO calificacion_indicadores(id_alumno, id_calificacion, "
                                     + "id_indicador_logro) VALUES(?,?,?) ";
    
    private final String readQuery = "SELECT alumnos.id, alumnos.numero, alumnos.apellido, alumnos.nombre, "
                                    +"calificaciones.id, calificaciones.calificacion FROM "
                                    + "calificacion_indicadores "
                                    +"INNER JOIN alumnos on calificacion_indicadores.id_alumno = alumnos.id "
                                    +"INNER JOIN alumno_cay on calificacion_indicadores.id_alumno = "
                                    + "alumno_cay.id_alumno "
                                    +"INNER JOIN calificaciones on calificacion_indicadores.id_calificacion "
                                    + "= calificaciones.id "
                                    +"INNER JOIN indicador_logro on calificacion_indicadores."
                                    + "id_indicador_logro = indicador_logro.id "
                                    +"WHERE alumno_cay.id_curso=? AND alumno_cay.id_year_escolar=? "
                                    +"AND alumno_cay.id_asignatura=? AND indicador_logro.id=? AND "
                                    + "indicador_logro.periodo=? ";
    
    private final String updateQuery = "UPDATE calificacion_indicadores SET id_calificacion = ? "
                                     + "WHERE id_alumno = ? AND id_indicador_logro = ? ";
      
    private final String searchQuery = "select id_alumno from calificacion_indicadores "
                                     + "WHERE id_alumno = ? AND id_indicador_logro = ? ";
            
    private final SingletonDtos dtos = SingletonDtos.getInstance();
    private final Singleton_Interfaces interfaces;

    public DaoCalificacionesIndicador() {
        this.interfaces = Singleton_Interfaces.getInstance();
        Start.setConteo(Start.getConteo()+0.1f);
    }
            
    @Override
    public void create(Dto_calificaciones_indicador t) throws DaoException {
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery);){
            pst.setInt(1, t.getAlumno().getID());
            pst.setInt(2, 1);
            pst.setInt(3, t.getIndicadorLogro().getID());
            pst.execute();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear una "
                                                        + "calificacion para el indicador\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }

    @Override
    public List<Dto_calificaciones_indicador> read(Dto_calificaciones_indicador t) throws DaoException {
        List<Dto_calificaciones_indicador> respuesta = null;
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(readQuery);){
            pst.setInt(1, dtos.curso.getID());
            pst.setInt(2, dtos.yearEscolar.getID());
            pst.setInt(3, dtos.asignatura.getID());
            pst.setInt(4, dtos.indicador_Logro.getID());
            pst.setInt(5, Controller_Indicadores.periodo);
                        
            rs = pst.executeQuery();
            if(rs.next()){
                respuesta = new ArrayList<>();
                do {                    
                    respuesta.add(new Dto_calificaciones_indicador(new DtoAlumno(rs.getInt(1), 
                                  rs.getString(3), rs.getString(4), rs.getByte(2)),
                                  new Dto_Calificaciones(rs.getInt(5), rs.getByte(6)), new 
                                  DtoIndicadorLogro()));
                } while (rs.next());
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer las "
                                                        + "calificaiones de os indicadores\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)   rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo read de la clase "
                                                        + "DaoCalificacionesIndicador\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
        return respuesta;
    }

    @Override
    public void update(Dto_calificaciones_indicador t) throws DaoException {
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(updateQuery);){
            pst.setInt(1, t.getCalificacion().getID());
            pst.setInt(2, t.getAlumno().getID());
            pst.setInt(3, t.getIndicadorLogro().getID());
            pst.executeUpdate();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al actualizar una "
                                                        + "calificacion del indicador\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }

    @Override
    public void delete(Dto_calificaciones_indicador t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(Dto_calificaciones_indicador t) throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(searchQuery);){
            pst.setInt(1, t.getAlumno().getID());
            pst.setInt(2, t.getIndicadorLogro().getID());
            
            rs = pst.executeQuery();
            if(rs.next()){
                
            }else{
                create(t);
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar una "
                                                        + "calificacion del indicador\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo search de la clase"
                                                        + "DaoCalificacionesIndicador\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
    }
    
}
