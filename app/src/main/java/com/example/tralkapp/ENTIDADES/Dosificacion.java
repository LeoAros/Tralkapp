package com.example.tralkapp.ENTIDADES;

import android.content.ContentValues;

import com.example.tralkapp.DB.Tablas;

public class Dosificacion {
    private Integer id;
    private Integer id_droga;
    private Integer id_proceso;
    private Double concentracion;
    private Double dosis;

    public Dosificacion(Integer id, Integer id_droga, Integer id_proceso, Double concentracion, Double dosis) {
        this.id = id;
        this.id_droga = id_droga;
        this.id_proceso = id_proceso;
        this.concentracion = concentracion;
        this.dosis = dosis;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_droga() {
        return id_droga;
    }

    public void setId_droga(Integer id_droga) {
        this.id_droga = id_droga;
    }

    public Integer getId_proceso() {
        return id_proceso;
    }

    public void setId_proceso(Integer id_proceso) {
        this.id_proceso = id_proceso;
    }

    public Double getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(Double concentracion) {
        this.concentracion = concentracion;
    }

    public Double getDosis() {
        return dosis;
    }

    public void setDosis(Double dosis) {
        this.dosis = dosis;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Tablas.CAMPO_ID, id);
        values.put(Tablas.CAMPO_ID_DROGA, id_droga);
        values.put(Tablas.CAMPO_ID_PROCESO,id_proceso);
        values.put(Tablas.CAMPO_CONCENTRACION,concentracion);
        values.put(Tablas.CAMPO_DOSIS,dosis);
        return values;
    }
}
