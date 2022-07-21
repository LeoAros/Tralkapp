package com.example.tralkapp;

import java.io.Serializable;

public class ListElement implements Serializable {
    public int id;
    public String nombre;
    public String peso;
    public String ficha;
    public int id_especie;
    public int id_genero;

    public ListElement(int id, String nombre, String peso, String ficha, int id_especie,int id_genero) {
        this.id = id;
        this.nombre = nombre;
        this.peso = peso;
        this.ficha = ficha;
        this.id_especie = id_especie;
        this.id_genero = id_genero;
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

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getFicha() {
        return ficha;
    }

    public void setFicha(String ficha) {
        this.ficha = ficha;
    }

    public int getId_especie() {
        return id_especie;
    }

    public void setId_especie(int id_especie) {
        this.id_especie = id_especie;
    }

    public int getId_genero() {
        return id_genero;
    }

    public void setId_genero(int id_genero) {
        this.id_genero = id_genero;
    }
}
