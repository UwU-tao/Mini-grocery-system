package application.controller;

import application.models.Product;

import java.util.ArrayList;
import java.util.List;

public class UserController {
    private List<Product> products = new ArrayList<>();
    private int userid;
    private String username;
    private String password;
    private String email;
    private int admin;

    private static UserController instace;

    private UserController() {}

    public static UserController getInstance() {
        if (instace == null) {
            synchronized (UserController.class) {
                if (instace == null) {
                    instace = new UserController();
                }
            }
        }
        return instace;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getUserid() {return userid;}
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }


    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        for (Product p : products) {
            if (p.getProductid() == product.getProductid()) {
                p.setQuantity(p.getQuantity() + 1);
                return;
            }
        }
        product.setQuantity(1);
        products.add(product);
    }

    public void deleteProduct(Product product) {
        for (Product p : products) {
            if (p.getProductid() == product.getProductid()) {
                products.remove(p);
                return;
            }
        }
    }

    public void clearSession() {
        userid = 0;
        username = null;
        password = null;
        email = null;
        admin = 0;
        products.clear();
    }
}
