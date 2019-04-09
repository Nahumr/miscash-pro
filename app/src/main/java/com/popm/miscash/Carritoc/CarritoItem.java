package com.popm.miscash.Carritoc;

public class CarritoItem {



    String producto_nombre, producto_cb;
    int   producto_cantidad;
    float producto_precio, producto_vuelto;

    public void setProducto_nombre(String producto_nombre) {
        this.producto_nombre = producto_nombre;
    }

    public void setProducto_cb(String producto_cb) {
        this.producto_cb = producto_cb;
    }

    public void setProducto_cantidad(int producto_cantidad) {
        this.producto_cantidad = producto_cantidad;
    }

    public void setProducto_precio(float producto_precio) {
        this.producto_precio = producto_precio;
    }

    public void setProducto_vuelto(float producto_vuelto) {
        this.producto_vuelto = producto_vuelto;
    }

    public String getProducto_nombre() {
        return producto_nombre;
    }

    public String getProducto_cb() {
        return producto_cb;
    }

    public int getProducto_cantidad() {
        return producto_cantidad;
    }

    public float getProducto_precio() {
        return producto_precio;
    }

    public float getProducto_vuelto() {
        return producto_vuelto;
    }

    public CarritoItem(String producto_nombre, String producto_cb, int producto_cantidad, float producto_precio, float producto_vuelto) {
        this.producto_nombre = producto_nombre;
        this.producto_cb = producto_cb;
        this.producto_cantidad = producto_cantidad;
        this.producto_precio = producto_precio;
        this.producto_vuelto = producto_vuelto;
    }
}
