package businessLogic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import database.CheckOutDB;
import database.DatabaseManager;

public class GiveFeedbackController {
	private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private ComboBox<Integer> foodQualityComboBox;
    @FXML
    private ComboBox<Integer> staffAttitudeComboBox;
    @FXML
    private ComboBox<Integer> cleanlinessComboBox;
    @FXML
    private ComboBox<Integer> roomServiceComboBox;
    @FXML
    private ComboBox<Integer> gymServiceComboBox;
    @FXML
    private ComboBox<Integer> overallSatisfacComboBox;
    @FXML
    private Button submitFeedbackbtn;

    private int guestID = CheckOutDB.getGuestId(); // Assume a guest ID is available. Replace with actual logic.
    
    @FXML
    public void initialize() {
        // Populate combo boxes with values 1 to 5
    	ObservableList<Integer> ratings = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        foodQualityComboBox.setItems(ratings);
        staffAttitudeComboBox.setItems(ratings);
        cleanlinessComboBox.setItems(ratings);
        roomServiceComboBox.setItems(ratings);
        gymServiceComboBox.setItems(ratings);
        overallSatisfacComboBox.setItems(ratings);
    }
    @FXML
    private void submitFeedback(ActionEvent event) throws IOException {
        // Validate that at least one ComboBox is filled
        if (isAnyComboBoxFilled()) {
            // Collect feedback and store it in the database
            try (Connection conn = DatabaseManager.getConnection();) {
                if (foodQualityComboBox.getValue() != null) {
                	CheckOutDB.insertFeedback(guestID, "Food Quality", foodQualityComboBox.getValue());
                }
                if (staffAttitudeComboBox.getValue() != null) {
                	CheckOutDB.insertFeedback(guestID, "Staff Attitude", staffAttitudeComboBox.getValue());
                }
                if (cleanlinessComboBox.getValue() != null) {
                	CheckOutDB.insertFeedback(guestID, "Cleanliness", cleanlinessComboBox.getValue());
                }
                if (roomServiceComboBox.getValue() != null) {
                	CheckOutDB.insertFeedback(guestID, "Room Service", roomServiceComboBox.getValue());
                }
                if (gymServiceComboBox.getValue() != null) {
                	CheckOutDB.insertFeedback(guestID, "Gym Service", gymServiceComboBox.getValue());
                }
                if (overallSatisfacComboBox.getValue() != null) {
                	CheckOutDB.insertFeedback(guestID, "Overall Satisfaction", overallSatisfacComboBox.getValue());
                }

                // Show success message
                showAlert("Success", "Feedback submitted successfully.");

                // Proceed to checkOut page
                root = FXMLLoader.load(getClass().getResource("/application/checkOut.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                String css= this.getClass().getResource("/application/application.css").toExternalForm();
                scene.getStylesheets().add(css);
                stage.setScene(scene);
                stage.show();

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while submitting feedback.");
            }

        } else {
            // Show validation error message if no ComboBox is filled
            showAlert("Validation Error", "At least one feedback field must be filled.");

            // Stay on the current page (no scene change)
        }
    }

    private boolean isAnyComboBoxFilled() {
        return foodQualityComboBox.getValue() != null ||
               staffAttitudeComboBox.getValue() != null ||
               cleanlinessComboBox.getValue() != null ||
               roomServiceComboBox.getValue() != null ||
               gymServiceComboBox.getValue() != null ||
               overallSatisfacComboBox.getValue() != null;
    }
    @FXML
    public void switchtoback(ActionEvent event) throws IOException
    {
	 Controller1 control = new Controller1();
     control.clickVerify(event);
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
