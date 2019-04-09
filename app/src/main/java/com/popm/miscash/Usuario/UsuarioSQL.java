package com.popm.miscash.Usuario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.popm.miscash.Carritoc.CarritoItem;

import java.util.LinkedList;

public class UsuarioSQL extends SQLiteOpenHelper {

    private static final String DB_NAME = "Usuario";
    private static final int DB_VERSION = 1;

    private static final String query = "CREATE TABLE USUARIO ( " +
                                            " nombre VARCHAR (40) ," +
                                            " apellido VARCHAR (40) ," +
                                            " correo VARCHAR (40) NOT NULL UNIQUE primary key)";

    private static  final String drop = "DROP TABLE IF EXISTS "+DB_NAME;


    public UsuarioSQL(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(drop);
        db.execSQL(query);
    }

    public int campos (){
        SQLiteDatabase db =  getReadableDatabase();
        return (int ) DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM usuario", null);
    }

    public void agregar(String correo, String nombre,String apellido){
        SQLiteDatabase db = getWritableDatabase();

        if (db!=null){
            ContentValues agregar = new ContentValues();

            agregar.put("nombre",nombre);
            agregar.put("apellido_p",apellido);
            agregar.put("correo",correo);
            db.close();
        }
    }

    public void agregar(Usuario usuario){
        SQLiteDatabase db = getWritableDatabase();

        if (db!=null){
            ContentValues agregar = new ContentValues();
            agregar.put("correo",usuario.getCorreo());
            agregar.put("nombre",usuario.getNombre());
            agregar.put("apellido",usuario.getApellido());
            db.insert("USUARIO",null,agregar);
            db.close();
        }
    }

    public String nombreApellido (){

        String nombre="";

        SQLiteDatabase db =  getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT nombre,apellido  FROM usuario",null);

        if (cursor.moveToFirst()){
            do{
                nombre = cursor.getString(cursor.getColumnIndex("nombre"))+" "+
                        cursor.getString(cursor.getColumnIndex("apellido"));


            }while (cursor.moveToNext());
        }

        return nombre;

    }

    public String nombre (){

        String nombre ="";
        SQLiteDatabase db =  getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT nombre FROM usuario",null);

        if (cursor.moveToFirst()){
            do{
                nombre = cursor.getString(cursor.getColumnIndex("nombre"))+" ";


            }while (cursor.moveToNext());
        }
        return  nombre;
    }

    public String correo (){

        String mail ="";
        SQLiteDatabase db =  getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT correo FROM usuario",null);

        if (cursor.moveToFirst()){
            do{
                mail = cursor.getString(cursor.getColumnIndex("correo"))+" ";


            }while (cursor.moveToNext());
        }
        return  mail;
    }

    public void drop (){
        SQLiteDatabase db = getWritableDatabase();
        if (db!=null){
            db.execSQL("DELETE from usuario ");
        }
    }

}
