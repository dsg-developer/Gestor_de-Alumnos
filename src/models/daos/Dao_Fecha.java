package models.daos;

import controllers.singletons.Singleton_Class;
import controllers.singletons.SingletonDtos;
import controllers.singletons.Singleton_Interfaces;
import java.time.LocalDate;
import java.util.List;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import main.Start;
import models.Conector;
import models.DaoException;
import models.GenericMethodSQL;
import models.GenericObject;
import views.RegistroError;

/**
 * @author Work
 */

public class Dao_Fecha extends Conector{
    Singleton_Interfaces interfaces = Singleton_Interfaces.getInstance();
    
    private final String createQuery = "INSERT INTO fechas(fecha) values(?)";
    private final String readQuery = "SELECT * FROM fechas ";
    private final String searchQuery = "SELECT id FROM fechas WHERE fecha=?";

    private final SingletonDtos dtos;
    
    public Dao_Fecha() {
        dtos = SingletonDtos.getInstance();
        Start.setConteo(Start.getConteo()+0.1f);
    }

    public void create() throws DaoException {
        byte error = 0;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery)){
            pst.setString(1, interfaces.principal.getFecha((byte)0));
            pst.execute();
        } catch (SQLException ex) {
            error++;
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear la "
                                                        + "fecha\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            JOptionPane.showMessageDialog(null, "Se ha generado un error al tratar de almacenar la fecha actual\n"+ex,
                                                "Error",JOptionPane.WARNING_MESSAGE);
        }
        if(error == 0)
            dtos.fecha.setID(lastID("fechas"));
    }

    public List<Object> read() throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void update(GenericObject t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void delete(GenericObject t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void search() throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(searchQuery);){
            pst.setString(1, interfaces.principal.getFecha((byte)0));
            
            rs = pst.executeQuery();
            if(rs.next()){
                dtos.fillFecha(rs.getInt(1), null);
            }else   create();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar una "
                                                        + "fecha\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo search de la clase fecha\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
    }
    
    
}
