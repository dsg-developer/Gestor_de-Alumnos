package controllers.singletons;

import models.ClassIDs;
import models.Conector;
import models.GenericObject;

/**
 * @author Work
 */

public class Singleton_Class {
    private static Singleton_Class clases;
    public GenericObject objecto;
    public ClassIDs ids;
    public Conector conector;

    private Singleton_Class() {
        conector = new Conector();
        objecto = new GenericObject();
        ids = new ClassIDs();
    }
    
    public static Singleton_Class getInstance(){
        if(clases == null)  clases = new Singleton_Class();
        return clases;
    }
}
