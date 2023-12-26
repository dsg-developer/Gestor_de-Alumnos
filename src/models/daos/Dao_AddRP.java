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
import models.dtos.DtoRegistroPersonal;
import views.RegistroError;

/**
 * @author Work-Game
 */

public class Dao_AddRP extends Conector implements GenericMethodSQL<DtoRegistroPersonal>{
    private final SingletonDtos dtos = SingletonDtos.getInstance();
    private final Singleton_Interfaces interfaces;
    
    private final String createQuery = "INSERT INTO registro_personal(id_alumno, id_indicador_logro, "
                                     + "id_fecha) VALUES(?,?,?) ";
    
    private final String searchQuery = "SELECT id FROM registro_personal WHERE id_alumno=? and "
                                     + "id_indicador_logro=? and id_fecha=? ";

    public Dao_AddRP() {
        this.interfaces = Singleton_Interfaces.getInstance();
        Start.setConteo(Start.getConteo()+0.1f);
    }
    
    
    
    @Override
    public void create(DtoRegistroPersonal t) throws DaoException {
        byte error = 0;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery);){
            pst.setInt(1, t.getAlumno().getID());
            pst.setInt(2, t.getIndicadorLogro().getID());
            pst.setInt(3, t.getFecha().getID());
            pst.execute();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear un "
                                                        + "RP\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            error++;
        }
        if(error == 0)
            dtos.registro_Personal.setID(lastID("registro_personal"));
    }

    @Override
    public List<DtoRegistroPersonal> read(DtoRegistroPersonal t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(DtoRegistroPersonal t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(DtoRegistroPersonal t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(DtoRegistroPersonal t) throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(searchQuery);){
            pst.setInt(1, t.getAlumno().getID());
            pst.setInt(2, t.getIndicadorLogro().getID());
            pst.setInt(3, t.getFecha().getID());
            
            rs = pst.executeQuery();
            if(rs.next()){
                dtos.registro_Personal.setID(rs.getInt(1));
            }else{
                create(t);
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar un "
                                                        + "RP\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo search de la clase DaoAddRP\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
    }
    
}
