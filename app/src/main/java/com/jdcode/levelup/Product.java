package com.jdcode.levelup;

public class Product {
    private String id;
    private String nombre;
    private String  imageUrl;
    private double precio;

    public Product(){

    }

    public Product(String id, String nombre, String imageUrl, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.imageUrl = imageUrl;
        this.precio = precio;
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
}
