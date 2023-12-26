package models.dtos;

import main.Start;

/**
 * @author Work-Game
 */

public class DtoAsignatura {
    private Integer ID;
    private String asignatura;

    public DtoAsignatura() {Start.setConteo(Start.getConteo()+0.1f);}

    public DtoAsignatura(Integer ID, String asignatura) {
        this.ID = ID;
        this.asignatura = asignatura;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }
}
