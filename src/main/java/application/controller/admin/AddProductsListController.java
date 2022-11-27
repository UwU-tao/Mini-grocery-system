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
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class AddProductsListController implements Initializable {
    private List<Product> products = new ArrayList<>();

    public boolean addProduct(Product product) {
        if (!products.contains(product)) {
            products.add(product);
            return true;
        }
        return false;
    }

    public boolean deleteProduct(Product product) {
        for (Product p : products) {
            if (p.getProductid() == product.getProductid()) {
                products.remove(p);
                return true;
            }
        }
        return false;
    }

    @FXML
    private Button addProduct;

    @FXML
    private Button back;

    @FXML
    private TableColumn<Product, String> categoryCol;

    @FXML
    private Button clearList;

    @FXML
    private TableColumn<Product, String> dateCol;

    @FXML
    private Button importBtn;

    @FXML
    private TableView<Product> importTable;

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
        importTable.setItems(FXCollections.observableArrayList(products));
    }

    private void addActions() {
        TableColumn<Product, String> id = new TableColumn<>("#");
        id.setCellValueFactory(product -> new ReadOnlyObjectWrapper<>(importTable.getItems().indexOf(product.getValue()) + ""));
        id.setSortable(false);
        id.setPrefWidth(40);

        TableColumn tableColumn = new TableColumn<>("Actions");
        tableColumn.setPrefWidth(120);
        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Product, Void> call(TableColumn<Product, Void> param) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {
                            Product product = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Delete product");
                            alert.setHeaderText("Are you sure that you want to delete Product: " + product.getName() + "?");
                            Optional<ButtonType> confirm = alert.showAndWait();

                            if (confirm.get() == ButtonType.OK) {
                                if (deleteProduct(product)) {
                                    getTableView().getItems().remove(getIndex());
                                }
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

        importTable.getColumns().add(0, id);
        importTable.getColumns().add(tableColumn);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addActions();
        showProducts();

        back.setOnAction(event -> {
            DataSource.getInstance().fxmlLoader(event, "Admin/dashboard.fxml");
        });

        addProduct.setOnAction(event -> {
            DataSource.getInstance().windowPopUp(addProduct.getScene().getWindow(), "Admin/addproduct.fxml");
        });

        clearList.setOnAction(event -> {
            products.clear();
            showProducts();
        });

        importBtn.setOnAction(event -> {
            DataSource.getInstance().importProducts(products);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succeed");
            alert.setHeaderText("Successfully");
            alert.show();
        });
    }
}
