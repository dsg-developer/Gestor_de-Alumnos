package models.dtos;

import main.Start;

public class DtoParametrosDeRP {
    private Dto_Calificaciones calificaciones;
    private DtoParametroRP prametroRP;
    private DtoRegistroPersonal registro_Personal;
    private DtosTemas tema;

    public DtoParametrosDeRP() {Start.setConteo(Start.getConteo()+0.1f);}

    public DtoParametrosDeRP(Dto_Calificaciones calificaciones, DtoParametroRP prametroRP, DtoRegistroPersonal 
                             registro_Personal, DtosTemas tema) {
        this.calificaciones = calificaciones;
        this.prametroRP = prametroRP;
        this.registro_Personal = registro_Personal;
        this.tema = tema;
    }

    public Dto_Calificaciones getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(Dto_Calificaciones calificaciones) {
        this.calificaciones = calificaciones;
    }

    public DtoParametroRP getPrametroRP() {
        return prametroRP;
    }

    public void setPrametroRP(DtoParametroRP prametroRP) {
        this.prametroRP = prametroRP;
    }

    public DtoRegistroPersonal getRegistro_Personal() {
        return registro_Personal;
    }

    public void setRegistro_Personal(DtoRegistroPersonal registro_Personal) {
        this.registro_Personal = registro_Personal;
    }

    public DtosTemas getTema() {
        return tema;
    }

    public void setTema(DtosTemas tema) {
        this.tema = tema;
    }
}
