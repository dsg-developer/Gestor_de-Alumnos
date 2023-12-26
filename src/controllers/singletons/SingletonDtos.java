package controllers.singletons;

import main.Start;
import models.dtos.*;

/**
 * @author Work-Game
 */

public class SingletonDtos {
    private static SingletonDtos dtos;
    
    public DtoAlumno alumno;
    public DtoAlumnoCAY alumno_CAY;
    public DtoAsignatura asignatura;
    public Dto_Asistencia asistencia;
    public Dto_Calificaciones calificaciones;
    public Dto_calificaciones_indicador calificaciones_indicador;
    public Dto_Competencia competencia;
    public DtoCurso curso;
    public Dto_Fecha fecha;
    public DtoIndicador indicador;
    public DtoIndicadorLogro indicador_Logro;
    public DtoParametroRP prametroRP;
    public DtoParametrosDeRP parametros_de_RP;
    public DtoRegistroPersonal registro_Personal;
    public DtosTemas tema;
    public Dto_Unidad unidad;
    public DtoYearEscolar yearEscolar;

    private SingletonDtos() {
        this.dtos = null;
        this.alumno = new DtoAlumno();
        this.alumno_CAY = new DtoAlumnoCAY();
        this.asignatura = new DtoAsignatura();
        this.asistencia = new Dto_Asistencia();
        this.calificaciones = new Dto_Calificaciones();
        this.calificaciones_indicador = new Dto_calificaciones_indicador();
        this.competencia = new Dto_Competencia();
        this.curso = new DtoCurso();
        this.fecha = new Dto_Fecha();
        this.indicador = new DtoIndicador();
        this.indicador_Logro = new DtoIndicadorLogro();
        this.prametroRP = new DtoParametroRP();
        this.parametros_de_RP = new DtoParametrosDeRP();
        this.registro_Personal = new DtoRegistroPersonal();
        this.tema = new DtosTemas();
        this.unidad = new Dto_Unidad();
        this.yearEscolar = new DtoYearEscolar();
        Start.setConteo(Start.getConteo()+1.0f);
    }
    
    public static SingletonDtos getInstance(){
        if(dtos == null)    dtos = new SingletonDtos();
        return dtos;
    }

    public DtoAlumno fillAlumno(Integer ID, String apellido, String nombre, byte numero) {
        this.alumno.setID(ID);
        this.alumno.setApellido(apellido);
        this.alumno.setNombre(nombre);
        this.alumno.setNumero(numero);
        return alumno;
    }
    
    public void fillAlumnoCAY(DtoAlumno alumno, DtoCurso curso, DtoAsignatura asignatura, 
                              DtoYearEscolar year_Escolar) {
        this.alumno_CAY.setAlumno(alumno);
        this.alumno_CAY.setCurso(curso);
        this.alumno_CAY.setAsignatura(asignatura);
        this.alumno_CAY.setYear_Escolar(year_Escolar);
    }

    public DtoAsignatura fillAsignatura(Integer ID, String asignatura) {
        this.asignatura.setID(ID);
        this.asignatura.setAsignatura(asignatura);
        return this.asignatura;
    }

    public void fillAsistencia(Integer ID, DtoAlumno alumno, Dto_Fecha fecha, Byte asitencia) {
        this.asistencia.setID(ID);
        this.asistencia.setAlumno(alumno);
        this.asistencia.setFecha(fecha);
        this.asistencia.setAsitencia(asitencia);
    }
    
    public void fillCalificacion_indicador(DtoAlumno alumno, Dto_Calificaciones calificacion, DtoIndicadorLogro indicador) {
        this.calificaciones_indicador.setAlumno(alumno);
        this.calificaciones_indicador.setCalificacion(calificacion);
        this.calificaciones_indicador.setIndicadorLogro(indicador);
    }
    
    public Dto_Calificaciones fillCalificaciones(Integer ID, byte calificacion) {
        this.calificaciones.setID(ID);
        this.calificaciones.setCalificacion(calificacion);
        return this.calificaciones;
    }
    
    public Dto_Competencia fillCompetencia(Integer ID, String competencia) {
        this.competencia.setID(ID);
        this.competencia.setCompetencia(competencia);
        return this.competencia;
    }

    public DtoCurso fillCurso(Integer ID, String curso) {
        this.curso.setID(ID);
        this.curso.setCurso(curso);
        return this.curso;
    }

    public Dto_Fecha fillFecha(Integer ID, String fecha) {
        this.fecha.setID(ID);
        this.fecha.setFecha(fecha);
        return this.fecha;
    }

    public DtoIndicador fillIndicador(Integer ID, String indicador){
        this.indicador.setID(ID);
        this.indicador.setIndicador(indicador);
        return this.indicador;
    }
    
    public void fillIndicadorLogro(Integer ID, DtoAsignatura asignatura, Dto_Competencia competencia,
                                   DtoCurso curso, DtosTemas tema, Dto_Unidad unidad, DtoIndicador indicador,
                                   DtoYearEscolar year_escolar, byte periodo){
        this.indicador_Logro.setID(ID);
        this.indicador_Logro.setAsignatura(asignatura);
        this.indicador_Logro.setCompetencia(competencia);
        this.indicador_Logro.setCurso(curso);
        this.indicador_Logro.setIndicador(indicador);
        this.indicador_Logro.setTema(tema);
        this.indicador_Logro.setUnidad(unidad);
        this.indicador_Logro.setYear_escolar(year_escolar);
        this.indicador_Logro.setPeriodo(periodo);
        //return this.indicador_Logro;
    }
    
    public DtoParametroRP fillPrametroRP(Integer ID, String parametro) {
        this.prametroRP.setID(ID);
        this.prametroRP.setParametro(parametro);
        return this.prametroRP;
    }
    
    public DtoParametrosDeRP fillParametros_de_RP(Dto_Calificaciones calificaciones, DtoParametroRP parametro,
                                                    DtoRegistroPersonal registro_Personal, DtosTemas tema) {
        this.parametros_de_RP.setCalificaciones(calificaciones);
        this.parametros_de_RP.setPrametroRP(parametro);
        this.parametros_de_RP.setRegistro_Personal(registro_Personal);
        this.parametros_de_RP.setTema(tema);
        return this.parametros_de_RP;
    }
    
    public DtoRegistroPersonal fillRegistroPersonal(Integer ID, DtoAlumno alumno, 
                                                    DtoIndicadorLogro indicadorLogro, Dto_Fecha fecha) {
        this.registro_Personal.setID(ID);
        this.registro_Personal.setAlumno(alumno);
        this.registro_Personal.setIndicadorLogro(indicadorLogro);
        this.registro_Personal.setFecha(fecha);
        return  this.registro_Personal;
    }
    
    public DtosTemas fillTema(Integer ID, String tema){
        this.tema.setID(ID);
        this.tema.setTema(tema);
        return this.tema;
    }
    
    public Dto_Unidad fillUnidad(Integer ID, String unidad){
        this.unidad.setID(ID);
        this.unidad.setUnidad(unidad);
        return this.unidad;
    } 
    
    public DtoYearEscolar fillYearEscolar(Integer ID, String Year) {
        this.yearEscolar.setID(ID);
        this.yearEscolar.setYear(Year);
        return this.yearEscolar;
    }
}
