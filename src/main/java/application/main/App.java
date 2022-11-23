package application.main;

import application.Main;
import application.utils.DataSource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    public static final String CURRENCY = "$";

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("SignInUI.fxml")));

        stage.setTitle("Grocery Store");
        stage.setResizable(false);
        stage.getIcons().add(new Image(Main.class.getResource("Img/store.png").openStream()));

        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void init() {
        try {
            super.init();
            if (!DataSource.getInstance().open()) {
                System.out.println("Failed to connect to database");
                Platform.exit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            super.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}