package models;

import java.util.ArrayList;
import java.util.List;
import main.Start;

/**
 * @author Work
 */

public class GenericObject{
    private List<Object> datos;
    
    public GenericObject() {datos = new ArrayList<>(); Start.setConteo(Start.getConteo()+0.1f);}

    public GenericObject(List<Object> datos) { this.datos = datos; }

    public List<Object> getDatos() {
        return datos;
    }

    public void setDatos(List<Object> datos) {
        this.datos = datos;
    }
    
    public void clearDatos() {
        if(datos != null)  datos.clear();
    }
}
