package com.popm.miscash.Tiendas;

public class Tienda {


    int id_tienda;
    float calif;
    String nombre;
    float latitud_x ,latitud_y;


    Tienda(){

    }

    public Tienda(int id_tienda, float calif, String nombre) {
        this.id_tienda = id_tienda;
        this.calif = calif;
        this.nombre = nombre;
    }

    public Tienda(int id_tienda, float calif, String nombre, float latitud_x, float latitud_y) {
        this.id_tienda = id_tienda;
        this.calif = calif;
        this.nombre = nombre;
        this.latitud_x = latitud_x;
        this.latitud_y = latitud_y;
    }



    public float getCalif() {
        return calif;
    }

    public float getLatitud_x() {
        return latitud_x;
    }

    public float getLatitud_y() {
        return latitud_y;
    }

    public int getId_tienda() {
        return id_tienda;
    }

    public String getNombre() {
        return nombre;
    }


}
