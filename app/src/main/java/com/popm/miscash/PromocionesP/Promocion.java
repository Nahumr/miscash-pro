package com.popm.miscash.PromocionesP;

public class Promocion {

    String nombre,codigo,precio,descripcion;

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getPrecio() {
        return precio;
    }


    public Promocion(String nombre, String precio, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }
}
