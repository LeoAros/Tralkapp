package com.example.tralkapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tralkapp.ENTIDADES.Dosificacion;
import com.example.tralkapp.ENTIDADES.Droga;
import com.example.tralkapp.ENTIDADES.Especie;
import com.example.tralkapp.ENTIDADES.Estado;
import com.example.tralkapp.ENTIDADES.Genero;
import com.example.tralkapp.ENTIDADES.Proceso;
import com.example.tralkapp.ENTIDADES.Tipo;
import com.example.tralkapp.ENTIDADES.User;
import com.example.tralkapp.SessionManagement;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Tralkapp.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Tablas.CREAR_TABLA_TIPO);
        db.execSQL(Tablas.CREAR_TABLA_USER);
        db.execSQL(Tablas.CREAR_TABLA_ESPECIE);
        db.execSQL(Tablas.CREAR_TABLA_DROGA);
        db.execSQL(Tablas.CREAR_TABLA_PROCESO);
        db.execSQL(Tablas.CREAR_TABLA_DOSIFICACION);
        db.execSQL(Tablas.CREAR_TABLA_ESTADO);
        db.execSQL(Tablas.CREAR_TABLA_GENERO);
        db.execSQL(Tablas.CREAR_TABLA_EJEMPLAR);
        db.execSQL(Tablas.CREAR_TABLA_PROCEDIMIENTO);
        db.execSQL(Tablas.CREAR_TABLA_NOTA);

        // Rellenar tabla con datos
        mockData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Tablas.TABLA_TIPO);
        db.execSQL("DROP TABLE IF EXISTS "+Tablas.TABLA_USER);
        db.execSQL("DROP TABLE IF EXISTS "+Tablas.TABLA_ESPECIE);
        db.execSQL("DROP TABLE IF EXISTS "+Tablas.TABLA_DROGA);
        db.execSQL("DROP TABLE IF EXISTS "+Tablas.TABLA_PROCESO);
        db.execSQL("DROP TABLE IF EXISTS "+Tablas.TABLA_DOSIFICACION);
        db.execSQL("DROP TABLE IF EXISTS "+Tablas.TABLA_ESTADO);
        db.execSQL("DROP TABLE IF EXISTS "+Tablas.TABLA_GENERO);
        db.execSQL("DROP TABLE IF EXISTS "+Tablas.TABLA_EJEMPLAR);
        db.execSQL("DROP TABLE IF EXISTS "+Tablas.TABLA_PROCEDIMIENTO);
        db.execSQL("DROP TABLE IF EXISTS "+Tablas.TABLA_NOTA);
        onCreate(db);

    }

    public Cursor getDosificacion(int id){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select d1.id,id_droga,concentracion,dosis,d2.nombre from dosificacion d1 inner join droga d2 on d1.id_droga = d2.id  where id_proceso = ?",new String[] {String.valueOf(id)});
        return  cursor;
    }

    public Cursor getAll(String tabla){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from "+tabla,null);
        return  cursor;
    }

    public Cursor getWhere(String tabla,String where,int dato){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from "+tabla+" where "+where+" = ?",new String[] {String.valueOf(dato)});
        return  cursor;
    }

    public Cursor getOrder(String tabla,String where,int dato,String order){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from "+tabla+" where "+where+" = ? order by fecha "+order+" ",new String[] {String.valueOf(dato)});
        return  cursor;
    }

    public Cursor getFicha(int dato){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select e.id,e.nombre,es.nombre,e.peso,est.nombre,e.radioCollar,e.id_genero from ejemplar e inner join especie es on e.id_especie = es.id inner join estado est on e.id_estado = est.id where e.id = ?",new String[] {String.valueOf(dato)});
        return  cursor;
    }

    public Cursor getPeso(String tabla,String where,int dato){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select peso from "+tabla+" where "+where+" = ?",new String[] {String.valueOf(dato)});
        return  cursor;
    }

    public Cursor getData(String campo,String tabla,String where,int dato){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select "+campo+" from "+tabla+" where "+where+" = ?",new String[] {String.valueOf(dato)});
        return  cursor;
    }

    public Cursor getUser(String tabla,String where,String dato){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from "+tabla+" where "+where+" = ?",new String[] {dato});
        return  cursor;
    }

    public Cursor getDatosUser(int dato){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select u.id,u.nombre,u.apellido,u.email,t.nombre from user u inner join tipo t on u.id_tipo = t.id where u.id = ?",new String[] {String.valueOf(dato)});
        return  cursor;
    }

    public Cursor getDatosProcePdf(int dato){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select p.nombre,p.fecha,u.nombre,u.apellido from procedimiento p inner join user u on p.id_user = u.id where p.id = ?",new String[] {String.valueOf(dato)});
        return  cursor;
    }

    public Cursor getNotas(int dato){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select n.nombre,n.fecha,u.nombre,u.apellido from nota n inner join user u on n.id_user = u.id where id_procedimiento = ? order by fecha asc ",new String[] {String.valueOf(dato)});
        return  cursor;
    }

    public Cursor getFolderPdf(int id_proce){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select p.nombre,p.fecha,e.nombre from procedimiento p inner join ejemplar e on p.id_ejemplar = e.id where p.id = ? ",new String[] {String.valueOf(id_proce)});
        return cursor;
    }

    // Rellenar tabla con datos

    private void mockData(SQLiteDatabase db) {
        mockEspecie(db, new Especie(1, "Huemul"));
        mockDroga(db, new Droga(1, "Medetomidina"));
        mockDroga(db, new Droga(2, "Ketamina"));

        mockProceso(db, new Proceso(1, "Inmovilización química",1));
        mockDosificacion(db,new Dosificacion(1,1,1,20.,0.11));
        mockDosificacion(db,new Dosificacion(2,2,1,100.,3.));

        mockProceso(db, new Proceso(2, "Proceso 2",1));
        mockDosificacion(db,new Dosificacion(3,1,2,30.3,0.33));
        mockDosificacion(db,new Dosificacion(4,2,2,300.,3.3));

//        mockProceso(db, new Proceso(3, "Proceso 3",1));
//        mockDosificacion(db,new Dosificacion(5,1,2,30.3,0.33));
//        mockDosificacion(db,new Dosificacion(6,2,2,300.,3.3));
//
//        mockProceso(db, new Proceso(4, "Proceso 4",1));
//        mockDosificacion(db,new Dosificacion(7,1,3,30.3,0.33));
//        mockDosificacion(db,new Dosificacion(8,2,3,300.,3.3));
//
//        mockProceso(db, new Proceso(5, "Proceso 5",1));
//        mockDosificacion(db,new Dosificacion(9,1,4,30.3,0.33));
//        mockDosificacion(db,new Dosificacion(10,2,4,300.,3.3));
//
//        mockProceso(db, new Proceso(6, "Proceso 6",1));
//        mockDosificacion(db,new Dosificacion(11,1,5,30.3,0.33));
//        mockDosificacion(db,new Dosificacion(12,2,6,300.,3.3));

        mockEstado(db,new Estado(1,"Semicautiverio"));
        mockEstado(db,new Estado(2,"Libertad"));

        mockGenero(db,new Genero(1,"Macho"));
        mockGenero(db,new Genero(2,"Hembra"));

        mockTipo(db,new Tipo(1,"Veterinario"));
        mockTipo(db,new Tipo(2,"Asistente"));

        mockUser(db,new User(1,"Leonardo","Aros","12345678","leo@tralkapp.cl",1));
        mockUser(db,new User(2,"Mauro","Aros","12345678","mauro@tralkapp.cl",2));

    }

    public long mockEspecie(SQLiteDatabase db, Especie especie) {
        return db.insert(
                Tablas.TABLA_ESPECIE,
                null,
                especie.toContentValues());
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

    public long mockEstado(SQLiteDatabase db, Estado estado) {
        return db.insert(
                Tablas.TABLA_ESTADO,
                null,
                estado.toContentValues());
    }

    public long mockGenero(SQLiteDatabase db, Genero genero) {
        return db.insert(
                Tablas.TABLA_GENERO,
                null,
                genero.toContentValues());
    }

    public long mockTipo(SQLiteDatabase db, Tipo tipo) {
        return db.insert(
                Tablas.TABLA_TIPO,
                null,
                tipo.toContentValues());
    }

    public long mockUser(SQLiteDatabase db, User user) {
        return db.insert(
                Tablas.TABLA_USER,
                null,
                user.toContentValues());
    }

    public int insertarEjemplar(String nombre,String peso,int id_especie,int id_estado,String radio,int id_genero){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String nombreEjemplarUpper = nombre.substring(0, 1).toUpperCase() + nombre.substring(1).toLowerCase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre",nombreEjemplarUpper);
        contentValues.put("peso",peso);
        contentValues.put("id_especie",id_especie);
        contentValues.put("id_estado",id_estado);
        contentValues.put("radioCollar",radio);
        contentValues.put("id_genero",id_genero);

        Cursor res = getWhere("ejemplar","radioCollar",Integer.valueOf(radio));
        if(res.moveToFirst()){
            return 0;
        }

        long result = MyDB.insert("ejemplar",null,contentValues);
        if(result==-1){
            return 1;
        }else {
            return 2;
        }
    }

    public Boolean editarEjemplar(String id_ejemplar,String nombre,Double peso,int id_especie,int id_estado,String radio,int id_genero){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String nombreEjemplarUpper = nombre.substring(0, 1).toUpperCase() + nombre.substring(1).toLowerCase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nombre",nombreEjemplarUpper);
        contentValues.put("peso",peso);
        contentValues.put("id_especie",id_especie);
        contentValues.put("id_estado",id_estado);
        contentValues.put("radioCollar",radio);
        contentValues.put("id_genero",id_genero);
        String[] args = new String []{id_ejemplar};
        long result = MyDB.update("ejemplar", contentValues, "id=?", args);
        if(result==-1){
            return false;
        }else {
            return true;
        }
    }


    public Boolean insertarProcedimiento(String nombre,int id_ejemplar,Context context){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String nombreProceUpper = nombre.substring(0, 1).toUpperCase() + nombre.substring(1).toLowerCase();
        SessionManagement sessionManagement = new SessionManagement(context);
        int userID = sessionManagement.getSession();
        contentValues.put("nombre",nombreProceUpper);
        contentValues.put("id_ejemplar",id_ejemplar);
        contentValues.put("id_user",userID);
        long result = MyDB.insert("procedimiento",null,contentValues);
        if(result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Boolean insertarNota(String nombre,int id_procedimiento,Context context){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String nombreNotaUpper = nombre.substring(0, 1).toUpperCase() + nombre.substring(1).toLowerCase();
        SessionManagement sessionManagement = new SessionManagement(context);
        int userID = sessionManagement.getSession();
        contentValues.put("nombre",nombreNotaUpper);
        contentValues.put("id_procedimiento",id_procedimiento);
        contentValues.put("id_user",userID);
        long result = MyDB.insert("nota",null,contentValues);
        if(result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Boolean checkUserPass(String email,String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from user where email = ? and password = ?",new String[] {email,password});
        if(cursor.getCount()>0){
            return true;
        }else {
            return false;
        }
    }

}
