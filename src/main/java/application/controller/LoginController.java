package application.controller;

import application.Main;
import application.models.User;
import application.utils.DataSource;
import application.utils.Helper;
import application.utils.PasswordUtils;
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
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField username;

    @FXML
    private Button loginButton, signupButton;

    @FXML
    private PasswordField password;

    private Stage stage = new Stage();

    private Scene scene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setOnAction(event -> {
            String user = username.getText();
            String pass = password.getText();

            if (user == null || user.isEmpty() || pass == null || pass.isEmpty()) {
                Helper.alertBox("Login Failed", "Please enter the username and password");
            } else {
                User us = DataSource.getInstance().getUserByUsername(user);
                if (us.getPassword() == null || us.getPassword().isEmpty()) {
                    Helper.alertBox("Login Failed", "No username matches that username");
                } else {
                    boolean match = PasswordUtils.verifyPass(pass, us.getPassword(), us.getSalt());

                    if (match) {
                        UserController.setUserid(us.getUserid());
                        UserController.setUsername(us.getUsername());
                        UserController.setPassword(us.getPassword());
                        UserController.setEmail(us.getEmail());
                        UserController.setAdmin(us.getAdmin());

                        Node node = (Node) event.getSource();
                        stage = (Stage) node.getScene().getWindow();
                        stage.close();
                        try {
                            if (us.getAdmin() == 0) {
                                scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Customer/dashboard.fxml"))));
                            } else if (us.getAdmin() == 1) {

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        stage.setScene(scene);
                        stage.show();
                    } else {
                        Helper.alertBox("Login Failed", "Wrong Password!");
                    }
                }
            }
        });

        signupButton.setOnAction(event -> {
            try {
                Node node = (Node) event.getSource();
                stage = (Stage) node.getScene().getWindow();
                stage.close();
                scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("SignUpUI.fxml"))));
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}