package application.controller.admin;

import application.models.Customer;
import application.models.Product;
import application.utils.DataSource;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MembersController implements Initializable {
    @FXML
    private Button back;

    @FXML
    private TableColumn<Customer, String> countCol;

    @FXML
    private TableColumn<Customer, String> emailCol;

    @FXML
    private TableColumn<Customer, String> id;

    @FXML
    private TableView<Customer> memTable;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<Product, String> usernameCol;

    public void showMembers() {
        id.setCellValueFactory(new PropertyValueFactory<>("userid"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("orderscount"));
        memTable.setItems(FXCollections.observableArrayList(DataSource.getInstance().getCustomers()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        back.setOnAction(event -> {
            DataSource.getInstance().fxmlLoader(event, "Admin/dashboard.fxml");
        });

    }
}
