package models.dtos;

import main.Start;

/**
 * @author Work-Game
 */

public class DtoIndicadorLogro {
    private Integer ID;
    private DtoAsignatura asignatura;
    private Dto_Competencia competencia;
    private DtoCurso curso;
    private DtoIndicador indicador;
    private DtosTemas tema;
    private Dto_Unidad unidad;
    private DtoYearEscolar year_escolar;
    private byte periodo;

    public DtoIndicadorLogro() {Start.setConteo(Start.getConteo()+0.1f);}

    public DtoIndicadorLogro(Integer ID, DtoAsignatura asignatura, Dto_Competencia competencia, 
                             DtoCurso curso, DtoIndicador indicador, DtosTemas tema, Dto_Unidad unidad,
                             DtoYearEscolar year_escolar, byte periodo) {
        this.ID = ID;
        this.asignatura = asignatura;
        this.competencia = competencia;
        this.curso = curso;
        this.indicador = indicador;
        this.tema = tema;
        this.unidad = unidad;
        this.year_escolar = year_escolar;
        this.periodo = periodo;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public DtoAsignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(DtoAsignatura asignatura) {
        this.asignatura = asignatura;
    }

    public Dto_Competencia getCompetencia() {
        return competencia;
    }

    public void setCompetencia(Dto_Competencia competencia) {
        this.competencia = competencia;
    }

    public DtoCurso getCurso() {
        return curso;
    }

    public void setCurso(DtoCurso curso) {
        this.curso = curso;
    }

    public DtoIndicador getIndicador() {
        return indicador;
    }

    public void setIndicador(DtoIndicador indicador) {
        this.indicador = indicador;
    }

    public DtosTemas getTema() {
        return tema;
    }

    public void setTema(DtosTemas tema) {
        this.tema = tema;
    }

    public Dto_Unidad getUnidad() {
        return unidad;
    }

    public void setUnidad(Dto_Unidad unidad) {
        this.unidad = unidad;
    }

    public DtoYearEscolar getYear_escolar() {
        return year_escolar;
    }

    public void setYear_escolar(DtoYearEscolar year_escolar) {
        this.year_escolar = year_escolar;
    }

    public byte getPeriodo() {
        return periodo;
    }

    public void setPeriodo(byte periodo) {
        this.periodo = periodo;
    }

}
