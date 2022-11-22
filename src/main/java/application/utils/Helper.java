package application.utils;

import javafx.scene.control.Alert;

public class Helper {
    public static void alertBox(String title, String infoMessage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(infoMessage);
        alert.show();
    }
}
