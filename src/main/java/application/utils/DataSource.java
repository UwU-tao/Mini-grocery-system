package application.utils;

import application.Main;
import application.controller.UserController;
import application.models.*;
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
import java.util.*;
import java.util.Date;
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
            PreparedStatement ps = con.prepareStatement("select * from login.products where productid = " + id + "");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                product.setProductid(rs.getInt("productid"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setExpireddate(rs.getDate("expireddate"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCategoryid(rs.getInt("categoryid"));

                PreparedStatement pss = con.prepareStatement("select * from login.categories where categoryid = "+product.getCategoryid()+"");
                ResultSet rss = pss.executeQuery();
                if (rss.next())  product.setCategory(rss.getString(2));
            }
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
            if (rs.next()) {
                product.setProductid(rs.getInt("productid"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setExpireddate(rs.getDate("expireddate"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCategoryid(rs.getInt("categoryid"));
            }
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
            ps.setString(3, UserController.getInstance().getUsername());

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

    public boolean deleteCustomer(String username) {
        try {
            PreparedStatement ps = con.prepareStatement("delete from login.users where username = '" + username + "'");
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByUsername(String username) {
        User user = new User();
        try {
            PreparedStatement ps = con.prepareStatement("select * from login.users where username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user.setUserid(rs.getInt(1));
                user.setUsername(rs.getString(2));
                user.setPassword(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setAdmin(rs.getInt(5));
                user.setSalt(rs.getString(6));

                int tmpp = rs.getInt(1);
                PreparedStatement pss = con.prepareStatement("select * from login.cart where userid = ?");
                pss.setInt(1, tmpp);
                ResultSet rss = pss.executeQuery();
                List<Product> prod = new ArrayList<>();
                while (rss.next()) {
                    int tmp = rss.getInt(1);
                    Product pr = getOneProduct(tmp);
                    pr.setQuantity(rss.getInt(2));
                    prod.add(pr);
                }
                user.setProducts(prod);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void deleteFromCart(int productid) {
        try {
            PreparedStatement ps = con.prepareStatement("delete from login.cart where productid = "+productid+"");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getLastIdx() {
        try {
            PreparedStatement ps = con.prepareStatement("select * from login.orders where orderid = (select max(orderid) from login.orders)");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("orderid");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int updateIdx() {
        int res = 0;
        try {
            PreparedStatement pss = con.prepareStatement("set foreign_key_checks = 0");
            pss.executeUpdate();

            res = getLastIdx();
            PreparedStatement ps = con.prepareStatement("update login.cart set orderid = "+res+" where userid = "+UserController.getInstance().getUserid()+"");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean recordOrder() {
        try {
            PreparedStatement ps = con.prepareStatement("insert into login.orders(orderid, userid, orderdate) values (?, ?, ?)");
            ps.setInt(1, updateIdx() + 1);
            ps.setInt(2, UserController.getInstance().getUserid());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            ps.setString(3, dateFormat.format(Calendar.getInstance().getTime()));
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeProduct(int productid, int quantity) {
        try {
            PreparedStatement pss = con.prepareStatement("select * from login.products where productid = "+productid+"");
            ResultSet rss = pss.executeQuery();

            int tmp = 0;
            if (rss.next()) tmp = rss.getInt("quantity") - quantity;
            PreparedStatement ps = con.prepareStatement("update login.products set quantity = "+tmp+" where productid = "+productid+"");
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void translateData() {
        List<Product> products = UserController.getInstance().getProducts();
        int id = UserController.getInstance().getUserid();
        if (products.size() > 0) {
            try {
                PreparedStatement pss = con.prepareStatement("set foreign_key_checks = 0");
                pss.executeUpdate();

                PreparedStatement ps = con.prepareStatement("delete from login.cart where userid = " + id + "");
                ps.executeUpdate();

                StringBuilder qr = new StringBuilder();
                qr.append("insert into login.cart (productid, quantity, userid, orderid) values ");
                for (int i = 0; i < products.size() - 1; i++) {
                    qr.append("(" + products.get(i).getProductid() + "," + products.get(i).getQuantity() + "," + id + "," + getLastIdx() + "),");
                }
                qr.append("(" + products.get(products.size() - 1).getProductid() + "," + products.get(products.size() - 1).getQuantity() + "," + id + "," + getLastIdx() + ")");
                PreparedStatement pe = con.prepareStatement(qr.toString());
                pe.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Order> getDetails() {
        List<Order> orders = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select * from login.orders");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order order = new Order();

                order.setOrderid(rs.getInt("orderid"));
                order.setUserid(rs.getInt("userid"));
                order.setOrderdate(rs.getDate("orderdate"));

                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public boolean checkProduct(String namee) {
        try {
            PreparedStatement ps = con.prepareStatement("select * from login.products where name = ?");
            ps.setString(1, namee);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void importProducts(List<Product> products) {
        if (products.size() > 0) {
            try {
                PreparedStatement ps2 = con.prepareStatement("update login.products set quantity = ? where name = ?");

                for (Product product : products) {
                    if (checkProduct(product.getName())) {
                        ps2.setString(1, String.valueOf(getOneProduct(product.getName()).getQuantity() + product.getQuantity()));
                        ps2.setString(2, product.getName());
                        ps2.executeUpdate();
                    } else {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String datee = dateFormat.format(product.getExpireddate());
                        PreparedStatement ps = con.prepareStatement("insert into login.products(categoryid, name, price, expireddate, quantity, sales) values (" + product.getCategoryid() + ", " + "'" + product.getName() + "'" + ", " + product.getPrice() + ", " + "'" + datee + "', " + product.getQuantity() + ", " + 0 + ")");
                        ps.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean addProduct(int categoryid, String namee, double price, Date expirationDate, int quantity) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String datee = dateFormat.format(expirationDate);

        try {
            PreparedStatement ps = con.prepareStatement("insert into login.products(categoryid, name, price, expireddate, quantity, sales) values (" + categoryid + ", " + "'" + namee + "'" + ", " + price + ", " + "'" + datee + "', " + quantity + ", " + 0 + ")");

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

    public boolean updateProduct(int srcId, String desName, double pricee, String categoryy, int quantityy) {
        try {
            PreparedStatement ps = con.prepareStatement("update login.products set products.name = ?, products.price = ?, products.quantity = ?, products.categoryid = ? where products.productid = ?");
            ps.setString(1, desName);
            ps.setDouble(2, pricee);
            ps.setInt(3, quantityy);
            ps.setInt(4, getCategoryId(categoryy));
            ps.setInt(5, srcId);
            ps.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(int id) {
        try {
            PreparedStatement ps = con.prepareStatement("delete from login.products where productid = " + id + "");
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
