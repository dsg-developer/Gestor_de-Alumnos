package models.dtos;

import main.Start;

/**
 * @author Work-Game
 */

public class Dto_Asistencia {
    private Integer ID;
    private DtoAlumno alumno;
    private Dto_Fecha fecha;
    private Byte asitencia;

    public Dto_Asistencia() {Start.setConteo(Start.getConteo()+0.1f);}

    public Dto_Asistencia(Integer ID, DtoAlumno alumno, Dto_Fecha fecha, Byte asitencia) {
        this.ID = ID;
        this.alumno = alumno;
        this.fecha = fecha;
        this.asitencia = asitencia;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public DtoAlumno getAlumno() {
        return alumno;
    }

    public void setAlumno(DtoAlumno alumno) {
        this.alumno = alumno;
    }

    public Dto_Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Dto_Fecha fecha) {
        this.fecha = fecha;
    }

    public Byte getAsitencia() {
        return asitencia;
    }

    public void setAsitencia(Byte asitencia) {
        this.asitencia = asitencia;
    }
}
