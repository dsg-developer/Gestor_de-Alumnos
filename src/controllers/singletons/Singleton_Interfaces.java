package controllers.singletons;

import javax.swing.JFrame;
import main.Start;
import views.AddAlumno;
import views.AddIndicadorLogro;
import views.AddOrSelect;
import views.IndicadoresLogro;
import views.ModelsTable;
import views.Principal;
import views.RegistroError;
import views.Registros_Personal;

/**
 * @author Work
 */

public class Singleton_Interfaces {
    private static Singleton_Interfaces interfaces;
    public AddAlumno add_alumno;
    public AddIndicadorLogro addIndicador_logro;
    public AddOrSelect add_select;
    public IndicadoresLogro indicador_Logro;
    public ModelsTable modelos;
    public Principal principal;
    public RegistroError registro_error;
    public Registros_Personal addRegistro_Personal;
    
    private Singleton_Interfaces() {
        interfaces = null;
        add_alumno = new AddAlumno();
        addIndicador_logro = new AddIndicadorLogro(new JFrame(), false);
        add_select = new AddOrSelect(new JFrame(), false);
        indicador_Logro = new IndicadoresLogro();
        modelos = new ModelsTable();
        principal = new Principal();
        registro_error = new RegistroError(new JFrame(), false);
        addRegistro_Personal = new Registros_Personal(new JFrame(), false);
        Start.setConteo(Start.getConteo()+1.0f);
    }
    
    public static Singleton_Interfaces getInstance(){
        if(interfaces == null)  interfaces = new Singleton_Interfaces();
        return interfaces;
    }
}
