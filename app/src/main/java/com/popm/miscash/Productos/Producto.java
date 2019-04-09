package com.popm.miscash.Productos;

public class Producto {

    String codigo_barras, nombre;
    float precio,vuelto;
    int tienda;

    public String getCodigo_barras() {
        return codigo_barras;
    }

    public String getNombre() {
        return nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public float getVuelto() {
        return vuelto;
    }

    public int getTienda() {
        return tienda;
    }

    public Producto(String codigo_barras, String nombre, float precio, float vuelto, int tienda) {
        this.codigo_barras = codigo_barras;
        this.nombre = nombre;
        this.precio = precio;
        this.vuelto = vuelto;
        this.tienda = tienda;
    }
}
