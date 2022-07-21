package com.example.tralkapp;

import java.io.Serializable;

public class ListProcedimientos implements Serializable {
    public int id;
    public String nombre;
    public String fecha;
    public String proce;
    public int id_ejemplar;

    public ListProcedimientos(int id, String nombre, String fecha, String proce, int id_ejemplar) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.proce = proce;
        this.id_ejemplar = id_ejemplar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getProce() {
        return proce;
    }

    public void setProce(String proce) {
        this.proce = proce;
    }

    public int getId_ejemplar() {
        return id_ejemplar;
    }

    public void setId_ejemplar(int id_ejemplar) {
        this.id_ejemplar = id_ejemplar;
    }
}
