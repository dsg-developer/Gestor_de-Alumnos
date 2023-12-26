package models.dtos;

import main.Start;

public class DtosTemas {
    private int ID;
    private String Tema;

    public DtosTemas() {
        this.ID = 0;
        this.Tema = "";
        Start.setConteo(Start.getConteo()+0.1f);
    }
    
    public DtosTemas(int ID, String Tema) {
        this.ID = ID;
        this.Tema = Tema;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTema() {
        return Tema;
    }

    public void setTema(String Tema) {
        this.Tema = Tema;
    }
}
