package com.popm.miscash.Tiendas;

public class Compra {


    int id_tienda;
    int id_ticket;
    float calif;
    String nombre;
    String monto;

    public Compra(int id_tienda, int id_ticket, float calif, String nombre, String monto) {
        this.id_tienda = id_tienda;
        this.id_ticket = id_ticket;
        this.calif = calif;
        this.nombre = nombre;
        this.monto = monto;
    }

    public String getMonto() {
        return monto;
    }

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


}
