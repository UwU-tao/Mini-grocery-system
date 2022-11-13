package application.controller;

import application.Main;
import application.Product;
import application.main.App;
import application.main.MyListener;
import application.utils.Account;
import application.utils.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AppController implements Initializable {
    @FXML
    private Pane chosenCard;
    @FXML
    private Label label, price;
    @FXML
    private ImageView img;
    @FXML
    private GridPane gridPane;
    private MyListener myListener;
    @FXML
    private ComboBox comboBox;
    @FXML
    private Button addButton;
    @FXML
    private TextField quantity;
    ObservableList<String> list = FXCollections.observableArrayList("Profile", "My Cart");
    private List<Product> products = new ArrayList<>();

    private void getProducts() {
//        List<Product> productList = new ArrayList<>();
//
//        Product product;
//
//        product = new Product();
//        product.setName("Cookie");
//        product.setPrice(1.99);
//        product.setImgSrc("Img/cookie.jpg");
//        product.setColor("80080C");
//        productList.add(product);
//
//        product = new Product();
//        product.setName("Ice cream");
//        product.setPrice(2.99);
//        product.setImgSrc("Img/ice_cream.jpg");
//        product.setColor("6A7324");
//        productList.add(product);
        products.clear();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root", "koroseen");
            preparedStatement = connection.prepareStatement("select * from product");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                products.add(new Product(resultSet.getString(2), Double.parseDouble(resultSet.getString(4)), "Img/cookie.jpg"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setChosenCard(Product product) {
        label.setText(product.getName());
        price.setText(String.valueOf(product.getPrice()));
//        Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream(product.getImgSrc())));
        img.setImage(product.getImg());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        products = new ArrayList<>(getProducts());
        getProducts();
        if (products.size() > 0) {
            setChosenCard(products.get(0));
            myListener = product -> setChosenCard(product);
        }
        int col = 0, row = 1;
        try {
            for (Product product : products) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(Main.class.getResource("Item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(product, myListener);

                if (col == 2) {
                    col = 0;
                    row++;
                }
                gridPane.add(anchorPane, col++, row);

                //set grid width
                gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridPane.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(15));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        comboBox.setItems(list);
        comboBox.setOnAction((EventHandler<ActionEvent>) event -> doAction(event, comboBox.getValue().toString()));

        addButton.setOnAction(event -> {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root", "koroseen");
                Statement s = connection.createStatement();
                ResultSet r = s.executeQuery("SELECT COUNT(*) AS recordCount FROM login.order");
                r.next();
                int count = r.getInt("recordCount");
                r.close();
                preparedStatement = connection.prepareStatement("insert into login.order values (?, ?, ?, ?, ?)");
                preparedStatement.setString(1, String.valueOf(count));
                preparedStatement.setString(2, Account.getAccountId());
                preparedStatement.setString(3, label.getText());
                preparedStatement.setInt(4, Integer.parseInt(quantity.getText()));
                preparedStatement.setDouble(5, Double.parseDouble(price.getText()));
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void doAction(ActionEvent event, String action) {
        switch (action) {
            case "Profile":
                DBUtils.changeScene(event, "SignUpUI.fxml", "test");
                break;
            case "My Cart":
                DBUtils.changeScene(event, "Cart.fxml", "My Cart");
                break;
        }
    }
}
