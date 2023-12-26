package models.dtos;

import main.Start;

/**
 * @author Work-Game
 */

public class Dto_Competencia {
    private Integer ID;
    private String competencia;
    
    public Dto_Competencia() {Start.setConteo(Start.getConteo()+0.1f);}

    public Dto_Competencia(Integer ID, String competencia) {
        this.ID = ID;
        this.competencia = competencia;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }
    
}
