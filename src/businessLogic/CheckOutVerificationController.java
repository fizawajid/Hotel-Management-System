package businessLogic;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import database.CheckOutDB;

public class CheckOutVerificationController {

    public TextField nameField;
    public TextField idField;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    public void clickVerify(ActionEvent event) throws IOException {
        String guestName = nameField.getText().trim();
        String guestID = idField.getText().trim();

        // Validate inputs
        if (guestName.isEmpty() || guestID.isEmpty()) {
            showErrorAlert("Validation Error", "Both Name and ID fields must be filled.");
            return;
        }
     // Call the database handling function
        boolean isVerified = CheckOutDB.verifyCheckOutGuest(guestID, guestName);

        if (isVerified) {
            // Proceed to the checkout page if verification is successful
            loadCheckOutPage(event);
        }
    }
    @FXML
    private void loadCheckOutPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/checkOut.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css= this.getClass().getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void switchtoback(ActionEvent event) throws IOException
    {
	 Controller1 control = new Controller1();
     control.switchtoguest(event);
    }
    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
