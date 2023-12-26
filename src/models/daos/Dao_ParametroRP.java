package models.daos;

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
import models.dtos.DtoParametroRP;
import views.RegistroError;

/**
 * @author Work-Game
 */

public class Dao_ParametroRP extends Conector implements GenericMethodSQL<DtoParametroRP>{
    private final String createQuery = "INSERT INTO parametros_rp(parametro) VALUES(?)";
    private final String readQuery = "SELECT * FROM parametros_rp";
    private final String searchQuery = "SELECT * FROM parametros_rp WHERE parametro=?";
    
    private final SingletonDtos dtos = SingletonDtos.getInstance();
    private final Singleton_Interfaces interfaces;

    public Dao_ParametroRP() {
        interfaces = Singleton_Interfaces.getInstance();
        Start.setConteo(Start.getConteo()+0.1f);
    }
    
    @Override
    public void create(DtoParametroRP t) throws DaoException {
        byte error = 0;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery);){
            pst.setString(1, t.getParametro());
            pst.execute();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear un "
                                                        + "parametro de RP\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            error++;
        }
        if(error == 0)
            dtos.prametroRP.setID(lastID("parametros_rp"));
    }

    @Override
    public List<DtoParametroRP> read(DtoParametroRP t) throws DaoException {
        List<DtoParametroRP> respuesta = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(readQuery);
                ResultSet rs = pst.executeQuery();){
            if(rs.next()){
                respuesta = new ArrayList<>();
                do {                    
                    respuesta.add(new DtoParametroRP(rs.getInt(1), rs.getString(2)));
                } while (rs.next());
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer los "
                                                        + "parametros de RP\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
        return respuesta;
    }

    @Override
    public void update(DtoParametroRP t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(DtoParametroRP t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(DtoParametroRP t) throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(searchQuery);){
            pst.setString(1, t.getParametro());
            
            rs = pst.executeQuery();
            if(rs.next()){
                dtos.prametroRP.setID(rs.getInt(1));
            }else{
                create(t);
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar un "
                                                        + "parametro de RP\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo search de la clase de parametro"
                                                        + "para elñ RP\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
    }
    
}
