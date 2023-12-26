package models.daos;

import controllers.singletons.Singleton_Interfaces;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import models.Conector;
import models.DaoException;
import models.GenericMethodSQL;
import models.dtos.Dto_Unidad;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import main.Start;
import views.RegistroError;

/**
 * @author Work-Game
 */

public class Dao_Unidad extends Conector implements GenericMethodSQL<Dto_Unidad>{
    private final String createQuery = "INSERT INTO unidades(unidad) VALUES(?) ";
    private final String readQuery = "SELECT * FROM unidades ";
    private final String updateQuery = "UPDATE unidades SET unidad=? WHERE id=?";
    private final String searchQuery = "SELECT * FROM unidades WHERE unidad=?";

    private final Singleton_Interfaces interfaces;
    
    public Dao_Unidad() {
        interfaces = Singleton_Interfaces.getInstance();
        Start.setConteo(Start.getConteo()+0.1f);
    }

    @Override
    public void create(Dto_Unidad t) throws DaoException {
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery)){
            pst.setString(1, t.getUnidad());
            pst.execute();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear una "
                                                        + "unidad\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }

    @Override
    public List<Dto_Unidad> read(Dto_Unidad t) throws DaoException {
        List<Dto_Unidad> respuesta = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(readQuery); 
             ResultSet rs = pst.executeQuery();){
            if(rs.next()){
                respuesta = new ArrayList<>();
                do{
                    respuesta.add(new Dto_Unidad(rs.getInt(1), rs.getString(2)));
                }while(rs.next());
            }
        } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer las "
                                                        + "unidades\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
        return respuesta;
    }

    @Override
    public void update(Dto_Unidad t) throws DaoException {
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(updateQuery);){
            pst.setString(1, t.getUnidad());
            pst.setInt(2, t.getID());
            pst.executeUpdate();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al actualizar una "
                                                        + "unidad\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }

    @Override
    public void delete(Dto_Unidad t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(Dto_Unidad t) throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(searchQuery);){
            pst.setString(1, t.getUnidad());
            
            rs = pst.executeQuery();
            if(rs.next()){
                JOptionPane.showMessageDialog(null, "Se ha encontrado una unidad identica a esta en el almacen", 
                                              "Coincidencia de datos", JOptionPane.INFORMATION_MESSAGE);
            }else   create(t);
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar una "
                                                        + "unidad\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo search de la clase unidad\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
    }
    
}
