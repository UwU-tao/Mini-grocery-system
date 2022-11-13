package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
public class AppController implements Initializable {
    @FXML
    private Pane chosenCard;
    @FXML
    private Label label, price;
    @FXML
    private ImageView img;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane gridPane;
    private int totalProducts;

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    private List<Product> products;

    private List<Product> getProducts() {
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();

            product.setName("Cookie");
            product.setPrice(1.99);
            product.setImgSrc("Img/cookie.jpg");
            product.setColor("0000");

            productList.add(product);
        }
        return productList;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        products = new ArrayList<>(getProducts());
        getProducts();
        int col = 0, row = 1;
        try {
            for (int i = 0; i < products.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setData(products.get(i));

                if (col == 2) {
                    col = 0;
                    row++;
                }
                gridPane.add(anchorPane, col++, row);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
