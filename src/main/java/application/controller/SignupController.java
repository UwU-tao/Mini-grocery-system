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
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
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

    private Stage stage;
    private Scene scene;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signinButton.setOnAction(event -> {
            try {
                Node node = (Node) event.getSource();
                stage = (Stage) node.getScene().getWindow();
                stage.close();
                scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("SignInUI.fxml"))));
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        createButton.setOnAction(event -> {
            String user = username.getText();
            String pass = password.getText();
            String e = email.getText();
            boolean check = agreeButton.isSelected();
            String error = "";
            boolean valid = true;

            if (user == null || user.isEmpty()) {
                error += "Please enter an username!\n";
                valid = false;
            } else if (!DataSource.getInstance().validUsername(user)) {
                error += "Please enter a valid username!\n";
                valid = false;
            }

            if (pass == null || pass.isEmpty()) {
                error += "Please enter a password!\n";
                valid = false;
            } else if (!DataSource.getInstance().validPassword(pass)) {
                error += "Please enter a valid password!\n";
                valid = false;
            }

            if (e == null || e.isEmpty()) {
                error += "Please enter an email!\n";
                valid = false;
            } else if (!DataSource.getInstance().validEmail(e)) {
                error += "Please enter a valid email!\n";
                valid = false;
            }

            if (DataSource.getInstance().getUserByUsername(user).getPassword() != null) {
                error += "This username has been used";
                valid = false;
            }

            if (!valid) {
                Helper.alertBox("Sign Up Failed!", error);
            } else {
                String salt = PasswordUtils.getSaltvalue(20);
                String securedPass = PasswordUtils.generateSecuredPass(pass, salt);

                if (DataSource.getInstance().addNewUser(user, securedPass, e, salt)) {
                    User us = DataSource.getInstance().getUserByUsername(user);

                    UserController.getInstance().setUsername(us.getUsername());
                    UserController.getInstance().setPassword(us.getPassword());
                    UserController.getInstance().setEmail(us.getEmail());
                    UserController.getInstance().setAdmin(us.getAdmin());
                    UserController.getInstance().setUserid(us.getUserid());

                    Node node =(Node) event.getSource();
                    stage = (Stage) node.getScene().getWindow();
                    stage.close();
                    try {
                        if (us.getAdmin() == 0) {
                            scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Customer/dashboard.fxml"))));
                        } else if (us.getAdmin() == 1) {
                            scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Admin/dashboard.fxml"))));
                        }
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }
}
