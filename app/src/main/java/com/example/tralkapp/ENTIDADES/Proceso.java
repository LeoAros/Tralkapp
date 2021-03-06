package com.example.tralkapp.ENTIDADES;

import android.content.ContentValues;

import com.example.tralkapp.DB.Tablas;

public class Proceso {
    private Integer id;
    private String nombre;
    private Integer id_especie;

    public Proceso(Integer id, String nombre, Integer id_especie) {
        this.id = id;
        this.nombre = nombre;
        this.id_especie = id_especie;
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

    public Integer getId_especie() {
        return id_especie;
    }

    public void setId_especie(Integer id_especie) {
        this.id_especie = id_especie;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Tablas.CAMPO_ID, id);
        values.put(Tablas.CAMPO_NOMBRE, nombre);
        values.put(Tablas.CAMPO_ID_ESPECIE,id_especie);
        return values;
    }
}
