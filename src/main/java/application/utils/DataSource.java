package application.utils;

import application.Main;
import application.controller.UserController;
import application.models.Product;
import application.models.User;
import javafx.css.Match;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSource extends Product {
    private Connection con;

    private final String URL = "jdbc:mysql://localhost:3306/login";

    private static DataSource instance;


    private DataSource() {
    }

    public static DataSource getInstance() {
        if (instance == null) {
            synchronized (DataSource.class) {
                if (instance == null) {
                    instance = new DataSource();
                }
            }
        }
        return instance;
    }

    public boolean open() {
        try {
            con = DriverManager.getConnection(URL, "root", "koroseen");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public FXMLLoader fxmlLoader(ActionEvent event, String path) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(path));
        try {
            Parent root = loader.load();
            Node node =  (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader;
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        try {
            String query = "select p.productid, p.name, p.price, c.name, p.categoryid, p.quantity, p.sales, p.expireddate from login.products p left join login.categories c on p.categoryid = c.categoryid";
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(query);

            while (rs.next()) {
                Product product = new Product();

                product.setProductid(rs.getInt(1));
                product.setName(rs.getString(2));
                product.setPrice(rs.getDouble(3));
                product.setCategory(rs.getString(4));
                product.setCategoryid(rs.getInt(5));
                product.setQuantity(rs.getInt(6));
                product.setSales(rs.getDouble(7));
                product.setExpireddate(rs.getDate(8));

                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public void changePass(String newPass) {
        String query = "update users set password = ?, salt = ? where username = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            String salt = PasswordUtils.getSaltvalue(20);
            ps.setString(1, PasswordUtils.generateSecuredPass(newPass, salt));
            ps.setString(2, salt);
            ps.setString(3, UserController.getUsername());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addNewUser(String username, String password, String email, String salt) {
        try {
            PreparedStatement ps = con.prepareStatement("insert into login.users(username, password, email, admin, salt) values (?, ?, ?, 0, ?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4, salt);

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public User getUserByUsername(String username) {
        User user = new User();
        try {
            PreparedStatement ps = con.prepareStatement("select  * from login.users where username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user.setUserid(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setAdmin(rs.getInt(5));
                user.setSalt(rs.getString(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void addProduct(String name, double price, Date expirationDate, int quantity) {

    }
    public boolean validUsername(String username) {
        Matcher matcher = Pattern.compile("^(?=.{5,})(?![._])(?!.*[._]{2})[A-Za-z0-9]+(?<![._])$", Pattern.CASE_INSENSITIVE).matcher(username);
        return matcher.find();
    }

    public boolean validPassword(String password) {
        Matcher matcher = Pattern.compile("^(?=.{5,})[A-Za-z0-9-_@]+$", Pattern.CASE_INSENSITIVE).matcher(password);
        return matcher.find();
    }

    public boolean validEmail(String email) {
        Matcher matcher = Pattern.compile("^(?=.{4,16}@)[^0-9_-][A-Za-z0-9_-]+@[^-_][A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z0-9]{2,})$", Pattern.CASE_INSENSITIVE).matcher(email);
        return matcher.find();
    }
}
