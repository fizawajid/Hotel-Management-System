package businessLogic;

import database.CheckInDB;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class RoomServiceController {
	
	@FXML
    public void clickhousekeeping(ActionEvent event) throws IOException {
		Integer loggedInGuestId = CheckInDB.getLoggedInGuestId();
		String bookingType = CheckInDB.getBookingType(loggedInGuestId); // Fetch the booking type for the guest
       
        if ("room".equalsIgnoreCase(bookingType)) {
        	Parent root = FXMLLoader.load(getClass().getResource("/application/requesthousekeeping.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            String css= this.getClass().getResource("/application/application.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.show();
        	
            
        } else {
            showAlert(Alert.AlertType.WARNING, "Access Denied", "Request housekeeping service is only available for room guests.");
        }
		
	}
	public void clickorderfood(ActionEvent event) throws IOException {
		
        	Parent root = FXMLLoader.load(getClass().getResource("/application/orderfood.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            String css= this.getClass().getResource("/application/application.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.show();
		
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
	private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}