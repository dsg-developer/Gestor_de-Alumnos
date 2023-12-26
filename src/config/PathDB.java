/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.io.File;
import main.Start;

/**
 *
 * @author Work
 */
public class PathDB {
    private final String caracter = System.getProperty("file.separator");

    //private String ruta = System.getenv("SystemDrive")+caracter+"ProgramData"+caracter+"gestor_alumnos";
    private final String ruta = System.getProperty("user.home")+caracter+"Documents"+caracter+
                                "gestor_notas_dataBase";
    public File archivo;
    public File carpeta;
    
    //db mysql
    private final String usuario = "dionisio";
    private final String password = "LucasCorreTodosLosDias";
    private final String DB = "proyectox.ddns.net:3306/updates";

    public PathDB() {        
        carpeta = new File(ruta);
        carpeta.mkdirs();
        archivo = new File(carpeta.getAbsolutePath()+caracter+"g_aData.db");
    }
    
    public String getCaracter() {
        return caracter;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPassword() {
        return password;
    }

    public String getDB() {
        return DB;
    }
}
