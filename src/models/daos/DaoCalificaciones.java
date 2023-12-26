package models.daos;

import controllers.singletons.SingletonDtos;
import controllers.singletons.Singleton_Interfaces;
import java.util.List;
import java.util.logging.*;
import java.sql.*;
import main.Start;
import models.Conector;
import models.DaoException;
import models.GenericMethodSQL;
import models.dtos.Dto_Calificaciones;
import views.RegistroError;

/**
 * @author Work-Game
 */

public class DaoCalificaciones extends Conector implements GenericMethodSQL<Dto_Calificaciones>{
    private final String createQuery = "INSERT INTO calificaciones(calificacion) VALUES(?) ";
    
    private final String searchQuery = "SELECT id FROM calificaciones WHERE calificacion=? ";
    
    private final String notasQuery = "SELECT sum(calificaciones.calificacion)/count(indicador_logro.id), count(indicador_logro.id) "
                                    + "FROM calificacion_indicadores "
                                    + "INNER JOIN calificaciones on calificacion_indicadores.id_calificacion "
                                    + "= calificaciones.id "
                                    + "INNER JOIN indicador_logro on calificacion_indicadores."
                                    + "id_indicador_logro = indicador_logro.id "
                                    + "INNER JOIN alumno_cay on calificacion_indicadores.id_alumno = "
                                    + "alumno_cay.id_alumno "
                                    + "WHERE indicador_logro.periodo=? AND alumno_cay.id_curso=? AND "
                                    + "alumno_cay.id_asignatura=? AND alumno_cay.id_year_escolar=? "
                                    + "AND alumno_cay.id_alumno=? ";
    
    private final SingletonDtos dtos = SingletonDtos.getInstance();
    private final Singleton_Interfaces interfaces;

    public DaoCalificaciones() {
        this.interfaces = Singleton_Interfaces.getInstance();
        Start.setConteo(Start.getConteo()+0.1f);
    }
    
    @Override
    public void create(Dto_Calificaciones t) throws DaoException {
        byte error = 0;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery)){
            pst.setInt(1, t.getCalificacion());
            pst.execute();
        } catch (SQLException ex) {
            error++;
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear una "
                                                        + "calificación\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
        if(error == 0)    dtos.calificaciones.setID(lastID("calificaciones"));
    }

    @Override
    public List<Dto_Calificaciones> read(Dto_Calificaciones t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public byte notaFinal(byte periodo){
        byte respuesta = 0;
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(notasQuery);){
            pst.setInt(1, periodo);
            pst.setInt(2, dtos.curso.getID());
            pst.setInt(3, dtos.asignatura.getID());
            pst.setInt(4, dtos.yearEscolar.getID());
            pst.setInt(5, dtos.alumno.getID());
            /*System.err.println(periodo +" "+dtos.curso.getID()+" "+dtos.asignatura.getID()+" "+
                    dtos.yearEscolar.getID()+" "+dtos.alumno.getID());*/
            
            rs = pst.executeQuery();
            if(rs.next()){
                respuesta = rs.getByte(1);
            }
            
            
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al obtener la "
                                                        + "nota\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el es "
                                                        + "del metodo nota final de la calse "
                                                            + "DAOCalificaciones\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
        return respuesta;
    }
    
    @Override
    public void update(Dto_Calificaciones t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Dto_Calificaciones t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(Dto_Calificaciones t) throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(searchQuery);){
            pst.setInt(1, t.getCalificacion());
            
            rs = pst.executeQuery();
            if(rs.next()){
                dtos.calificaciones.setID(rs.getInt(1));
            }else{
                create(t);
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar una "
                                                        + "calificacion\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo search de la clase "
                                                        + "DAOCalificaciones\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
    }
    
}
