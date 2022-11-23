package application.controller.admin;

import application.models.Product;
import application.utils.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductsListController implements Initializable {
    @FXML
    private Button back;
    @FXML
    private TableView productsTable;

    @FXML
    private TableColumn<Product, String> id;
    @FXML
    private TableColumn<Product, String> categoryCol;

    @FXML
    private TableColumn<Product, String> dateCol;

    @FXML
    private TableColumn<Product, String> nameCol;

    @FXML
    private TableColumn<Product, String> priceCol;

    @FXML
    private TableColumn<Product, String> quantityCol;

    @FXML
    private TableColumn<Product, String> salesCol;

    public void showProducts() {
        id.setCellValueFactory(new PropertyValueFactory<>("productid"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("expireddate"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
//        salesCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productsTable.setItems(FXCollections.observableArrayList(DataSource.getInstance().getProducts()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        back.setOnAction(event -> {
            DataSource.getInstance().fxmlLoader(event, "Admin/dashboard.fxml");
        });
    }
}
