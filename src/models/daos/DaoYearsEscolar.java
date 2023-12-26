package models.daos;

import controllers.singletons.Singleton_Class;
import controllers.singletons.Singleton_Interfaces;
import java.util.List;
import main.Start;
import java.sql.*;
import models.Conector;
import models.DaoException;
import models.GenericMethodSQL;
import models.GenericObject;
import java.util.logging.*;
import javax.swing.JOptionPane;
import models.dtos.DtoYearEscolar;
import views.RegistroError;

/**
 * @author Work
 */

public class DaoYearsEscolar extends Conector implements GenericMethodSQL<GenericObject>{
    private final String createQuery = "INSERT INTO years_escolar(year_escolar) values(?)";
    private String searchQuery = "SELECT * from years_escolar WHERE year_escolar = ?";
    private Singleton_Class clases = Singleton_Class.getInstance();
    private Singleton_Interfaces interfaces;
    
    public DaoYearsEscolar() { 
        interfaces = Singleton_Interfaces.getInstance();
        Start.setConteo(Start.getConteo()+0.1f);
    }

    @Override
    public void create(GenericObject t) throws DaoException {
        byte error = 0;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery)){
            pst.setString(1, String.valueOf(t.getDatos().get(2)));
            pst.execute();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear un "
                                                        + "año escolar\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            error++;
            JOptionPane.showMessageDialog(null, "Se ha generado un error al tratar de almacenar el año escolar\n"+ex,
                                                "Error",JOptionPane.WARNING_MESSAGE);
        }
        if(error == 0)
            clases.objecto.getDatos().add(lastID("years_escolar"));
    }

    @Override
    public List<GenericObject> read(GenericObject t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(GenericObject t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(GenericObject t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(GenericObject t) throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(searchQuery)){
            pst.setString(1, String.valueOf(t.getDatos().get(2)));
            
            rs = pst.executeQuery();
            if (rs.next()) {
                clases.objecto.getDatos().add(rs.getInt("id"));
            }else{
                create(t);
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar un "
                                                        + "año escolar\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo search de la clase DaoYearEscolar\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
    }
    
}
