/* Para entender el orden de los datos, ver consulta sql readQuery.
*/

package models.daos;

import controllers.singletons.Singleton_Interfaces;
import java.util.List;
import main.Start;
import java.sql.*;
import java.util.ArrayList;
import models.Conector;
import models.DaoException;
import models.GenericMethodSQL;
import models.GenericObject;
import java.util.logging.*;
import javax.swing.JOptionPane;
import models.dtos.DtoAlumnoCAY;
import views.RegistroError;

/**
 * @author Work
 */

public class Dao_CAY extends Conector implements GenericMethodSQL<GenericObject>{
    private final String createQuery = "INSERT INTO cay(id_curso, id_asignatura, id_year) values(?,?,?)";
    private final String searchQuery = "SELECT * FROM cay WHERE id_curso = ? and id_asignatura = ? and id_year = ?";
    private final String readQuery = "SELECT cursos.id, cursos.curso, "
                                   + "asignaturas.id, asignaturas.asignatura, "
                                   + "years_escolar.id, years_escolar.year_escolar FROM cay "
                                   + "INNER JOIN cursos ON cay.id_curso = cursos.id "
                                   + "INNER JOIN asignaturas ON cay.id_asignatura = asignaturas.id "
                                   + "INNER JOIN years_escolar ON cay.id_year = years_escolar.id";
    
    private final Singleton_Interfaces interfaces;
    public Dao_CAY() { 
        interfaces = Singleton_Interfaces.getInstance();
        Start.setConteo(Start.getConteo()+0.1f); 
    }

    @Override
    public void create(GenericObject t) throws DaoException {
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery)){
            pst.setInt(1, (Integer) t.getDatos().get(3));
            pst.setInt(2, (Integer) t.getDatos().get(4));
            pst.setInt(3, (Integer) t.getDatos().get(5));
            pst.execute();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear un "
                                                        + "CAY\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            JOptionPane.showMessageDialog(null, "Se ha generado un error al tratar de almacenar el CAY\n"+ex,
                                                "Error",JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public List<GenericObject> read(GenericObject t) throws DaoException {
        return (null);
    }
    
    public List<Object> read2() throws DaoException {
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(readQuery); ResultSet rs = pst.executeQuery();){
            if(rs.next()){
                List<Object> respuesta = new ArrayList<>();
                do{
                    respuesta.add(rs.getObject(1)); respuesta.add(rs.getObject(2));
                    respuesta.add(rs.getObject(3)); respuesta.add(rs.getObject(4));
                    respuesta.add(rs.getObject(5)); respuesta.add(rs.getObject(6));
                }while(rs.next());
                return respuesta;
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer los "
                                                        + "CAY\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }return (null);
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
            pst.setInt(1, (Integer) t.getDatos().get(3));
            pst.setInt(2, (Integer) t.getDatos().get(4));
            pst.setInt(3, (Integer) t.getDatos().get(5));
            
            rs = pst.executeQuery();
            if(rs.next()){
                JOptionPane.showMessageDialog(null, "El curso "+t.getDatos().get(0)+" con asignatura "+t.getDatos().get(1)+
                        " del año "+t.getDatos().get(2)+"\nya se encuentra en el almacen, o revice detalladamente lo que intenta agregar");
            }else{
                create(t);
            }
        } catch (Exception ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar un "
                                                        + "CAY\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo search de la clase cay\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            }
        }
    }
    
    
}
