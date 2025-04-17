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

import domain.HouseKeepingService;
import database.dbHandler;

public class ManageRoomsController implements Initializable {

    @FXML
    private TableView<HouseKeepingService> handleRoomServicesTable;

    @FXML
    private TableColumn<HouseKeepingService, Integer> idColumn;

    @FXML
    private TableColumn<HouseKeepingService, Integer> roomNoColumn;
    
    @FXML
    private TableColumn<HouseKeepingService, Integer> guestIdColumn;

    @FXML
    private TableColumn<HouseKeepingService, String> serviceTypeColumn;

    @FXML
    private TextField roomServicesTextField;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up the columns with the model class properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        guestIdColumn.setCellValueFactory(new PropertyValueFactory<>("guestId"));
        roomNoColumn.setCellValueFactory(new PropertyValueFactory<>("roomNo"));
        serviceTypeColumn.setCellValueFactory(new PropertyValueFactory<>("serviceType"));

        // Load data into the table view
        ObservableList<HouseKeepingService> servicesData = dbHandler.loadRequestedServices();
        handleRoomServicesTable.setItems(servicesData);
    }
 // Handle the "Provide Service" button click event
    @FXML
    private void handleProvideService() {
        String serviceIdText = roomServicesTextField.getText();

        if (serviceIdText.isEmpty()) {
        	showAlert(Alert.AlertType.ERROR, "Error", "Please enter a service ID.");
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
        if (!dbHandler.isServiceIdValid(serviceId)) {
        	showAlert(Alert.AlertType.ERROR, "Error", "No service found with the provided ID.");
            return;
        }

        // Delete the service from the database
        if (dbHandler.deleteService(serviceId)) {
        	showAlert(Alert.AlertType.INFORMATION, "Success", "Service marked as provided and deleted.");
            // Refresh the table
        	ObservableList<HouseKeepingService> servicesData = dbHandler.loadRequestedServices();
            handleRoomServicesTable.setItems(servicesData);
        } else {
        	showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the service.");
        }
    }
    @FXML
    private void handleDoneService() {
        String serviceIdText = roomServicesTextField.getText();

        if (serviceIdText.isEmpty()) {
        	// Call switchtoback at the end
            try {
                switchtoback(null);
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
        if (!dbHandler.isServiceIdValid(serviceId)) {
        	showAlert(Alert.AlertType.ERROR, "Error", "No service found with the provided ID.");
            return;
        }

        // Delete the service from the database
        if (dbHandler.deleteService(serviceId)) {
        	showAlert(Alert.AlertType.INFORMATION, "Success", "Service marked as provided and deleted.");
            // Refresh the table
        	ObservableList<HouseKeepingService> servicesData = dbHandler.loadRequestedServices();
            handleRoomServicesTable.setItems(servicesData);
            
         // Call switchtoback at the end
            try {
                switchtoback(null);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to switch to the previous screen.");
            }
        } else {
        	showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the service.");
        }
        
    }
    
    @FXML
    public void switchtoback(ActionEvent event) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("/application/crew.fxml"));

        // Use any UI element to get the current stage
        Stage stage = (Stage) roomServicesTextField.getScene().getWindow();
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
