package businessLogic;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import domain.RestaurantService;
import database.dbHandler;

public class HandleRestServicesController implements Initializable {

    @FXML
    private TableView<RestaurantService> handleRestaurantServicesTable;

    @FXML
    private TableColumn<RestaurantService, Integer> idColumn;

    @FXML
    private TableColumn<RestaurantService, Integer> guestIdColumn;

    @FXML
    private TableColumn<RestaurantService, String> itemColumn;

    @FXML
    private TableColumn<RestaurantService, Integer> quantityColumn;

    @FXML
    private TextField orderServicesTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up the columns with the model class properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        guestIdColumn.setCellValueFactory(new PropertyValueFactory<>("guestid"));
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Load data into the table view for checked-in orders
        ObservableList<RestaurantService> ordersData = dbHandler.loadCheckedInOrders();
        handleRestaurantServicesTable.setItems(ordersData);
    }

    @FXML
    private void handleProvideService() {
        String orderIdText = orderServicesTextField.getText();

        if (orderIdText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter an order ID.");
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(orderIdText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid order ID. Please enter a valid number.");
            return;
        }

        // Verify if the entered order ID exists in the database
        if (!dbHandler.isOrderValid(orderId)) {
            showAlert(Alert.AlertType.ERROR, "Error", "No order found with the provided ID.");
            return;
        }

        // Delete the order from the database
        if (dbHandler.UpdateOrder(orderId)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Order marked as served and deleted.");
            // Refresh the table
            ObservableList<RestaurantService> ordersData = dbHandler.loadCheckedInOrders();
            handleRestaurantServicesTable.setItems(ordersData);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to mark the order as served.");
        }
    }
    @FXML
    private void handleDoneService() {
        String serviceIdText = orderServicesTextField.getText();

        if (serviceIdText.isEmpty()) {
        	// Call switchtoback at the end
            try {
                switchToBack(null);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to switch to the previous screen.");
            }
            return;
        }

        int serviceId;
        try {
            serviceId = Integer.parseInt(serviceIdText);
        } catch (NumberFormatException e) {
        	showAlert(Alert.AlertType.ERROR, "Error", "Invalid service ID. Please enter a valid number.");
            return;
        }

        // Verify if the entered service ID exists in the database
        if (!dbHandler.isOrderValid(serviceId)) {
        	showAlert(Alert.AlertType.ERROR, "Error", "No service found with the provided ID.");
            return;
        }

        // Delete the service from the database
        if (dbHandler.UpdateOrder(serviceId)) {
        	showAlert(Alert.AlertType.INFORMATION, "Success", "Service marked as provided and deleted.");
            // Refresh the table
        	ObservableList<RestaurantService> servicesData = dbHandler.loadCheckedInOrders();
        	handleRestaurantServicesTable.setItems(servicesData);
            
         // Call switchtoback at the end
            try {
                switchToBack(null);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to switch to the previous screen.");
            }
        } else {
        	showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the service.");
        }
        
    }
    @FXML
    public void switchToBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/application/restaurantStaff.fxml"));

        // Use any UI element to get the current stage
        Stage stage = (Stage) orderServicesTextField.getScene().getWindow();
        Scene scene = new Scene(root);
        String css= this.getClass().getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}