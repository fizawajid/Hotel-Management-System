package businessLogic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import database.CheckInDB;

public class RequestHouseKeepingServiceController {

    @FXML
    private CheckBox roomCleaningCheckBox;

    @FXML
    private CheckBox laundryCheckBox;

    @FXML
    private CheckBox minibarCheckBox;

    @FXML
    private CheckBox pillowsCheckBox;

    @FXML
    private CheckBox petCareCheckBox;

    @FXML
    private Button doneButton;
    
    @FXML
    public void handleDoneButtonAction(ActionEvent event) throws SQLException, IOException {
        
    	int guestId = CheckInDB.getLoggedInGuestId(); // Get the current guest ID
        int roomNo = CheckInDB.getRoomNoByGuestId(guestId); // Fetch the room number
        
        System.out.println("Room No: " + roomNo);
        System.out.println("Guest ID: " + guestId);
        
        if (roomNo != -1) { // Ensure a valid room number is fetched
            // Handle each housekeeping request through the model
            if (roomCleaningCheckBox.isSelected()) {
                handleServiceRequest(roomNo, "Room Cleaning and Maintenance", event);
            }
            if (laundryCheckBox.isSelected()) {
                handleServiceRequest(roomNo, "Laundary Services", event);
            }
            if (minibarCheckBox.isSelected()) {
                handleServiceRequest(roomNo, "Restocking Minibar", event);
            }
            if (pillowsCheckBox.isSelected()) {
                handleServiceRequest(roomNo, "Extra Pillows, Blankets and Towels", event);
            }
            if (petCareCheckBox.isSelected()) {
                handleServiceRequest(roomNo, "Pet Care", event);
            }

            // Success message if all requests are submitted
            
            switchtoback(event); // Navigate back or perform the relevant action
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not find room number for the current guest.");
        }
       
    }
    
    private void handleServiceRequest(int roomNo, String serviceType, ActionEvent event) throws SQLException {
    	int guestId = CheckInDB.getLoggedInGuestId(); 
    	System.out.println("Room No: " + roomNo);
    	System.out.println(serviceType);
        // Check if the service request is already pending
        if (CheckInDB.isServiceRequestPending(guestId,roomNo, serviceType)) {
            // If pending, notify the guest
            showAlert(Alert.AlertType.INFORMATION, "Request Pending", 
                      "Be patient, your request for '" + serviceType + "' is being handled.");
        } else {
            // Otherwise, insert the new service request
        	CheckInDB.insertHouseKeepingRequest(guestId,roomNo, serviceType);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Housekeeping requests have been submitted.");
            
        }
    } 

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void switchtoback(ActionEvent event)throws IOException
    {
    	Parent root = FXMLLoader.load(getClass().getResource("/application/roomService.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css= this.getClass().getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }
    
}