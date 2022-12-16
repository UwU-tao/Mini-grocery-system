package application.controller.customers;

import application.controller.UserController;
import application.models.Product;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CartController implements Initializable {
    @FXML
    private Button back;

    @FXML
    private Button checkout;

    @FXML
    private TableColumn<Product, String> categoryCol;

    @FXML
    private TableColumn<Product, String> dateCol;

    @FXML
    private Label name;

    @FXML
    private TableColumn<Product, String> nameCol;

    @FXML
    private TableColumn<Product, String> priceCol;

    @FXML
    private TableView<Product> cartTable;

    @FXML
    private TableColumn<Product, String> quantityCol;

    @FXML
    private TableColumn<Product, String> salesCol;

    public void showProducts() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("expireddate"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        cartTable.setItems(FXCollections.observableArrayList(UserController.getInstance().getProducts()));
    }

    private void addActions() {
        TableColumn<Product, String> id = new TableColumn<>("#");
        id.setCellValueFactory(product -> new ReadOnlyObjectWrapper<>(cartTable.getItems().indexOf(product.getValue()) + ""));
        id.setSortable(false);

        TableColumn tableColumn = new TableColumn<>("Actions");
        tableColumn.setPrefWidth(200);
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<>() {

            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Delete from cart");

                    {
                        deleteButton.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirm");
                            alert.setHeaderText("Are you sure want to delete " + product.getName() + " from cart?");

                            Optional<ButtonType> confirm = alert.showAndWait();
                            if (confirm.get() == ButtonType.OK) {
                                UserController.getInstance().deleteProduct(product);
                                showProducts();

                                alert.setTitle("Succeed");
                                alert.setHeaderText("Successfully");
                                alert.show();
                            }
                        });
                    }

                    private final HBox buttonsPane = new HBox();

                    {
                        buttonsPane.setAlignment(Pos.CENTER);
                        buttonsPane.setSpacing(5);
                        buttonsPane.getChildren().add(deleteButton);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
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
        tableColumn.setCellFactory(cellFactory);

        cartTable.getColumns().add(0, id);
        cartTable.getColumns().add(tableColumn);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addActions();
        showProducts();

        name.setText(UserController.getInstance().getUsername().toUpperCase());
        back.setOnAction(event -> {
            DataSource.getInstance().fxmlLoader(event, "Customer/dashboard.fxml");
        });

        checkout.setOnAction(event -> {
            if (DataSource.getInstance().recordOrder()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("succeed");
                alert.setHeaderText("Successfully");
                alert.show();

                List<Product> productList = UserController.getInstance().getProducts();
                for (Product p : productList) {
                    DataSource.getInstance().removeProduct(p.getProductid(), p.getQuantity());
                }

                UserController.getInstance().clearProducts();
                showProducts();
            }
        });
    }
}