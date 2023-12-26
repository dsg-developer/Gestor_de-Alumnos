package controllers;

import controllers.singletons.Singleton_Class;
import controllers.singletons.Singleton_Controllers;
import controllers.singletons.SingletonDtos;
import controllers.singletons.Singleton_Interfaces;
import controllers.singletons.SingletonModels;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import main.Start;
import models.DaoException;
import views.RegistroError;

/**
 * @author Work
 */

public class Controller_AddAlumno {
    Singleton_Class clases;
    Singleton_Controllers controladores;
    SingletonDtos dtos;
    Singleton_Interfaces interfaces;
    SingletonModels modelos;

    public Controller_AddAlumno() {
        clases = Singleton_Class.getInstance();
        dtos = SingletonDtos.getInstance();
        interfaces = Singleton_Interfaces.getInstance();
        modelos = SingletonModels.getInstance();
        eventos();
        Start.setConteo(Start.getConteo()+0.1f);
    }

    private void addAlumno(){        
        try {
            
            dtos.fillAlumno(0, interfaces.add_alumno.JTFApellido.getText(),
                            interfaces.add_alumno.JTFNombre.getText(),
                            Byte.parseByte(interfaces.add_alumno.JSNumero.getValue().toString()));
            modelos.alumno.search(dtos.alumno);
            dtos.fillAlumnoCAY(dtos.alumno, dtos.curso, dtos.asignatura, dtos.yearEscolar);
            modelos.alumno_CAY.search(dtos.alumno_CAY);
            interfaces.add_alumno.resetComponent();
            controladores = Singleton_Controllers.getInstance();
            if(interfaces.principal.scroll_tabla_derecha.isVisible())
                controladores.principal.setCarga();
        } catch (DaoException ex) {
            interfaces.registro_error.registrarError(Level.SEVERE, "Nuevo error detectado al agregar un "
                                                        + "alumno\n");
            interfaces.registro_error.registrarError(Level.SEVERE, RegistroError.getStackTrace(ex));
            new DaoException("error en el metodo addAlumno de la clase Controller_addAlumno.");
        }        
    }
    
    private void eventos() {
        eventoBTN();
    }

    private void eventoBTN() {
        interfaces.add_alumno.JBAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(todoBien())  addAlumno(); 
            }
        });
    }
    
    private boolean todoBien(){
        if(interfaces.add_alumno.JTFApellido.getText().isEmpty() || interfaces.add_alumno.JTFNombre.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Es necesario que complete los campos faltantes para poder continuar.");
            return false;
        }
        else    return true;
    }
    
}
