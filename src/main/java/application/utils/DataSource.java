package application.utils;

import application.models.Product;
import application.models.User;
import javafx.css.Match;

import java.sql.*;
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
