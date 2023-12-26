package models.daos;

import controllers.singletons.Singleton_Class;
import controllers.singletons.Singleton_Interfaces;
import java.util.List;
import main.Start;
import models.Conector;
import models.DaoException;
import models.GenericMethodSQL;
import models.GenericObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import javax.swing.JOptionPane;
import models.dtos.DtoAsignatura;
import views.RegistroError;

/**
 * @author Work
 */

public class DaoAsignatura extends Conector implements GenericMethodSQL<GenericObject>{
    private final String createQuery = "INSERT INTO asignaturas(asignatura) values(?)";
    
    private final String readQuery = "SELECT * FROM asignaturas ";
    
    private final String updateQuery = "UPDATE asignaturas SET asignatura=? "
                                     + "WHERE id=? ";
        
    private final String searchQuery = "SELECT * from asignaturas WHERE asignatura = ?";
    
    private Singleton_Class clases = Singleton_Class.getInstance();
    private Singleton_Interfaces interfaces;
    
    public DaoAsignatura() { 
        interfaces = Singleton_Interfaces.getInstance();
        Start.setConteo(Start.getConteo()+0.1f);
    }

    @Override
    public void create(GenericObject t) throws DaoException {
        byte error = 0;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery)){
            pst.setString(1, String.valueOf(t.getDatos().get(1)));
            pst.execute();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear una "
                                                        + "asignatura\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            error++;
            JOptionPane.showMessageDialog(null, "Se ha generado un error al tratar de almacenar la asignatura\n"+ex, 
                                                "Error", JOptionPane.WARNING_MESSAGE);
        }
        if(error == 0)
            clases.objecto.getDatos().add(lastID("asignaturas"));
    }

    @Override
    public List<GenericObject> read(GenericObject t) throws DaoException {
        return (null);
    }

    public List<DtoAsignatura> read1(){
        List<DtoAsignatura> respuesta = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(readQuery); 
             ResultSet rs = pst.executeQuery();){
            if (rs.next()) {
                respuesta = new ArrayList<>();
                do {
                    respuesta.add(new DtoAsignatura(rs.getInt(1), rs.getString(2)));
                } while (rs.next());
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer las "
                                                        + "asignaturas\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
        return respuesta;
    }
    
    @Override
    public void update(GenericObject t) throws DaoException {
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(updateQuery);){
            pst.setString(1, t.getDatos().get(1).toString());
            pst.setInt(2, Integer.parseInt(t.getDatos().get(0).toString()));
            pst.executeUpdate();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al actualizar una "
                                                        + "asignatura\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }

    @Override
    public void delete(GenericObject t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(GenericObject t) throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(searchQuery)){
            pst.setString(1, String.valueOf(t.getDatos().get(1)));
            
            rs = pst.executeQuery();
            if(rs.next()){
                clases.objecto.getDatos().add(rs.getInt("id"));
            }else{
                create(t);
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar una "
                                                        + "asignatura\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el rs "
                                                        + "del metodo search de la clase DAOAsignatura\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
    }
    
}
