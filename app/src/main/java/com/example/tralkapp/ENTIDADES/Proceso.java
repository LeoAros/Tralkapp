package com.example.tralkapp.ENTIDADES;

import android.content.ContentValues;

import com.example.tralkapp.DB.Tablas;

public class Proceso {
    private Integer id;
    private String nombre;
    private Integer id_animal;

    public Proceso(Integer id, String nombre, Integer id_animal) {
        this.id = id;
        this.nombre = nombre;
        this.id_animal = id_animal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId_animal() {
        return id_animal;
    }

    public void setId_animal(Integer id_animal) {
        this.id_animal = id_animal;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Tablas.CAMPO_ID, id);
        values.put(Tablas.CAMPO_NOMBRE, nombre);
        values.put(Tablas.CAMPO_ID_ANIMAL,id_animal);
        return values;
    }
}
