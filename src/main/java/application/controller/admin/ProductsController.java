package application.controller.admin;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class ProductsController {
    //    public TextFormatter<Double> doubleTextFormatter() {
    public static UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("[0-9]*(\\.[0-9]*)")) {
            return change;
        }
        return null;
    };
//    }
}
