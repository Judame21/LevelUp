package com.jdcode.levelup;

public class Product {
    private String id;
    private String nombre;
    private String  imageUrl;
    private double precio;
    private String descripcion;
    private int cantidad;

    public Product(){

    }

    public Product(String id, String nombre, String imageUrl, double precio, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.imageUrl = imageUrl;
        this.precio = precio;
        this.descripcion = descripcion;
        this.cantidad = 1;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imagenUrl) {
        this.imageUrl = imagenUrl;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
