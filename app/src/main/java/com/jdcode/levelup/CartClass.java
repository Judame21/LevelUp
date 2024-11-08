package com.jdcode.levelup;

import java.util.ArrayList;
import java.util.List;

public class CartClass {
    private List<Product> productList;

    public CartClass() {
        productList = new ArrayList<>();
    }
    public void addProduct(Product product) {
        productList.add(product);
    }
    public List<Product> getProducts() {
        return productList;
    }
    public int getTotalItems() {
        return productList.size();
    }



}
