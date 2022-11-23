package application.controller.customers;

import application.Main;
import application.controller.UserController;
import application.models.Product;
import application.utils.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CartController implements Initializable {
    @FXML
    private Label name;

    @FXML
    private TableView<Product> tableView;

    @FXML
    private TableView<Product> cartTableView = new TableView<>();

    @FXML
    private Button back;


    @FXML
    public void showProducts() {
        Task<ObservableList<Product>> getProductTask = new Task<>() {
            @Override
            protected ObservableList<Product> call() {
                return FXCollections.observableArrayList(DataSource.getInstance().getProducts());
            }
        };
        cartTableView.itemsProperty().bind(getProductTask.valueProperty());
//        addActionButtonsToTable();
        new Thread(getProductTask).start();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setText(UserController.getUsername().toUpperCase());
        back.setOnAction(event -> {
            DataSource.getInstance().fxmlLoader(event, "Customer/dashboard.fxml");
        });
    }
}