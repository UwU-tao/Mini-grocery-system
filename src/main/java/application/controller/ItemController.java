package application;

import application.main.MyListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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


    public void setData(Product product) {
        this.product = product;
        this.nameLabel.setText(product.getName());
        this.priceLable.setText(CURRENCY + product.getPrice());
        Image image = new Image(getClass().getResourceAsStream(product.getImgSrc()));
        this.img.setImage(image);
    }
}
