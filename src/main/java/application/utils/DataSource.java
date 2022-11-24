package application.utils;

import application.Main;
import application.controller.UserController;
import application.models.Categories;
import application.models.Customer;
import application.models.Product;
import application.models.User;
import javafx.css.Match;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader;
    }

    public FXMLLoader windowPopUp(Window window, String path) {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource(path)));
        try {
            Parent parent = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(parent);
            newStage.setTitle("Add Product");
            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.initOwner(window);
            newStage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResource("Img/add-product.png")).openStream()));
//                newStage.initStyle(StageStyle.UNDECORATED);
            newStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public Product getOneProduct(int id) {
        Product product = new Product();
        try {
            PreparedStatement ps = con.prepareStatement("select * from login.products where productid = "+id+"");
            ResultSet rs = ps.executeQuery();
            product.setProductid(rs.getInt("productid"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            product.setExpireddate(rs.getDate("expireddate"));
            product.setQuantity(rs.getInt("quantity"));
            product.setCategoryid(rs.getInt("categoryid"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public Product getOneProduct(String namee) {
        Product product = new Product();
        try {
            PreparedStatement ps = con.prepareStatement("select * from login.products where name = ?");
            ps.setString(1, namee);
            ResultSet rs = ps.executeQuery();
            product.setProductid(rs.getInt("productid"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            product.setExpireddate(rs.getDate("expireddate"));
            product.setQuantity(rs.getInt("quantity"));
            product.setCategoryid(rs.getInt("categoryid"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
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

    public boolean addProduct(int categoryid, String namee, double price, Date expirationDate, int quantity) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String datee = dateFormat.format(expirationDate);
        System.out.println(datee);

        try {
            PreparedStatement ps = con.prepareStatement("insert into login.products(categoryid, name, price, expireddate, quantity, sales) values ("+categoryid+", " + "'" + namee + "'" + ", " + price  + ", " + "'" + datee + "', " + quantity + ", " + 0 + ")");

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getCategoryId(String namee) {
        try {
            int id = 0;
            PreparedStatement ps = con.prepareStatement("select * from login.categories where name = ? limit 1");
            ps.setString(1, namee);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getCategoryName(int id) {
        try {
            PreparedStatement ps = con.prepareStatement("select * from login.categories where categoryid = "+id+"");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteProduct(int id) {
        try {
            PreparedStatement ps = con.prepareStatement("delete from login.products where productid = "+id+"");
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select users.*,  (select count(distinct orders.orderid) from login.orders where users.userid = orders.userid) orderscount from login.users where admin = 0");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer();

                customer.setUserid(rs.getInt("userid"));
                customer.setUsername(rs.getString("username"));
                customer.setPassword(rs.getString("password"));
                customer.setEmail(rs.getString("email"));
                customer.setOrderscount(rs.getInt("orderscount"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customers;
    }

    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select * from login.categories");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Categories cate = new Categories();
                cate.setCategoryid(rs.getInt(1));
                cate.setName(rs.getString(2));
                categories.add(cate.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
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
