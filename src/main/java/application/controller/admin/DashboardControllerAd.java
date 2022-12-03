package application.controller.admin;

import application.Main;
import application.controller.UserController;
import application.utils.DataSource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardControllerAd implements Initializable {
    private static ProductsListController controller = new ProductsListController();
    public static ProductsListController getController() {return controller;}

    private static AddProductsListController listController = new AddProductsListController();
    public static AddProductsListController getListController() {return listController;}
    @FXML
    private Button addProducts;

    @FXML
    private Button logout;

    @FXML
    private Button memberslist;

    @FXML
    private Button productsList;

    @FXML
    private Button orders;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logout.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure you want to log out?");

            alert.setTitle("Log out?");
            Optional<ButtonType> optional = alert.showAndWait();
            if (optional.get() == ButtonType.OK) {
                UserController.getInstance().clearSession();
                Stage stage;
                Node node = (Node) event.getSource();
                stage = (Stage) node.getScene().getWindow();
                stage.close();
                Scene scene = null;
                try {
                    scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("SignInUI.fxml"))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.setScene(scene);
                stage.show();
            }
        });

        addProducts.setOnAction(event -> {
            FXMLLoader loader = DataSource.getInstance().fxmlLoader(event, "Admin/addproductslist.fxml");
            listController = loader.getController();
        });

        productsList.setOnAction(event -> {
            FXMLLoader loader = DataSource.getInstance().fxmlLoader(event, "Admin/productslist.fxml");
            controller = loader.getController();
        });

        memberslist.setOnAction(event -> {
            FXMLLoader loader = DataSource.getInstance().fxmlLoader(event, "Admin/memberslist.fxml");
            MembersController controller = loader.getController();
            controller.showMembers();
        });

        orders.setOnAction(event -> {
            DataSource.getInstance().fxmlLoader(event, "Admin/ordersdetails.fxml");
        });
    }
}
