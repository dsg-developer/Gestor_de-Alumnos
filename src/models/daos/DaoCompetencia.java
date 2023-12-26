package models.daos;

import controllers.singletons.SingletonDtos;
import controllers.singletons.Singleton_Interfaces;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import main.Start;
import models.Conector;
import models.DaoException;
import models.GenericMethodSQL;
import models.dtos.Dto_Competencia;
import views.RegistroError;

/**
 * @author Work-Game
 */

public class DaoCompetencia extends Conector implements GenericMethodSQL<Dto_Competencia>{
    public DaoCompetencia() {
        dtos = SingletonDtos.getInstance();
        interfaces = Singleton_Interfaces.getInstance();
        Start.setConteo(Start.getConteo()+0.1f);
    }
    
    private final String createQuery= "INSERT INTO competencias(competencia) VALUES(?) ";
    private final String readQuery = "SELECT * FROM competencias ";
    private final String updateQuery = "UPDATE competencias SET competencia=? WHERE id=?";
    private final String searchQuery = "SELECT id FROM competencias WHERE competencia=? ";

    private final SingletonDtos dtos;
    private final Singleton_Interfaces interfaces;

    @Override
    public void create(Dto_Competencia t) throws DaoException {
        byte error = 0;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery);){
            pst.setString(1, t.getCompetencia());
            pst.execute();
        } catch (SQLException ex) {
            error++;
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear una "
                                                        + "competencia\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
        if(error == 0){
            dtos.competencia.setID(lastID("competencias"));
        }
    }

    @Override
    public List<Dto_Competencia> read(Dto_Competencia t) throws DaoException {
        List<Dto_Competencia> respuesta = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(readQuery);
                ResultSet rs = pst.executeQuery()){
            if(rs.next()){
                respuesta = new ArrayList<>();
                do {                    
                    respuesta.add(new Dto_Competencia(rs.getInt(1), rs.getString(2)));
                } while (rs.next());
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer las "
                                                        + "competencias\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }return respuesta;
    }

    @Override
    public void update(Dto_Competencia t) throws DaoException {
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(updateQuery)){
            pst.setString(1, t.getCompetencia());
            pst.setInt(2, t.getID());
            pst.executeUpdate();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al actualizar una "
                                                        + "competencia\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }
    }

    @Override
    public void delete(Dto_Competencia t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(Dto_Competencia t) throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(searchQuery);){
            pst.setString(1, t.getCompetencia());
            
            rs = pst.executeQuery();
            if(rs.next()){
                /*JOptionPane.showMessageDialog(null, "Se ha encontrado una competencia identica a esta en el"
                                                  + " almacen, compruebe que no hay similitud", 
                                              "Coincidencia de datos", JOptionPane.INFORMATION_MESSAGE);*/
            }else{
                create(t);
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar una "
                                                        + "competencia\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)  rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo search de la clase DaoCompetencia\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
    }
}
