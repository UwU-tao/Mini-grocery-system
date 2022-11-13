module demo.app.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens demo.app.demo to javafx.fxml;
    exports demo.app.demo;
}