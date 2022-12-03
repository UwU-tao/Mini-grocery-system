package application.controller.admin;

import application.models.Order;
import application.utils.DataSource;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Orders implements Initializable {
    @FXML
    private Button back;

    @FXML
    private TableView<Order> details;

    @FXML
    private TableColumn<Order, String> orderid;

    @FXML
    private TableColumn<Order, String> userid;

    @FXML
    private TableColumn<Order, String> orderdate;

    public void showDetails() {
        orderid.setCellValueFactory(new PropertyValueFactory<>("orderid"));
        userid.setCellValueFactory(new PropertyValueFactory<>("userid"));
        orderdate.setCellValueFactory(new PropertyValueFactory<>("orderdate"));
        details.setItems(FXCollections.observableArrayList(DataSource.getInstance().getDetails()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showDetails();

        back.setOnAction(event -> {
            DataSource.getInstance().fxmlLoader(event, "Admin/dashboard.fxml");
        });
    }
}
