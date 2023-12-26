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
import models.dtos.DtoIndicadorLogro;
import models.dtos.DtosTemas;
import views.RegistroError;

public class DaoTema extends Conector implements GenericMethodSQL<DtosTemas>{

    public DaoTema() {
        dtos = SingletonDtos.getInstance();
        interfaces = Singleton_Interfaces.getInstance();
        Start.setConteo(Start.getConteo()+0.1f);
    }

    private final String createQuery = "INSERT INTO temas(tema) VALUES(?)";
    
    private final String readQuery = "SELECT * FROM temas";
    
    private final String updateQuery = "UPDATE temas SET tema=? where id=?";
    
    private final String searchQuery = "SELECT id FROM temas WHERE tema = ?";
    
    private final SingletonDtos dtos;
    private final Singleton_Interfaces interfaces;
    
    @Override
    public void create(DtosTemas t) throws DaoException {
        byte error = 0;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery);) {
            pst.setString(1, t.getTema());
            pst.execute();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear un "
                                                        + "tema\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
        if(error == 0){
            dtos.tema.setID(lastID("temas"));
        }
    }

    @Override
    public List<DtosTemas> read(DtosTemas t) throws DaoException {
        List<DtosTemas> respuesta = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(readQuery);
                ResultSet rs = pst.executeQuery();){
            if(rs.next()){
                respuesta = new ArrayList<>();
                do {                    
                    respuesta.add(new DtosTemas(rs.getInt(1), rs.getString(2)));
                } while (rs.next());
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer los "
                                                        + "temas\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
        return respuesta;
    }

    @Override
    public void update(DtosTemas t) throws DaoException {
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(updateQuery)) {
            pst.setString(1, t.getTema());
            pst.setInt(2, t.getID());
            pst.executeUpdate();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al actualizar un "
                                                        + "tema\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }

    @Override
    public void delete(DtosTemas t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(DtosTemas t) throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(searchQuery);) {
            pst.setString(1, t.getTema());
            
            rs = pst.executeQuery();
            if(rs.next())   dtos.tema.setID(rs.getInt(1));
            else    create(t);
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar un "
                                                        + "tema\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo search de la clase DaoTema\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
    }
    
}
