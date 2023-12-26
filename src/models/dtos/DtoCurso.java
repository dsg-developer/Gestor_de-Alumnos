package models.dtos;

import main.Start;

/**
 * @author Work-Game
 */

public class DtoCurso {
    private Integer ID;
    private String curso;

    public DtoCurso() {Start.setConteo(Start.getConteo()+0.1f);}

    public DtoCurso(Integer ID, String curso) {
        this.ID = ID;
        this.curso = curso;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
