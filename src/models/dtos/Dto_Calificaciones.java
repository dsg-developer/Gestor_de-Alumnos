package models.dtos;

import main.Start;

public class Dto_Calificaciones {
    private Integer ID;
    private Byte calificacion;

    public Dto_Calificaciones() {Start.setConteo(Start.getConteo()+0.1f);}

    public Dto_Calificaciones(Integer ID, byte calificacion) {
        this.ID = ID;
        this.calificacion = calificacion;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Byte getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(byte calificacion) {
        this.calificacion = calificacion;
    }
}
