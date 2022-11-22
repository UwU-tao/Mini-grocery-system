package application.controller.customers;

import application.Main;
import application.controller.UserController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private TableView tableView;

    @FXML
    private Button cart, logout;

    @FXML
    private Label name;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setText(UserController.getUsername().toUpperCase());

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

        cart.setOnAction(event -> {

        });
    }
}
