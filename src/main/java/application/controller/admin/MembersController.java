package application.controller.admin;

import application.models.Customer;
import application.utils.DataSource;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MembersController implements Initializable {
    @FXML
    private Button back;

    @FXML
    private TableColumn<Customer, String> countCol;

    @FXML
    private TableColumn<Customer, String> emailCol;

    @FXML
    private TableView<Customer> memTable;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<Customer, String> usernameCol;

    public void showMembers() {
        addActions();
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("orderscount"));
        memTable.setItems(FXCollections.observableArrayList(DataSource.getInstance().getCustomers()));
    }

    private void addActions() {
        TableColumn<Customer, String> id = new TableColumn("#");
        id.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(memTable.getItems().indexOf(p.getValue()) + ""));
        id.setSortable(false);

        TableColumn actions = new TableColumn<>("Actions");
        actions.setMinWidth(140);

        Callback<TableColumn<Customer, Void>, TableCell<Customer, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Customer, Void> call(TableColumn<Customer, Void> param) {
                return new TableCell<>() {
                    private final Button delete = new Button("Delete");

                    {
                        delete.setOnAction(event -> {
                            Customer customer = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            alert.setHeaderText("Are you sure you want to delete User: " + customer.getUsername());

                            Optional<ButtonType> confirm = alert.showAndWait();
                            if (confirm.get() == ButtonType.OK) {
                                if (DataSource.getInstance().deleteCustomer(customer.getUsername())) {
                                    getTableView().getItems().remove(getIndex());
                                }
                            }
                        });
                    }

                    private final HBox buttonsPane = new HBox();

                    {
                        buttonsPane.setAlignment(Pos.CENTER);
                        buttonsPane.setSpacing(5);
                        buttonsPane.getChildren().add(delete);
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttonsPane);
                        }
                    }
                };
            }
        };
        actions.setCellFactory(cellFactory);

        memTable.getColumns().add(0, id);
        memTable.getColumns().add(actions);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        back.setOnAction(event -> {
            DataSource.getInstance().fxmlLoader(event, "Admin/dashboard.fxml");
        });

    }
}
