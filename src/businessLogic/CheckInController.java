package businessLogic;

import database.CheckInDB;
import database.GuestDbhandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class CheckInController {

    @FXML
    private TextField guestIdField;

    @FXML
    private TextField guestNameField;

    @FXML
    private Button checkInButton;

    private GuestDbhandler dbHandler;

    public CheckInController() {
        this.dbHandler = new GuestDbhandler();
    }

    @FXML
    public void handleCheckIn(ActionEvent event) throws IOException {
        String guestIdText = guestIdField.getText();
        String guestName = guestNameField.getText();
        
       
        
        if (guestIdText.isEmpty() || guestName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter both Guest ID and Name.");
            return;
        }

        try {
            int guestId = Integer.parseInt(guestIdText);
            CheckInDB.authenticateGuest(guestId);
            // Verify guest info
            boolean isGuestValid = CheckInDB.verifyGuestInfo(guestId, guestName);
            if (!isGuestValid) {
                showAlert(Alert.AlertType.ERROR, "Validation Failed", "Guest ID or Name does not match any records.");
                return;
            }
            
            String status = CheckInDB.getBookingStatus(guestId);
            
            if ("checkedin".equalsIgnoreCase(status)) {
            	
            	loadNextPage(event);
            	return;
            }
            
            if ("checkedout".equalsIgnoreCase(status)) {
            	showAlert(Alert.AlertType.ERROR,"Validation Failed", "You have Checked-Out already");
                return;
            }
            
            // Determine booking type (room or table)
            boolean hasRoomBooking = CheckInDB.hasRoomBooking(guestId);
            boolean hasTableReservation = CheckInDB.hasTableReservation(guestId);
            
            if (hasRoomBooking) {
            	
            	
                if (handleRoomCheckIn(guestId)) {
                	
                    loadNextPage(event);
                    return;// Load Room Dashboard or related page
                }
            } else if (hasTableReservation) {
            	
                if (handleTableCheckIn(guestId)) {
                    loadNextPage(event);
                    return;// Load Table Dashboard or related page
                }
            } 

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Guest ID must be a valid number.");
        }
    }

    private boolean handleRoomCheckIn(int guestId) {

        LocalDate checkInDate = CheckInDB.getCheckInDate(guestId);
        LocalDate checkOutDate = CheckInDB.getCheckOutDate(guestId);
        LocalDate today = LocalDate.now();

        if (today.isBefore(checkInDate)) {
            showAlert(Alert.AlertType.WARNING, "Early Check-In", "You cannot check in before the check-in date.");
            return false;
        } else if (today.isEqual(checkInDate) || (today.isAfter(checkInDate) && (today.isBefore(checkOutDate)))) {
            boolean isUpdated = CheckInDB.updateRoomCheckInStatus(guestId);
            
                showAlert(Alert.AlertType.INFORMATION, "Check-In Successful", "Welcome!");
                return true;
           
        } else if (today.isAfter(checkOutDate)|| (today.isEqual(checkOutDate))) {
            showAlert(Alert.AlertType.ERROR, "Check-In Denied", "The check-out date has passed. You cannot check in.");
            return false;
        }
        return false;
    }

    private boolean handleTableCheckIn(int guestId) {
        LocalDate currentDate = LocalDate.now(); // Get today's date
        LocalDate reservationDate = CheckInDB.getReservationDate(guestId); // Fetch reservation date

        if (!currentDate.isEqual(reservationDate)) {
            if (currentDate.isBefore(reservationDate)) {
                showAlert(Alert.AlertType.WARNING, "Too Early", "You cannot check in before the reservation date.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Reservation Expired", "The reservation date has passed. You cannot check in.");
            }
            return false;
        }

        LocalTime currentTime = LocalTime.now();
        LocalTime startTime = CheckInDB.getStartTime(guestId);
        LocalTime endTime = CheckInDB.getEndTime(guestId);

        // Check if startTime or endTime is null
        if (startTime == null || endTime == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Time Data", "Reservation start or end time is not set.");
            return false;
        }

        if (currentTime.isBefore(startTime)) {
            showAlert(Alert.AlertType.WARNING, "Too Early", "You cannot check in before the reservation start time.");
            return false;
        } else if (currentTime.isAfter(endTime)) {
            showAlert(Alert.AlertType.ERROR, "Time Exceeded", "The reservation time has passed. You cannot check in.");
            return false;
        } else {
            boolean isUpdated = CheckInDB.updateTableCheckInStatus(guestId);
            
                showAlert(Alert.AlertType.INFORMATION, "Check-In Successful", "Welcome!");
                return true;
           
        }
    }


    
    
    public void switchtoback(ActionEvent event)throws IOException
    {
    	Parent root = FXMLLoader.load(getClass().getResource("/application/guest.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css= this.getClass().getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }
    
    public void loadNextPage(ActionEvent event)throws IOException
    {
    	Parent root = FXMLLoader.load(getClass().getResource("/application/roomService.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css= this.getClass().getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        stage.setScene(scene);
        stage.show();
    }
    
    

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}