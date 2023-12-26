package controllers.singletons;

import main.Start;
import models.ImportarDocumentos;
import models.daos.DaoTema;
import models.daos.Dao_Alumno;
import models.daos.DaoAlumnoCAY;
import models.daos.DaoIndicador;
import models.daos.DaoAsignatura;
import models.daos.Dao_Asistencia;
import models.daos.Dao_CAY;
import models.daos.DaoCompetencia;
import models.daos.Dao_Curso;
import models.daos.Dao_Fecha;
import models.daos.DaoIndicadorLogro;
import models.daos.Dao_Unidad;
import models.daos.DaoYearsEscolar;
import models.daos.Dao_AddRP;
import models.daos.DaoCalificaciones;
import models.daos.DaoCalificacionesIndicador;
import models.daos.Dao_ParametroRP;
import models.daos.DaoParametroDeRP;

/**
 * @author Work
 */

public class SingletonModels {
    private static SingletonModels models;
    public Dao_AddRP addRegistro_Personal;
    public Dao_Alumno alumno;
    public DaoAlumnoCAY alumno_CAY;
    public DaoAsignatura asignatura;
    public Dao_Asistencia asistencias;
    public DaoCalificaciones Calificaciones;
    public DaoCalificacionesIndicador calificaciones_Indicador;
    public Dao_CAY cay;
    public DaoCompetencia competencia;
    public Dao_Curso curso;
    public Dao_Fecha fechas;
    public ImportarDocumentos importar;
    public DaoIndicador indicador;
    public DaoIndicadorLogro indicador_logro;
    public Dao_ParametroRP parametroRP;
    public DaoParametroDeRP parametro_de_RP;
    public DaoTema tema;
    public Dao_Unidad unidad;
    public DaoYearsEscolar year_escolar;

    private SingletonModels() {
        models = null;
        addRegistro_Personal = new Dao_AddRP();
        alumno = new Dao_Alumno();
        alumno_CAY = new DaoAlumnoCAY();
        asignatura = new DaoAsignatura();
        asistencias = new Dao_Asistencia();
        Calificaciones = new DaoCalificaciones();
        calificaciones_Indicador = new DaoCalificacionesIndicador();
        cay = new Dao_CAY();
        competencia = new DaoCompetencia();
        curso = new Dao_Curso();
        fechas = new Dao_Fecha();
        importar = new ImportarDocumentos();
        indicador = new DaoIndicador();
        indicador_logro = new DaoIndicadorLogro();
        parametroRP = new Dao_ParametroRP();
        parametro_de_RP = new DaoParametroDeRP();
        this.tema = new DaoTema();
        unidad = new Dao_Unidad();
        year_escolar = new DaoYearsEscolar();
        Start.setConteo(Start.getConteo()+1.0f);
    }
    
    public static SingletonModels getInstance(){
        if(models == null)  models = new SingletonModels();
        return models;
    }
}
