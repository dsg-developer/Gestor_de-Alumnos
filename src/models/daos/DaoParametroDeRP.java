
package models.daos;

import controllers.singletons.SingletonDtos;
import controllers.singletons.Singleton_Interfaces;
import java.util.List;
import java.util.logging.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import main.Start;
import models.Conector;
import models.DaoException;
import models.GenericMethodSQL;
import models.dtos.DtoAlumno;
import models.dtos.Dto_Calificaciones;
import models.dtos.Dto_Fecha;
import models.dtos.DtoParametrosDeRP;
import models.dtos.DtoParametroRP;
import models.dtos.DtoRegistroPersonal;
import models.dtos.DtosTemas;
import views.RegistroError;

/**
 * @author Work-Game
 */

public class DaoParametroDeRP extends Conector implements GenericMethodSQL<DtoParametrosDeRP>{
    private String fecha2;
    
    private final String createQuery = "INSERT INTO parametro_de_rp(id_calificacion, id_parametro_rp, "
                                     + "id_rp, id_tema) VALUES(?,?,?,?) ";
    
    private final String readQuery = "SELECT alumnos.id, alumnos.numero, alumnos.apellido, alumnos.nombre, "
                                   + "parametros_rp.id, parametros_rp.parametro, "
                                   + "calificaciones.id, calificaciones.calificacion, "
                                   + "registro_personal.id FROM parametro_de_rp "
                                   + "INNER JOIN registro_personal ON parametro_de_rp.id_rp = "
                                   + "registro_personal.id "
                                   + "INNER JOIN alumnos ON registro_personal.id_alumno = alumnos.id "
                                   + "INNER JOIN alumno_cay ON registro_personal.id_alumno = "
                                   + "alumno_cay.id_alumno "
                                   + "INNER JOIN parametros_rp ON parametro_de_rp.id_parametro_rp = "
                                   + "parametros_rp.id "
                                   + "INNER JOIN calificaciones ON parametro_de_rp.id_calificacion = "
                                   + "calificaciones.id "
                                   + "INNER JOIN indicadores ON indicadores.id = indicadores.id "
                                   + "WHERE alumno_cay.id_curso=? AND alumno_cay.id_year_escolar=? AND "
                                   + "alumno_cay.id_asignatura=? "
                                   + "AND registro_personal.id_fecha=? AND parametro_de_rp.id_tema=? "
                                   + "AND indicadores.id =?"
                                   + "ORDER BY alumnos.numero ";
    
     private final String readQuery2 = "SELECT registro_personal.id_fecha, fechas.fecha FROM parametro_de_rp "
                                     + "INNER JOIN registro_personal ON parametro_de_rp.id_rp = "
                                     + "registro_personal.id "
                                     + "INNER JOIN fechas ON registro_personal.id_fecha = fechas.id "
                                     + "INNER JOIN alumno_cay ON registro_personal.id_alumno = "
                                     + "alumno_cay.id_alumno "
                                     + "WHERE alumno_cay.id_curso=? AND alumno_cay.id_year_escolar=? AND "
                                     + "alumno_cay.id_asignatura=? ";
     
     private String sumarQuery = "SELECT SUM(calificaciones.calificacion), fechas.fecha FROM parametro_de_rp "
                               + "INNER JOIN calificaciones ON parametro_de_rp.id_calificacion = "
                               + "calificaciones.id "
                               + "INNER JOIN registro_personal ON parametro_de_rp.id_rp = "
                               + "registro_personal.id "
                               + "INNER JOIN alumno_cay ON registro_personal.id_alumno = "
                               + "alumno_cay.id_alumno "
                               + "INNER JOIN indicadores ON indicadores.id = indicadores.id "
                               + "INNER JOIN fechas ON registro_personal.id_fecha = fechas.id "
                               + "WHERE registro_personal.id_alumno = ? AND indicadores.id=? "
                               + "AND alumno_cay.id_curso=? AND alumno_cay.id_asignatura=? "
                               + "AND alumno_cay.id_year_escolar = ?"
                               + "AND fechas.fecha BETWEEN '"+fecha()+"' AND '"+fecha2+"' ";
    
     
     private final String seachquery = "select * from parametro_de_rp WHERE id_calificacion=? AND "
                                     + "id_parametro_rp=? AND id_rp=? AND id_tema=?";

    private final SingletonDtos dtos = SingletonDtos.getInstance();
    private final Singleton_Interfaces interfaces = Singleton_Interfaces.getInstance();

    public DaoParametroDeRP() {Start.setConteo(Start.getConteo()+0.1f);}
       
    @Override
    public void create(DtoParametrosDeRP t) throws DaoException {
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(createQuery);){
            pst.setInt(1, 1);
            pst.setInt(2, t.getPrametroRP().getID());
            pst.setInt(3, t.getRegistro_Personal().getID());
            pst.setInt(4, t.getRegistro_Personal().getIndicadorLogro().getTema().getID());
            pst.execute();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al crear un "
                                                        + "parametro de rp\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            Logger.getLogger(DaoParametroDeRP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<DtoParametrosDeRP> read(DtoParametrosDeRP t) throws DaoException {
        ResultSet rs = null;
        List<DtoParametrosDeRP> respuesta = null;
        try (Connection cn = conect(); PreparedStatement pst =cn.prepareStatement(readQuery);){
            pst.setInt(1, dtos.curso.getID());
            pst.setInt(2, dtos.yearEscolar.getID());
            pst.setInt(3, dtos.asignatura.getID());
            pst.setInt(4, dtos.fecha.getID());
            pst.setInt(5, dtos.tema.getID());
            pst.setInt(6, dtos.indicador_Logro.getIndicador().getID());
            
            rs = pst.executeQuery();
            if(rs.next()){
                respuesta = new ArrayList<>();
                do{
                    respuesta.add(new DtoParametrosDeRP(new Dto_Calificaciones(rs.getInt(7), rs.getByte(8)),
                                new DtoParametroRP(rs.getInt(5), rs.getString(6)),
                                new DtoRegistroPersonal(rs.getInt(9), new DtoAlumno(rs.getInt(1),
                                rs.getString(3), rs.getString(4), rs.getByte(2)), null, null), null ));
                }while(rs.next());
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer la lista "
                                                        + "de parametro de rp\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            Logger.getLogger(DaoParametroDeRP.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo read de la clase "
                                                        + "DaoParametroDeRP\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
                Logger.getLogger(DaoParametroDeRP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return respuesta;
    }

    public List<DtoParametrosDeRP> readFechas(){
        List<DtoParametrosDeRP> respuesta = null;
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(readQuery2); ){
            pst.setInt(1, dtos.curso.getID());
            pst.setInt(2, dtos.yearEscolar.getID());
            pst.setInt(3, dtos.asignatura.getID());
            
            rs = pst.executeQuery();
            if(rs.next()){
                respuesta = new ArrayList<>();
                do {                    
                    respuesta.add(new DtoParametrosDeRP(null, null, new DtoRegistroPersonal(0, null, null,
                    new Dto_Fecha(rs.getInt(1), rs.getString(2))), null));
                } while(rs.next());
            }
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al leer la fecha "
                                                        + "de parametro de rp\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            Logger.getLogger(Dao_Alumno.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo radFecha de la clase "
                                                        + "DaoParametroDeRP\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
                Logger.getLogger(DaoParametroDeRP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return respuesta;
    }
    
    public byte sumarRPs(){
        byte respuesta = 0;
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(sumarQuery);){
            pst.setInt(1, dtos.alumno.getID());
            pst.setInt(2, dtos.indicador_Logro.getIndicador().getID());
            pst.setInt(3, dtos.curso.getID());
            pst.setInt(4, dtos.asignatura.getID());
            pst.setInt(5, dtos.yearEscolar.getID());
            
            rs = pst.executeQuery();
            if(rs.next()) respuesta = rs.getByte(1);
            else   respuesta = 0;
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al sumar RPS de "
                                                        + "parametro de rp\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            Logger.getLogger(DaoParametroDeRP.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(rs != null)  rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo sumarRPs de la clase "
                                                        + "DaoParametroDeRP\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
                Logger.getLogger(DaoParametroDeRP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return respuesta;
    }
    
    @Override
    public void update(DtoParametrosDeRP t) throws DaoException {
        String updateQuery = "UPDATE parametro_de_rp SET id_calificacion=? WHERE id_parametro_rp= "+
                              t.getPrametroRP().getID()+" AND id_rp= "+t.getRegistro_Personal().getID()+
                              " AND id_tema = "+t.getTema().getID();
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(updateQuery);){
            pst.setInt(1, t.getCalificaciones().getID());
            pst.executeUpdate();
        } catch (SQLException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al actualizar un "
                                                        + "parametro de rp\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            Logger.getLogger(Dao_Alumno.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(DtoParametrosDeRP t) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(DtoParametrosDeRP t) throws DaoException {
        ResultSet rs = null;
        try (Connection cn = conect(); PreparedStatement pst = cn.prepareStatement(seachquery);) {
            pst.setInt(1, 1);
            pst.setInt(2, t.getPrametroRP().getID());
            pst.setInt(3, t.getRegistro_Personal().getID());
            pst.setInt(4, t.getRegistro_Personal().getIndicadorLogro().getTema().getID());
            
            rs = pst.executeQuery();
            if(rs.next()){
                
            }else   create(t);
        } catch (Exception ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al buscar un "
                                                        + "parametro de rp\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
        }finally{
            try {
                if(rs != null)    rs.close();
            } catch (SQLException ex) {
                interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al cerrar el "
                                                        + "rs del metodo search de la clase "
                                                        + "DaoParametroDeRP\n");
                interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
                Logger.getLogger(DaoParametroDeRP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String fecha(){
        LocalDate f = LocalDate.now();
        switch(f.getMonthValue()){
            case 1: fecha2 = f.getYear()+"-01-31"; 
                    return f.getYear()+"-01-01";
        
            case 2: fecha2 = f.getYear()+"-02-28"; 
                    return f.getYear()+"-02-01";
                        
            case 3: fecha2 = f.getYear()+"-03-31"; 
                    return f.getYear()+"-03-01";
                    
            case 4: fecha2 = f.getYear()+"-04-30"; 
                    return f.getYear()+"-04-01";
                    
            case 5: fecha2 = f.getYear()+"-05-31"; 
                    return f.getYear()+"-05-01";
            
            case 6: fecha2 = f.getYear()+"-06-30"; 
                    return f.getYear()+"-06-01";
            
            case 7: fecha2 = f.getYear()+"-07-31"; 
                    return f.getYear()+"-07-01";
                    
            case 8: fecha2 = f.getYear()+"-08-31";
                    return f.getYear()+"-08-01";
            
            case 9: fecha2 = f.getYear()+"-09-30"; 
                    return f.getYear()+"-09-01";
                    
            case 10: fecha2 = f.getYear()+"-10-31"; 
                    return f.getYear()+"-10-01";
            
            case 11: fecha2 = f.getYear()+"-11-30"; 
                    return f.getYear()+"-11-01";
                    
            case 12: fecha2 = f.getYear()+"-12-31"; 
                     return f.getYear()+"-12-01";
                     
            default: return (null);
        }        
    }   
    
}
