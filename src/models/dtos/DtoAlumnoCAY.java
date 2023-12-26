package models.dtos;

import main.Start;

public class DtoAlumnoCAY {
    private DtoAlumno alumno;
    private DtoCurso curso;
    private DtoAsignatura asignatura;
    private DtoYearEscolar year_Escolar;
    private double promedio;
    private double porcentaje_asistencia;
    private String edicion;

    public DtoAlumnoCAY() {Start.setConteo(Start.getConteo()+0.1f);}

    public DtoAlumnoCAY(DtoAlumno alumno, DtoCurso curso, DtoAsignatura asignatura, 
            DtoYearEscolar year_Escolar, double promedio, double porcentaje_asistencia, String edicion){
        this.alumno = alumno;
        this.curso = curso;
        this.asignatura = asignatura;
        this.year_Escolar = year_Escolar;
        this.promedio = promedio;
        this.porcentaje_asistencia = porcentaje_asistencia;
        this.edicion = edicion;
    }

    public DtoAlumno getAlumno() {
        return alumno;
    }

    public void setAlumno(DtoAlumno alumno) {
        this.alumno = alumno;
    }

    public DtoCurso getCurso() {
        return curso;
    }

    public void setCurso(DtoCurso curso) {
        this.curso = curso;
    }

    public DtoAsignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(DtoAsignatura asignatura) {
        this.asignatura = asignatura;
    }

    public DtoYearEscolar getYear_Escolar() {
        return year_Escolar;
    }

    public void setYear_Escolar(DtoYearEscolar year_Escolar) {
        this.year_Escolar = year_Escolar;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public double getPorcentaje_asistencia() {
        return porcentaje_asistencia;
    }

    public void setPorcentaje_asistencia(double porcentaje_asistencia) {
        this.porcentaje_asistencia = porcentaje_asistencia;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }
}
