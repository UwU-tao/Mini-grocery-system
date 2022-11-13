package application.controller;

import application.Main;
import application.Product;
import application.main.MyListener;
import application.utils.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CartController implements Initializable {
    @FXML
    private GridPane grid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int row = 1, col = 0;

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root", "koroseen");
            ps = connection.prepareStatement("select  * from login.order");
            rs = ps.executeQuery();
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(Main.class.getResource("CartItem.fxml"));
                HBox hBox = fxmlLoader.load();
                CartItemController controller = fxmlLoader.getController();
                controller.setData(new Product(rs.getString(3), Double.parseDouble(rs.getString(5)), "Img/cookie.jpg"));
                grid.add(hBox, col, row++);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}