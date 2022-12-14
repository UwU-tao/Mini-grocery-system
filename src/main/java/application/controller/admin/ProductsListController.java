package application.controller.admin;

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
import javafx.stage.Window;
import javafx.util.Callback;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductsListController implements Initializable {

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
        tableColumn.setPrefWidth(160);
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<>() {

            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button("Edit");

                    {
                        editButton.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());
                            editProduct(editButton.getScene().getWindow(), product);
                        });
                    }

                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Delete product");
                            alert.setHeaderText("Are you sure that you want to delete Product: " + product.getName() + "?");
                            Optional<ButtonType> confirm = alert.showAndWait();

                            if (confirm.get() == ButtonType.OK) {
                                if (DataSource.getInstance().deleteProduct(product.getProductid())) {
                                    getTableView().getItems().remove(getIndex());
                                }
                            }
                        });
                    }

                    private final HBox buttonsPane = new HBox();

                    {
                        buttonsPane.setAlignment(Pos.CENTER);
                        buttonsPane.setSpacing(5);
                        buttonsPane.getChildren().add(editButton);
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

        productsTable.getColumns().add(0, id);
        productsTable.getColumns().add(tableColumn);
    }

    private void editProduct(Window window, Product product) {
        EditProductController controller = DataSource.getInstance().windowPopUp(window, "Admin/editproduct.fxml").getController();
        controller.setName(product.getName());
        controller.setPrice(product.getPrice());
        controller.setQuantity(product.getQuantity());
        controller.setExpirationDate(product.getExpireddate());
        controller.setCategory(product.getCategory());
        controller.setTemp(product.getName());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addActions();
        showProducts();

        back.setOnAction(event -> {
            DataSource.getInstance().fxmlLoader(event, "Admin/dashboard.fxml");
        });
    }
}
