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
            DataSource.getInstance().windowPopUp(addProduct.getScene().getWindow(), "Admin/addproduct.fxml");
        });
    }
}
