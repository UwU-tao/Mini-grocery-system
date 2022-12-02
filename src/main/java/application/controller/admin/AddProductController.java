package application.controller.admin;

import application.models.Product;
import application.utils.DataSource;
import application.utils.Helper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    @FXML
    private Button addProduct;

    @FXML
    private ComboBox<String> category;

    @FXML
    private Button exit;

    @FXML
    private TextField expirationDate;

    @FXML
    private TextField name;

    @FXML
    private TextField price;

    @FXML
    private TextField quantity;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        category.setItems(FXCollections.observableArrayList(DataSource.getInstance().getCategories()));
        // Todo
        //  Set text formatter for all the input fields.

        exit.setOnAction(event -> {
            Stage stage = (Stage) exit.getScene().getWindow();
            stage.close();
        });

        addProduct.setOnAction(event -> {
            try {
                int categoryid = DataSource.getInstance().getCategoryId(category.getSelectionModel().getSelectedItem());
                String n = name.getText();
                double p = Double.parseDouble(price.getText());
                int q = Integer.parseInt(quantity.getText());
                Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(expirationDate.getText());

                Product product = new Product();
                product.setQuantity(q);
                product.setCategoryid(categoryid);
                product.setName(n);
                product.setExpireddate(date);
                product.setPrice(p);

                boolean check = DashboardControllerAd.getListController().addProduct(product);

                if (check) {
                    Helper.alertBox("Succeed", "Successfully");
                    name.clear();
                    expirationDate.clear();
                    price.clear();
                    quantity.clear();
                    category.getSelectionModel().select(-1);
                    DashboardControllerAd.getListController().showProducts();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }
}
