module demo.app.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens application to javafx.fxml;
    exports application;
    exports application.main;
    opens application.main to javafx.fxml;
    exports application.controller;
    opens application.controller to javafx.fxml;
    exports application.models;
    opens application.models to javafx.fxml;
    exports application.utils;
    opens application.utils to javafx.fxml;
    exports application.controller.customers;

    opens application.controller.customers to javafx.fxml;
    opens application.controller.admin to javafx.fxml;
}