package application.controller.admin;

import application.utils.DataSource;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditProductController implements Initializable {
    @FXML
    private ComboBox<String> category;

    @FXML
    private Label temp;
    @FXML
    private Button edit;

    @FXML
    private Button exit;

    @FXML
    private TextField expirationDate;

    @FXML
    private TextField name;

    @FXML
    private TextField price;

    public void setCategory(String name) {
        category.getSelectionModel().select(name);
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate.setText(String.valueOf(expirationDate));
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setPrice(double price) {
        this.price.setText(String.valueOf(price));
    }

    public void setQuantity(int quantity) {
        this.quantity.setText(String.valueOf(quantity));
    }

    public void setTemp(String name) {
        this.temp.setText(name);
    }

    @FXML
    private TextField quantity;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        category.setItems(FXCollections.observableArrayList(DataSource.getInstance().getCategories()));
        expirationDate.setEditable(false);

        exit.setOnAction(event -> {
            Stage stage = (Stage) exit.getScene().getWindow();
            stage.close();
        });

        edit.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Do you want to apply these changes?");
            Optional<ButtonType> confirm = alert.showAndWait();
            System.out.println(name.getText());
            if (confirm.get() == ButtonType.OK) {
                int id = DataSource.getInstance().getOneProduct(temp.getText()).getProductid();
                if (DataSource.getInstance().updateProduct(id, name.getText(), Double.parseDouble(price.getText()), category.getValue(), Integer.parseInt(quantity.getText()))) {
                    alert.setTitle("Succeed");
                    alert.setHeaderText("Changes applied");
                    alert.show();

                    DashboardControllerAd.getController().showProducts();
                }
            }
        });
    }
}