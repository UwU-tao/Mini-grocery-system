package application.controller.customers;

import application.Main;
import application.controller.UserController;
import application.utils.DataSource;
import application.utils.Helper;
import application.utils.PasswordUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditPassController implements Initializable {
    @FXML
    private PasswordField oldpass;
    @FXML
    private PasswordField newpass;
    @FXML
    private Button confirm;
    @FXML
    private Button back;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirm.setOnAction(event -> {
            boolean check = PasswordUtils.verifyPass(oldpass.getText(), UserController.getInstance().getPassword(), DataSource.getInstance().getUserByUsername(UserController.getInstance().getUsername()).getSalt());
            if (!check) {
                Helper.alertBox("Error!!!", "You have entered wrong password");
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Are you sure that you wanna change the password?");
                Optional<ButtonType> optional = alert.showAndWait();

                if (optional.get() == ButtonType.OK) {
                    boolean confirm = DataSource.getInstance().validPassword(newpass.getText());
                    if (confirm) {
                        DataSource.getInstance().changePass(newpass.getText());
                        alert.setTitle("Succeed");
                        alert.setHeaderText("You have change your password successfully");
                        alert.show();
                    } else {
                        Helper.alertBox("Wrong format", "You have entered the wrong password format");
                    }
                }
            }
        });

        back.setOnAction(event -> {
            DataSource.getInstance().fxmlLoader(event, "Customer/dashboard.fxml");


        });
    }
}
