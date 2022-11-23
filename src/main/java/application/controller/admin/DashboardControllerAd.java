package application.controller.admin;

import application.Main;
import application.controller.UserController;
import application.utils.DataSource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardControllerAd implements Initializable {

    @FXML
    private Button addProducts;

    @FXML
    private Button logout;

    @FXML
    private Button ordersDetails;

    @FXML
    private Button productsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logout.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure you want to log out?");

            alert.setTitle("Log out?");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.get() == ButtonType.OK) {
                UserController.clearSession();
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

        addProducts.setOnAction(event -> {
            DataSource.getInstance().fxmlLoader(event, "Admin/addproductslist.fxml");
        });

        productsList.setOnAction(event -> {
            FXMLLoader loader = DataSource.getInstance().fxmlLoader(event, "Admin/productslist.fxml");
            ProductsListController controller = loader.getController();
            controller.showProducts();
        });
    }
}
