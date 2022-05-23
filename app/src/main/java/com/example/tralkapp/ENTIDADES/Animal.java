package com.example.tralkapp.ENTIDADES;

import android.content.ContentValues;

import com.example.tralkapp.DB.Tablas;

public class Animal {
    private Integer id;
    private String nombre;

    public Animal(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Tablas.CAMPO_ID, id);
        values.put(Tablas.CAMPO_NOMBRE, nombre);
        return values;
    }
}
