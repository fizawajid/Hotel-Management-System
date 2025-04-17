package businessLogic;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import database.GuestDbhandler;

public class GuestCredentialsController {
	 private Stage stage;
	    private Scene scene;
	    private Parent root;
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField numberOfGuestsField;
    
    private String checkinDate;
    private String checkoutDate;
    private int noGuests1;

    // A method to set the values without binding them to UI components
    public void setBookingDetails(String checkinDate, String checkoutDate, int noGuests) {
        // Store the values in instance variables for later use
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.noGuests1 = noGuests;
        //this.noGuests1 = Integer.parseInt(noGuests);
        // You can also use these values for further processing here
        // For example, you could use them for calculations or database interactions
        System.out.println("Booking Details: " + checkinDate + ", " + checkoutDate + ", " + noGuests);
    }

    @FXML
    public void handleBookButton(ActionEvent event) throws IOException {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String bookingType = "room"; // Example: Default booking type
        String checkinStatus = "pending";   // Example: Default check-in status
        String noguests= numberOfGuestsField.getText();
        if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || noguests.isEmpty()) {
            showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }
		int noguestsint;
		 try {
			 noguestsint=Integer.parseInt(noguests);
	        } catch (NumberFormatException e) {
	            showAlert("Validation Error", "Guest numbers must be a valid number.");
	            return;
	        }
        int guestid = GuestDbhandler.addGuest(firstName, lastName, phoneNumber, email, bookingType, checkinStatus,noguestsint, noGuests1,checkinDate,checkoutDate);
        if (guestid>0) {
        	showAlert("Success", "Guest has been successfully booked!\nGuest ID: " + guestid);
            clearFields();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/bookrooms.fxml"));
                Parent root = loader.load();
                // Get the current stage and set the new scene
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                String css= this.getClass().getResource("/application/application.css").toExternalForm();
                scene.getStylesheets().add(css);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                showAlert("Error", "Failed to load the previous screen. Please try again.");
            }
        } else {
            showAlert("Error", "Failed to book the guest. Please try again.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void switchtoback(ActionEvent event) throws IOException
    {
     root = FXMLLoader.load(getClass().getResource("/application/bookrooms.fxml"));
     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
     scene = new Scene(root);
     String css= this.getClass().getResource("/application/application.css").toExternalForm();
     scene.getStylesheets().add(css);
     stage.setScene(scene);
     stage.show();
    }
    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        phoneNumberField.clear();
        emailField.clear();
        numberOfGuestsField.clear();
    }
}
