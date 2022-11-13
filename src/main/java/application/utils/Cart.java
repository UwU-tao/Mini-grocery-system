package application.utils;

import application.Product;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
        System.out.println("Added");
    }

    public List<Product> getProducts() {
        return products;
    }
}
