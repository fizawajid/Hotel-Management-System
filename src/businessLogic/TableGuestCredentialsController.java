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

public class TableGuestCredentialsController {

	private Stage stage;
    private Scene scene;
    private Parent root;
	
    @FXML
    private TextField fnamefield;

    @FXML
    private TextField lnamefield;

    @FXML
    private TextField phonefield;

    @FXML
    private TextField emailfield;

    private String reservationDate;
    private String starttime;
    private String endtime;
    private int capacity;
    private int tableno;
    
    // A method to set the values without binding them to UI components
    public void settableBookingDetails(String reservationDate, String starttime, String endtime, int capacity, int tableno) {
        // Store the values in instance variables for later use
        this.reservationDate =  reservationDate;
        this.starttime = starttime;
        this.endtime = endtime;
        this.capacity = capacity;
        this.tableno = tableno;
      
        System.out.println("Booking Details: " + reservationDate + ", " + starttime + ", " + endtime + " ," + capacity + "," + tableno);
    }
    @FXML
    public void clickbacktablecredential(ActionEvent event) throws IOException
    {
    	root = FXMLLoader.load(getClass().getResource("/application/booktable.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css= this.getClass().getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void bookguesttable(ActionEvent event) {
        String firstName1 = fnamefield.getText();
        String lastName1 = lnamefield.getText();
        String phoneNumber = phonefield.getText();
        String email = emailfield.getText();
        String bookingType = "table"; // Example: Default booking type
        String checkinStatus = "pending";   // Example: Default check-in status

        if (firstName1.isEmpty() || lastName1.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
            showAlert("Validation Error", "Please fill in all required fields.");
            return;
        }

        int guestId = GuestDbhandler.addGuestfortable(firstName1, lastName1, phoneNumber, email, bookingType, checkinStatus, capacity, tableno, starttime, endtime, reservationDate);

        if (guestId > 0) {
            showAlert("Success", "Guest has been successfully booked!\nGuest ID: " + guestId);
            clearFields();
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/booktable.fxml"));
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

    private void clearFields() {
        fnamefield.clear();
        lnamefield.clear();
        phonefield.clear();
        emailfield.clear();
    }
}
