package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {
    @FXML
    private RadioButton agreeButton;

    @FXML
    private Button createButton, signinButton;

    @FXML
    private TextField email, username;

    @FXML
    private PasswordField password;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signinButton.setOnAction(event -> {
                DBUtils.changeScene(event, "SignInUI.fxml", "Login Form");
        });

        createButton.setOnAction(event -> {
            if (agreeButton.isSelected()) {
                if (!username.getText().trim().isEmpty() || !password.getText().trim().isEmpty() || !email.getText().trim().isEmpty()) {
                    DBUtils.signUpUser(event, username.getText(), password.getText(), email.getText());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("You must fill in all the information");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You must check the Terms and Conditons!");
                alert.show();
            }
        });
    }
}
