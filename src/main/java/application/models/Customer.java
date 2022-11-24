package application.models;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int userid;
    private String username;
    private String password;
    private String email;
    private int orderscount;
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

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

    public int getOrderscount() {
        return orderscount;
    }

    public void setOrderscount(int orderscount) {
        this.orderscount = orderscount;
    }
}
