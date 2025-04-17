package businessLogic;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import database.GuestDbhandler;

public class CancelBookVerifController {
	 	private Stage stage;
	    private Scene scene;
	    private Parent root;

	    @FXML
	    private TextField nameField;

	    @FXML
	    private TextField idField;
	    
	    @FXML
	    private void handleVerify(ActionEvent event) {
	        String fullName = nameField.getText().trim();
	        String idInput = idField.getText().trim();

	        if (fullName.isEmpty() || idInput.isEmpty()) {
	            showAlert("Error", "Please fill in both fields.");
	            return;
	        }

	        try {
	            int guestId = Integer.parseInt(idInput);
	            String[] nameParts = fullName.split(" ");
	            if (nameParts.length < 2) {
	                showAlert("Error", "Please enter the full name (first and last).");
	                return;
	            }
	            String firstName = nameParts[0];
	            String lastName = nameParts[1];

	            // Call database handler function
	            boolean guestExists = GuestDbhandler.verifyGuestFromDatabase(guestId, firstName, lastName);

	            if (guestExists) {
	                // Guest found, navigate to cancelBooking.fxml
	                navigateToCancelBooking(event);
	            } else {
	                showAlert("Error", "No pending bookings found for the provided details.");
	            }
	        } catch (NumberFormatException e) {
	            showAlert("Error", "Please enter a valid numeric ID.");
	        } catch (Exception e) {
	            e.printStackTrace();
	            showAlert("Error", "An error occurred. Please try again.");
	        }
	    }
	    private void navigateToCancelBooking(ActionEvent event) {
	        try {
	            root = FXMLLoader.load(getClass().getResource("/application/cancelbooking.fxml"));
	            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            scene = new Scene(root);
	            String css= this.getClass().getResource("/application/application.css").toExternalForm();
	            scene.getStylesheets().add(css);
	            stage.setScene(scene);
	            stage.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	            showAlert("Error", "Failed to load the next page.");
	        }
	    }
	    @FXML
	    public void switchtoback(ActionEvent event) throws IOException
	    {
		 Controller1 control = new Controller1();
	     control.switchtoguest(event);
	    }
	    private void showAlert(String title, String message) {
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle(title);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }
}
