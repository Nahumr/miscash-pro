package com.popm.miscash.Tiendas;

public class Compra {


    int id_tienda;
    int id_ticket;
    float calif;
    String nombre;

    public int getId_tienda() {
        return id_tienda;
    }

    public int getId_ticket() {
        return id_ticket;
    }

    public float getCalif() {
        return calif;
    }

    public String getNombre() {
        return nombre;
    }

    public Compra(int id_tienda, int id_ticket, float calif, String nombre) {
        this.id_tienda = id_tienda;
        this.id_ticket = id_ticket;
        this.calif = calif;
        this.nombre = nombre;
    }
}
