package application.controller.admin;

import application.Main;
import application.utils.DataSource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class AddProductsListController implements Initializable {
    @FXML
    private Button addProduct;

    @FXML
    private Button back;

    @FXML
    private Button clearList;

    @FXML
    private Button importBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        back.setOnAction(event -> {
            DataSource.getInstance().fxmlLoader(event, "Admin/dashboard.fxml");
        });

        addProduct.setOnAction(event -> {
            try {
                Parent parent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Admin/addproduct.fxml")));
                Stage newStage = new Stage();
                Scene scene = new Scene(parent);
                newStage.setTitle("Add Product");
                newStage.setScene(scene);
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.initOwner(addProduct.getScene().getWindow());
                newStage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResource("Img/add-product.png")).openStream()));
//                newStage.initStyle(StageStyle.UNDECORATED);
                newStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
