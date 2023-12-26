package models.dtos;

import main.Start;

/**
 * @author Work-Game
 */

public class DtoAlumno {
    private Integer ID;
    private String apellido;
    private String nombre;
    private byte numero;
    private String edicion;

    public DtoAlumno() {Start.setConteo(Start.getConteo()+0.1f);}

    public DtoAlumno(Integer ID, String apellido, String nombre, byte numero) {
        this.ID = ID;
        this.apellido = apellido;
        this.nombre = nombre;
        this.numero = numero;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public byte getNumero() {
        return numero;
    }

    public void setNumero(byte numero) {
        this.numero = numero;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }

}
