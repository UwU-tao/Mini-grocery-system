package application.controller.customers;

import application.Main;
import application.controller.UserController;
import application.models.Product;
import application.utils.DataSource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Button cart;

    @FXML
    private Button logout;
    @FXML
    private Button edit;

    @FXML
    private Button products;

    @FXML
    private StackPane stack;

    @FXML
    private TableView<Product> tableView;

    @FXML
    private Label name;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setText(UserController.getInstance().getUsername().toUpperCase());

        logout.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure you want to log out?");

            alert.setTitle("Log out?");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.get() == ButtonType.OK) {
                DataSource.getInstance().translateData();
                UserController.getInstance().clearSession();

                Stage stage;
                Node node = (Node) event.getSource();
                stage = (Stage) node.getScene().getWindow();
                stage.close();
                Scene scene = null;
                try {
                    scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("SignInUI.fxml"))));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setScene(scene);
                stage.show();
            }
        });

        cart.setOnAction(event -> {
            FXMLLoader fxmlLoader = DataSource.getInstance().fxmlLoader(event,"Customer/cart.fxml");
            CartController cartController = fxmlLoader.getController();
            cartController.showProducts();
        });

        edit.setOnAction(event -> {
            DataSource.getInstance().fxmlLoader(event, "Customer/editpassword.fxml");

        });

        products.setOnAction(event -> {
            FXMLLoader loader = DataSource.getInstance().fxmlLoader(event, "Customer/products.fxml");
            ProductsController controller = loader.getController();
            controller.showProducts();
        });
    }
}
