package controllers.singletons;

import controllers.Controller_AddAlumno;
import controllers.Controller_AddOrSelect;
import controllers.Controller_Registro_Personal;
import controllers.Controller_Indicadores;
import controllers.Controller_Principal;
import main.Start;

/**
 * @author Work
 */

public class Singleton_Controllers {
    private static Singleton_Controllers controllers;
    public Controller_AddAlumno add_alumno;
    public Controller_Registro_Personal addRegistro_Personal;
    public Controller_AddOrSelect add_select;
    public Controller_Indicadores indicadores;
    public Controller_Principal principal;
    
    private Singleton_Controllers() {
        add_alumno = new Controller_AddAlumno();
        addRegistro_Personal = new Controller_Registro_Personal();
        add_select = new Controller_AddOrSelect();
        indicadores = new Controller_Indicadores();
        principal = new Controller_Principal();
        Start.setConteo(Start.getConteo()+1.0f);
    }
    
    public static Singleton_Controllers getInstance(){
        if(controllers == null) controllers = new Singleton_Controllers();
        return controllers;
    }
}
