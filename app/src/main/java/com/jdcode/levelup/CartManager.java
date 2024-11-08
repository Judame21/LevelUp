package com.jdcode.levelup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class CartManager {
    private static CartManager instance;
    private List<Product> cartProducts;

    private CartManager() {
        cartProducts = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addProductToCart(Product product) {
        for (Product p : cartProducts){
            if(p.getNombre().equals(product.getNombre())){
                p.setCantidad(p.getCantidad() + 1);
                return;
            }
        }


        if (product.getCantidad() == 0) {
            product.setCantidad(1); // Establecer la cantidad a 1 si es 0
            cartProducts.add(product);
        }
    }

    public void removeProductFromCart(Product product) {
        Iterator<Product> iterator = cartProducts.iterator();
        while (iterator.hasNext()) {
            Product p = iterator.next();
            if (p.getNombre().equals(product.getNombre())) {
                if (p.getCantidad() > 1) {
                    p.setCantidad(p.getCantidad() - 1);
                } else {
                    iterator.remove();
                }
                break;
            }
        }
    }

    public List<Product> getCartProducts() {
        return cartProducts;
    }

    public double getTotalPrice() {
        double total = 0;
        for (Product product : cartProducts) {
            total += product.getPrecio();
        }
        return total;
    }
}
