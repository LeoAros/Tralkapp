package com.example.tralkapp.DB;

public class Tablas {

    public static final String CAMPO_ID = "id";
    public static final String CAMPO_NOMBRE = "nombre";

    //Constantes para tabla tipo_user
    public static final String TABLA_TIPO = "tipo";

    public static final String CREAR_TABLA_TIPO = "CREATE TABLE "+
            TABLA_TIPO+" ("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            CAMPO_NOMBRE+" TEXT)";

    //Constantes para tabla users
    public static final String TABLA_USER = "user";
    public static final String CAMPO_APELLIDO = "apellido";
    public static final String CAMPO_EMAIL = "email";
    public static final String CAMPO_PASSWORD = "password";
    public static final String CAMPO_TIPO = "id_tipo";

    public static final String CREAR_TABLA_USER = "CREATE TABLE "+
            TABLA_USER+" ("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            CAMPO_NOMBRE+ " TEXT, "+CAMPO_APELLIDO+" TEXT,"+CAMPO_PASSWORD+" TEXT, "+CAMPO_EMAIL+" TEXT, "+
            CAMPO_TIPO+" INTEGER REFERENCES "+TABLA_TIPO+"(id))";

    //Constantes para tabla especie de animal
    public static final String TABLA_ESPECIE = "especie";

    public static final String CREAR_TABLA_ESPECIE = "CREATE TABLE " +
            TABLA_ESPECIE + " (" + CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_NOMBRE + " TEXT)";

    //Constantes para tabla droga
    public static final String TABLA_DROGA = "droga";

    public static final String CREAR_TABLA_DROGA = "CREATE TABLE " +
            TABLA_DROGA + " (" + CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_NOMBRE + " TEXT)";

    //Constantes para tabla proceso
    public static final String TABLA_PROCESO = "proceso";
    public static final String CAMPO_ID_ESPECIE = "id_especie";

    public static final String CREAR_TABLA_PROCESO = "CREATE TABLE " +
            TABLA_PROCESO + " (" + CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_NOMBRE + " TEXT, " + CAMPO_ID_ESPECIE + " INTEGER REFERENCES " + TABLA_ESPECIE + "(id)) ";

    //Constantes para tabla dosificación
    public static final String TABLA_DOSIFICACION = "dosificacion";
    public static final String CAMPO_ID_DROGA = "id_droga";
    public static final String CAMPO_ID_PROCESO = "id_proceso";
    public static final String CAMPO_CONCENTRACION = "concentracion";
    public static final String CAMPO_DOSIS = "dosis";

    public static final String CREAR_TABLA_DOSIFICACION = "CREATE TABLE " +
            TABLA_DOSIFICACION + " (" + CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_CONCENTRACION + " DOUBLE," +
            CAMPO_DOSIS + " DOUBLE," +
            CAMPO_ID_DROGA + " INTEGER REFERENCES " + TABLA_DROGA + "(id)," +
            CAMPO_ID_PROCESO + " INTEGER REFERENCES " + TABLA_PROCESO + "(id))";

    //Constantes para tabla estado
    public static final String TABLA_ESTADO = "estado";
    public static final String CREAR_TABLA_ESTADO = "CREATE TABLE "+
            TABLA_ESTADO+" ("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            CAMPO_NOMBRE + " TEXT)";

    //Constantes para tabla genero (macho-hembra)
    public static final String TABLA_GENERO = "genero";
    public static final String CREAR_TABLA_GENERO = "CREATE TABLE "+
            TABLA_GENERO+" ("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            CAMPO_NOMBRE + " TEXT)";

    //Constantes para tabla ejemplar
    public static final String TABLA_EJEMPLAR = "ejemplar";
    public static final String CAMPO_PESO_EJEMPLAR = "peso";
    public static final String CAMPO_ID_ESTADO = "id_estado";
    public static final String CAMPO_RADIO_COLLAR = "radioCollar";
    public static final String CAMPO_ID_GENERO= "id_genero";

    public static final String CREAR_TABLA_EJEMPLAR = "CREATE TABLE "+
            TABLA_EJEMPLAR+" ("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            CAMPO_NOMBRE + " TEXT, "+CAMPO_PESO_EJEMPLAR+ " DOUBLE, "+
            CAMPO_ID_ESPECIE + " INTEGER REFERENCES "+TABLA_ESPECIE+"(id),"+
            CAMPO_ID_ESTADO + " INTEGER REFERENCES "+TABLA_ESTADO+"(id),"+
            CAMPO_RADIO_COLLAR + " TEXT,"+
            CAMPO_ID_GENERO + " INTEGER REFERENCES "+TABLA_GENERO+"(id))";

    //Constantes para tabla procedimiento
    public static final String TABLA_PROCEDIMIENTO = "procedimiento";
    public static final String CAMPO_FECHA = "fecha";
    public static final String CAMPO_ID_EJEMPLAR = "id_ejemplar";
    public static final String CAMPO_ID_USUARIO = "id_user";

    public static final String CREAR_TABLA_PROCEDIMIENTO = "CREATE TABLE "+
            TABLA_PROCEDIMIENTO+" ("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            CAMPO_NOMBRE+" TEXT, "+CAMPO_FECHA+" DATETIME DEFAULT CURRENT_TIMESTAMP,"+
            CAMPO_ID_EJEMPLAR+" INTEGER REFERENCES "+TABLA_EJEMPLAR+"(id),"+
            CAMPO_ID_USUARIO+ " INTEGER REFERENCES "+TABLA_USER+"(id))";

    //Constantes para tabla notas
    public static final String TABLA_NOTA = "nota";
    public static final String CAMPO_PROCEDIMIENTO = "id_procedimiento";

    public static final String CREAR_TABLA_NOTA = "CREATE TABLE "+
            TABLA_NOTA+" ("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            CAMPO_NOMBRE+" TEXT, "+
            CAMPO_FECHA+" DATETIME DEFAULT CURRENT_TIMESTAMP,"+
            CAMPO_PROCEDIMIENTO+" INTEGER REFERENCES "+TABLA_PROCEDIMIENTO+"(id),"+
            CAMPO_ID_USUARIO+ " INTEGER REFERENCES "+TABLA_USER+"(id))";
}
