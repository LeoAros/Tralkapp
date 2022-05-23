package com.example.tralkapp.DB;

public class Tablas {

    public static final String CAMPO_ID="id";
    public static final String CAMPO_NOMBRE="nombre";

    //Constantes para tabla animal
    public static final String TABLA_ANIMAL="animal";

    public static final String CREAR_TABLA_ANIMAL="CREATE TABLE "+
            " "+TABLA_ANIMAL+" ("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            CAMPO_NOMBRE+" TEXT)";

    //Constantes para tabla droga
    public static final String TABLA_DROGA="droga";

    public static final String CREAR_TABLA_DROGA="CREATE TABLE "+
            " "+TABLA_DROGA+" ("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            CAMPO_NOMBRE+" TEXT)";

    //Constantes para tabla proceso
    public static final String TABLA_PROCESO="proceso";
    public static final String CAMPO_ID_ANIMAL="id_animal";

    public static final String CREAR_TABLA_PROCESO="CREATE TABLE "+
            " "+TABLA_PROCESO+" ("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            CAMPO_NOMBRE+" TEXT, "+CAMPO_ID_ANIMAL+" INTEGER REFERENCES "+TABLA_ANIMAL+"(id)) ";

    //Constantes para tabla dosificación
    public static final String TABLA_DOSIFICACION="dosificacion";
    public static final String CAMPO_ID_DROGA="id_droga";
    public static final String CAMPO_ID_PROCESO="id_proceso";
    public static final String CAMPO_CONCENTRACION="concentracion";
    public static final String CAMPO_DOSIS="dosis";

    public static final String CREAR_TABLA_DOSIFICACION="CREATE TABLE "+
            " "+TABLA_DOSIFICACION+" ("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            CAMPO_CONCENTRACION+" DOUBLE,"+
            CAMPO_DOSIS+" DOUBLE,"+
            CAMPO_ID_DROGA+" INTEGER REFERENCES "+TABLA_DROGA+"(id),"+
            CAMPO_ID_PROCESO+ " INTEGER REFERENCES "+TABLA_PROCESO+"(id))";

}
