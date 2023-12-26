package models.dtos;

import main.Start;

/**
 * @author Work-Game
 */

public class Dto_calificaciones_indicador {
    private DtoAlumno alumno;
    private Dto_Calificaciones calificacion;
    private DtoIndicadorLogro indicadorLogro;

    public Dto_calificaciones_indicador() {Start.setConteo(Start.getConteo()+0.1f);}

    public Dto_calificaciones_indicador(DtoAlumno alumno, Dto_Calificaciones calificacion, DtoIndicadorLogro indicadorLogro) {
        this.alumno = alumno;
        this.calificacion = calificacion;
        this.indicadorLogro = indicadorLogro;
    }

    public DtoAlumno getAlumno() {
        return alumno;
    }

    public void setAlumno(DtoAlumno alumno) {
        this.alumno = alumno;
    }

    public Dto_Calificaciones getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Dto_Calificaciones calificacion) {
        this.calificacion = calificacion;
    }

    public DtoIndicadorLogro getIndicadorLogro() {
        return indicadorLogro;
    }

    public void setIndicadorLogro(DtoIndicadorLogro indicadorLogro) {
        this.indicadorLogro = indicadorLogro;
    }
    
}
