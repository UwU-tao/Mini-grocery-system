package application.controller.customers;

import application.controller.UserController;
import application.models.Product;
import application.utils.DataSource;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductsController implements Initializable {
    @FXML
    private Button back;
    @FXML
    private TableView productsTable;

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
        addActions();
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("expireddate"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        productsTable.setItems(FXCollections.observableArrayList(DataSource.getInstance().getProducts()));
    }

    private void addActions() {
        TableColumn<Product, String> id = new TableColumn<>("#");
        id.setCellValueFactory(product -> new ReadOnlyObjectWrapper<>(productsTable.getItems().indexOf(product.getValue()) + ""));
        id.setSortable(false);

        TableColumn tableColumn = new TableColumn<>("Actions");
        tableColumn.setPrefWidth(140);
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<>() {

            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final Button addButton = new Button("Add to cart");

                    {
                        addButton.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            alert.setHeaderText("Are you sure want to add " + product.getName() + " to your cart?");
                            Optional<ButtonType> confirm = alert.showAndWait();

                            if (confirm.get() == ButtonType.OK) {
                                alert.setTitle("Succeed");
                                alert.setHeaderText("Successfully");
                                alert.show();

                                UserController.getInstance().addProduct(product);
                            }
                        });
                    }

                    private final HBox buttonsPane = new HBox();

                    {
                        buttonsPane.setSpacing(5);
                        buttonsPane.getChildren().add(addButton);
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

        productsTable.getColumns().add(0, id);
        productsTable.getColumns().add(tableColumn);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        back.setOnAction(event -> {
            DataSource.getInstance().fxmlLoader(event, "Customer/dashboard.fxml");
        });
    }
}
