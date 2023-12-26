package models.dtos;

import main.Start;

/**
 * @author Work-Game
 */

public class Dto_Fecha {
    private Integer ID;
    private String fecha;

    public Dto_Fecha() {Start.setConteo(Start.getConteo()+0.1f);}

    public Dto_Fecha(Integer ID, String fecha) {
        this.ID = ID;
        this.fecha = fecha;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
