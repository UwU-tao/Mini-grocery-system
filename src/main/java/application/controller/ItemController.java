package application.controller;

import application.Main;
import application.utils.DBUtils;
import application.Product;
import application.main.MyListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.Objects;

public class ItemController {
    public static String CURRENCY = "$";
    @FXML
    private ImageView img;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLable;

    @FXML
    private void click(MouseEvent mouseEvent) {
        myListener.onClickListener(product);
    }

    private Product product;
    private MyListener myListener;


    public void setData(Product product, MyListener myListener) {
        this.myListener = myListener;
        this.product = product;
        this.nameLabel.setText(product.getName());
        this.priceLable.setText(CURRENCY + product.getPrice());
        Image image = new Image(Objects.requireNonNull(Main.class.getResourceAsStream(product.getImgSrc())));
        this.img.setImage(image);
    }
}
