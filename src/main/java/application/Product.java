package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Product {
    private String productId;
    private String name;
    private double price;
    private String imgSrc;

    private Image img;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public Image getImg() {return img;}

    public Product() {}
    public Product(String name, double price, String imgSrc) {
        this.name = name;
        this.price = price;
        this.img = new Image(Objects.requireNonNull(Main.class.getResourceAsStream(imgSrc)));
    }
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
