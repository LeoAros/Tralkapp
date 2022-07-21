package com.example.tralkapp;

import java.io.Serializable;

public class ListNotas implements Serializable {
    public int id;
    public String nombre;
    public String fecha;
    public String nota;
    public int id_procedimiento;

    public ListNotas(int id, String nombre, String fecha, String nota, int id_procedimiento) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.nota = nota;
        this.id_procedimiento = id_procedimiento;
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

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public int getId_procedimiento() {
        return id_procedimiento;
    }

    public void setId_procedimiento(int id_procedimiento) {
        this.id_procedimiento = id_procedimiento;
    }
}
