package application.utils;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private static String accountId;
    private Cart myCart = new Cart();
    private double balance;
    private List<Payment> payments = new ArrayList<>();
    public Cart getMyCart() {
        return myCart;
    }

    public static String getAccountId() {
        return accountId;
    }

    public static void setAccountId(String accountId) {
        Account.accountId = accountId;
    }
}
