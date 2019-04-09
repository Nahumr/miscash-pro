package com.popm.miscash.Compras.Tickets;

public class DTicket {

    String nombre;
    Float precio,subtotal;
    int cantidad;

    public String getNombre() {
        return nombre;
    }

    public Float getPrecio() {
        return precio;
    }

    public Float getSubtotal() {
        return subtotal;
    }

    public int getCantidad() {
        return cantidad;
    }

    public DTicket(String nombre, Float precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        subtotal= precio*cantidad;
    }

}
