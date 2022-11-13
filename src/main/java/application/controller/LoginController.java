package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private RadioButton customerButton, managerButton;

    @FXML
    private TextField username;

    @FXML
    private Button loginButton, signupButton;

    @FXML
    private PasswordField password;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signupButton.setOnAction(event -> {
            DBUtils.changeScene(event, "SignUpUI.fxml", "Registration");
        });

        loginButton.setOnAction(event -> {
            if (!username.getText().trim().isEmpty() || !password.getText().trim().isEmpty()) {
                if (customerButton.isSelected()) {
                    DBUtils.loginInUser(event, username.getText(), password.getText());
                } else if (managerButton.isSelected()) {

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please choose login type");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please fill in all");
                alert.show();
            }
        });
    }
}