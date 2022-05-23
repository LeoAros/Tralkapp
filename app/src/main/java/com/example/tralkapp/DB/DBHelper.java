package com.example.tralkapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.tralkapp.ENTIDADES.Animal;
import com.example.tralkapp.ENTIDADES.Dosificacion;
import com.example.tralkapp.ENTIDADES.Droga;
import com.example.tralkapp.ENTIDADES.Proceso;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Tralkapp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Tablas.CREAR_TABLA_ANIMAL);
        db.execSQL(Tablas.CREAR_TABLA_DROGA);
        db.execSQL(Tablas.CREAR_TABLA_PROCESO);
        db.execSQL(Tablas.CREAR_TABLA_DOSIFICACION);

        // Rellenar tabla con datos
        mockData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Tablas.TABLA_ANIMAL);
        db.execSQL("DROP TABLE IF EXISTS "+Tablas.TABLA_DROGA);
        db.execSQL("DROP TABLE IF EXISTS "+Tablas.TABLA_PROCESO);
        db.execSQL("DROP TABLE IF EXISTS "+Tablas.TABLA_DOSIFICACION);
        onCreate(db);

    }

    public Cursor getData(String tabla){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select id,nombre from "+tabla,null);
        return  cursor;
    }

    public Cursor getProcesos(int id){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select id,nombre from proceso where id_animal = ?",new String[] {String.valueOf(id)});
        return  cursor;
    }

    public Cursor getDosificacion(int id){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select d1.id,id_droga,concentracion,dosis,d2.nombre from dosificacion d1 inner join droga d2 on d1.id_droga = d2.id  where id_proceso = ?",new String[] {String.valueOf(id)});
        return  cursor;
    }

    // Rellenar tabla con datos

    private void mockData(SQLiteDatabase db) {
        mockAnimal(db, new Animal(1, "Huemul"));
        mockDroga(db, new Droga(1, "Medetomidina"));
        mockDroga(db, new Droga(2, "Ketamina"));
        mockProceso(db, new Proceso(1, "Anestesiar",1));
        mockDosificacion(db,new Dosificacion(1,1,1,20.,0.11));
        mockDosificacion(db,new Dosificacion(2,2,1,100.,3.));

        mockProceso(db, new Proceso(2, "Proceso 2",1));
        mockDosificacion(db,new Dosificacion(3,1,2,30.3,0.33));
        mockDosificacion(db,new Dosificacion(4,2,2,300.,3.3));
    }

    public long mockAnimal(SQLiteDatabase db, Animal animal) {
        return db.insert(
                Tablas.TABLA_ANIMAL,
                null,
                animal.toContentValues());
    }

    public long mockDroga(SQLiteDatabase db, Droga droga) {
        return db.insert(
                Tablas.TABLA_DROGA,
                null,
                droga.toContentValues());
    }

    public long mockProceso(SQLiteDatabase db, Proceso proceso) {
        return db.insert(
                Tablas.TABLA_PROCESO,
                null,
                proceso.toContentValues());
    }

    public long mockDosificacion(SQLiteDatabase db, Dosificacion dosificacion) {
        return db.insert(
                Tablas.TABLA_DOSIFICACION,
                null,
                dosificacion.toContentValues());
    }
//hola perra
}
