package models.dtos;

import main.Start;

/**
 * @author Work-Game
 */

public class Dto_Unidad {
    private Integer ID;
    private String unidad;

    public Dto_Unidad() {Start.setConteo(Start.getConteo()+0.1f);}

    public Dto_Unidad(Integer ID, String unidad) {
        this.ID = ID;
        this.unidad = unidad;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
}
