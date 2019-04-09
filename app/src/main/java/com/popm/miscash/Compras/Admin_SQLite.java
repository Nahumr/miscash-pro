package com.popm.miscash.Compras;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.popm.miscash.Carritoc.CarritoItem;

import java.util.ArrayList;
import java.util.List;

public class Admin_SQLite extends SQLiteOpenHelper {


    private static final String DB_NAME = "Detalles";
    private static final int DB_VERSION = 1;
    private static final String query = "CREATE TABLE Detalles (producto_cb varchar (13) primary key, " +
            "producto_cantidad integer, producto_nombre varchar(50), producto_precio float, producto_vuelto float, tienda int );";

    private static  final String drop = "DROP TABLE IF EXISTS "+DB_NAME;

    public Admin_SQLite(Context context) {
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


    public void agregar(String cb, String producto_nombre,float producto_precio,float producto_vuelto,int producto_cantidad, int tienda){

        SQLiteDatabase db = getWritableDatabase();

        if (db!=null){
            ContentValues agregar = new ContentValues();

            agregar.put("producto_cb",cb);
            agregar.put("producto_cantidad",producto_cantidad);
            agregar.put("producto_nombre",producto_nombre);
            agregar.put("producto_precio",producto_precio);
            agregar.put("producto_vuelto",producto_vuelto);
            agregar.put("tienda",tienda);
            db.insert("Detalles",null,agregar);
            db.close();
        }
    }

    public List<CarritoItem> datoItems (){
        List <CarritoItem> datos = new ArrayList<CarritoItem>();

        SQLiteDatabase db =  getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Detalles",null);

            if (cursor.moveToFirst()){
                do{
                    String cb = cursor.getString(cursor.getColumnIndex("producto_cb"));
                    String nombre = cursor.getString(cursor.getColumnIndex("producto_nombre"));
                    Float precio = cursor.getFloat(cursor.getColumnIndex("producto_precio"));
                    Float vuelto = cursor.getFloat(cursor.getColumnIndex("producto_vuelto"));
                    int cantidad = cursor.getInt(cursor.getColumnIndex("producto_cantidad"));

                    datos.add(new CarritoItem(nombre,cb,cantidad,precio,vuelto));
                }while (cursor.moveToNext());
            }

        return datos;
    }

    public float granTotal (){
        float ttotal=0;
        SQLiteDatabase db =  getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT (producto_precio*producto_cantidad) as total FROM Detalles",null);

        if (cursor.moveToFirst()){
            do{
                ttotal+= cursor.getFloat(cursor.getColumnIndex("total"));

            }while (cursor.moveToNext());
        }

        return ttotal;
    }

    public float totalVuelto (){
        float ttotal=0;
        SQLiteDatabase db =  getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT (producto_vuelto*producto_cantidad) as total FROM Detalles",null);

        if (cursor.moveToFirst()){
            do{
                ttotal += cursor.getFloat(cursor.getColumnIndex("total"));

            }while (cursor.moveToNext());
        }

        return ttotal;
    }


    public void actualizar (String cb,int producto_cantidad ){

        SQLiteDatabase db = getWritableDatabase();

        if (db!=null){
            db.execSQL("UPDATE Detalles SET producto_cantidad="+String.valueOf(producto_cantidad)+" WHERE producto_cb='"+cb+"'");
        }
    }

    public void eliminar (String cb){

        SQLiteDatabase db = getWritableDatabase();

        if (db!=null){
            db.execSQL("DELETE FROM Detalles WHERE producto_cb= '"+cb+"'");
        }
    }

    public void drop (){
        SQLiteDatabase db = getWritableDatabase();
        if (db!=null){
            db.execSQL("DELETE from Detalles ");
        }
    }

    public int registros (){
        SQLiteDatabase db =  getReadableDatabase();
        return (int ) DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM Detalles", null);
    }

}
