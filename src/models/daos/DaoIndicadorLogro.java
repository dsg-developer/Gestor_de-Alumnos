package models.daos;

import controllers.Controller_Indicadores;
import controllers.singletons.SingletonDtos;
import controllers.singletons.Singleton_Interfaces;
import java.util.List;
import models.Conector;
import models.DaoException;
import models.GenericMethodSQL;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.*;
import java.util.ArrayList;
import main.Start;
import models.dtos.DtoIndicador;
import models.dtos.DtoAlumno;
import models.dtos.Dto_Competencia;
import models.dtos.DtoIndicadorLogro;
import models.dtos.Dto_Unidad;
import models.dtos.DtosTemas;
import views.IndicadoresLogro;
import views.RegistroError;

/**
 * @author Work-Game
 */

public class DaoIndicadorLogro extends Conector implements GenericMethodSQL<DtoIndicadorLogro>{
    private final Singleton_Interfaces interfaces = Singleton_Interfaces.getInstance();
    private final SingletonDtos dtos = SingletonDtos.getInstance();
    
    private final String createQuery = "insert into indicador_logro(id_asignatura, id_competencia, id_curso,"
                                     + "id_unidad, id_indicador, id_tema, id_year_escolar, periodo)"
                                     + " values(?,?,?,?,?,?,?,?)";
    
    private  String readQuery = "select distinct indicador_logro.id, indicador_logro.id_tema, "
                                + "indicador_logro.id_competencia, indicador_logro.id_unidad,"
                                + "indicador_logro.id_indicador, "
                                + "unidades.unidad, "
                                + "competencias.competencia, "
                                + "indicadores.indicador, "
                                + "temas.tema, indicador_logro.periodo from indicador_logro "
                                + "INNER JOIN unidades ON indicador_logro.id_unidad = unidades.id "
                                + "INNER JOIN competencias ON indicador_logro.id_competencia=competencias.id "
                                + "INNER JOIN indicadores ON indicador_logro.id_indicador = indicadores.id "
                                + "INNER JOIN temas ON indicador_logro.id_tema = temas.id "
                                + "INNER JOIN alumno_cay ON indicador_logro.id_asignatura = "
                                + "alumno_cay.id_asignatura "
                                + "WHERE indicador_logro.periodo=? AND alumno_cay.id_curso=? AND "
                                + "alumno_cay.id_asignatura=? AND alumno_cay.id_year_escolar=?";
    
    private final String updateQuery = "UPDATE indicador_logro SET tema=? WHERE id=?";
        
    public DaoIndicadorLogro() {
        Start.setConteo(Start.getConteo()+0.1f);
    }

    @Override
    public void create(DtoIndicadorLogro t) throws DaoException {
        byte error = 0;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery)){
            pst.setInt(1, t.getAsignatura().getID());
            pst.setInt(2, t.getCompetencia().getID());
            pst.setInt(3, t.getCurso().getID());
            pst.setInt(4, t.getUnidad().getID());
            pst.setInt(5, t.getIndicador().getID());
            pst.setInt(6, t.getTema().getID());
            pst.setInt(7, t.getYear_escolar().getID());
            pst.setInt(8, t.getPeriodo());
            pst.execute();               
        } catch (SQLException ex) {
            error++;
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear un "
                                                        + "indicador de logro\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
        if(error == 0){
            dtos.indicador_Logro.setID(lastID("indicador_logro"));
        }
    }

    @Override
    public List<DtoIndicadorLogro> read(DtoIndicadorLogro t) throws DaoException {
        ResultSet rs = null;
        List <DtoIndicadorLogro> respuesta = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(readQuery)){
            pst.setInt(1, Controller_Indicadores.periodo);
            pst.setInt(2, dtos.curso.getID());
            pst.setInt(3, dtos.asignatura.getID());
            pst.setInt(4, dtos.yearEscolar.getID());
            
            rs = pst.executeQuery();
            if (rs.next()) {
                respuesta = new ArrayList<>();
                do {    
                    respuesta.add(new DtoIndicadorLogro(rs.getInt(1), dtos.asignatura,
                                  new Dto_Competencia(rs.getInt(3), rs.getString(7)), dtos.curso,
                                  new DtoIndicador(rs.getInt(5), rs.getString(8)), new DtosTemas(rs.getInt(2),
                                  rs.getString(9)), new Dto_Unidad(rs.getInt(4),rs.getString(6)), 
                                  dtos.yearEscolar, rs.getByte(10)));
                } while (rs.next());
            } 
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer los "
                                                        + "indicadores de logro\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo read de la clase "
                                                        + "DaoIndicadoresLogro\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
            return respuesta;
    }
    
    @Override
    public void update(DtoIndicadorLogro t) throws DaoException {
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(updateQuery);){
           pst.setString(1, t.getIndicador().getIndicador());
           pst.setInt(2, t.getID());
           pst.executeUpdate();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al actualizar un "
                                                        + "indicador de logro\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }

    @Override
    public void delete(DtoIndicadorLogro t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(DtoIndicadorLogro t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
