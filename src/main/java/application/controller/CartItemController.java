package application.controller;

import application.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class CartItemController {
    @FXML
    private ImageView img;

    @FXML
    private Label product;

    @FXML
    private Label quantity;

    @FXML
    private Label totalprice;

    @FXML
    private Label unitprice;

    private Product p;

    public void setData(Product p) {
        this.p = p;
        this.product.setText(p.getName());
        this.unitprice.setText("$" + p.getPrice());
        this.quantity.setText("");
        this.totalprice.setText("$" + p.getPrice());
        this.img.setImage(p.getImg());
    }
}
