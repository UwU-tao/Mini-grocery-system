package application.controller;

import application.Main;
import application.Product;
import application.main.App;
import application.main.MyListener;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
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
    ObservableList<String> list = FXCollections.observableArrayList("Profile", "My Cart");
    private List<Product> products;

    private List<Product> getProducts() {
        List<Product> productList = new ArrayList<>();

        Product product;

        product = new Product();
        product.setName("Cookie");
        product.setPrice(1.99);
        product.setImgSrc("Img/cookie.jpg");
        product.setColor("80080C");
        productList.add(product);

        product = new Product();
        product.setName("Ice cream");
        product.setPrice(3.99);
        product.setImgSrc("Img/ice_cream.jpg");
        product.setColor("6A7324");
        productList.add(product);

        return productList;
    }

    private void setChosenCard(Product product) {
        label.setText(product.getName());
        price.setText(App.CURRENCY + product.getPrice());
        Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream(product.getImgSrc())));
        img.setImage(image);
        chosenCard.setStyle("-fx-background-color: #" + product.getColor() + ";\n"
                + "-fx-background-radius: 30;");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        products = new ArrayList<>(getProducts());
        if (products.size() > 0) {
            setChosenCard(products.get(0));
            myListener = new MyListener() {
                @Override
                public void onClickListener(Product product) {
                    setChosenCard(product);
                }
            };
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
    }

    private void doAction(ActionEvent event, String action) {
        switch (action) {
            case "Profile":
                DBUtils.changeScene(event, "SignUpUI.fxml", "test");
                break;

            case "My Cart":

                break;
        }
    }
}
