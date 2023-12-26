package models.dtos;

import main.Start;

/**
 * @author Work-Game
 */

public class DtoYearEscolar {
    private Integer ID;
    private String Year;

    public DtoYearEscolar() {Start.setConteo(Start.getConteo()+0.1f);}

    public DtoYearEscolar(Integer ID, String Year) {
        this.ID = ID;
        this.Year = Year;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String Year) {
        this.Year = Year;
    }
}
