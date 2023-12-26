package models.dtos;

import main.Start;

public class DtoIndicador {
    private Integer ID;
    private String indicador;

    public DtoIndicador() {Start.setConteo(Start.getConteo()+0.1f);}

    public DtoIndicador(Integer ID, String indicador) {
        this.ID = ID;
        this.indicador = indicador;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }
}
