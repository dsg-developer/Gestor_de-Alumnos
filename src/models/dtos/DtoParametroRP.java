package models.dtos;

import main.Start;

public class DtoParametroRP {
    private Integer ID;
    private String parametro;

    public DtoParametroRP() {Start.setConteo(Start.getConteo()+0.1f);}

    public DtoParametroRP(Integer ID, String parametro) {
        this.ID = ID;
        this.parametro = parametro;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }
}
