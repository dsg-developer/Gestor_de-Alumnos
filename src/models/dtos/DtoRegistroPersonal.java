package models.dtos;

import main.Start;

public class DtoRegistroPersonal {
    private Integer ID;
    private DtoAlumno alumno;
    private Dto_Fecha fecha;
    private DtoIndicadorLogro indicadorLogro;

    public DtoRegistroPersonal() {Start.setConteo(Start.getConteo()+0.1f);} 

    public DtoRegistroPersonal(Integer ID, DtoAlumno alumno, DtoIndicadorLogro indicadorLogro, Dto_Fecha fecha
                                ) {
        this.ID = ID;
        this.alumno = alumno;
        this.indicadorLogro = indicadorLogro;
        this.fecha = fecha;
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

    public DtoIndicadorLogro getIndicadorLogro() {
        return indicadorLogro;
    }

    public void setIndicadorLogro(DtoIndicadorLogro indicadorLogro) {
        this.indicadorLogro = indicadorLogro;
    }

    public Dto_Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Dto_Fecha fecha) {
        this.fecha = fecha;
    }
}
