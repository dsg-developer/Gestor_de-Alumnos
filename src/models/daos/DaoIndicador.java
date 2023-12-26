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
import models.dtos.DtoIndicador;
import views.RegistroError;

public class DaoIndicador extends Conector implements GenericMethodSQL<DtoIndicador>{

    public DaoIndicador() {
        dtos = SingletonDtos.getInstance();
        interfaces = Singleton_Interfaces.getInstance();
        Start.setConteo(Start.getConteo()+0.1f);
    }

    private final String createQuery = "INSERT INTO indicadores(indicador) VALUES(?)";
    
    private final String readQuery = "SELECT * FROM indicadores";
    
    private final String updateQuery = "UPDATE indicadores SET indicador = ? WHERE id = ?";
    
    private final String searchQuery = "SELECT id FROM indicadores WHERE indicador = ?";
    
    private final SingletonDtos dtos;
    private final Singleton_Interfaces interfaces;
    
    @Override
    public void create(DtoIndicador t) throws DaoException {
        byte error = 0;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery);){
            pst.setString(1, t.getIndicador());
            pst.execute();
        } catch (SQLException ex) {
            error++;
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear un "
                                                        + "indicador\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
        if(error == 0)  dtos.indicador.setID(lastID("indicadores"));
    }

    @Override
    public List<DtoIndicador> read(DtoIndicador t) throws DaoException {
        List<DtoIndicador> respuesta = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(readQuery); 
             ResultSet rs = pst.executeQuery();) {
            if(rs.next()){
                respuesta = new ArrayList<>();
                do {                    
                    respuesta.add(new DtoIndicador(rs.getInt(1), rs.getString(2)));
                } while (rs.next());
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer los "
                                                        + "indicadore\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
        return respuesta;
    }

    @Override
    public void update(DtoIndicador t) throws DaoException {
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(updateQuery);) {
            pst.setString(1, t.getIndicador());
            pst.setInt(2, t.getID());
            pst.executeUpdate();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al actualizar un "
                                                        + "indicador\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }

    @Override
    public void delete(DtoIndicador t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(DtoIndicador t) throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(searchQuery);) {
            pst.setString(1, t.getIndicador());
            
            rs = pst.executeQuery();
            if(rs.next()){
                dtos.indicador.setID(rs.getInt(1));
            }else   create(t);
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar un "
                                                        + "indicador\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if (rs != null)   rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo search de la cladr\n DaoIndicador");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
    }
    
}
