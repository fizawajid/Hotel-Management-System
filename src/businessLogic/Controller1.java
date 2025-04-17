package businessLogic;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import database.CheckOutDB;
import database.dbHandler;

public class Controller1 {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField staffusernametext, staffpasswordtext; // Username input field
    private dbHandler dbHandler; // Instance of database handler
    
    @FXML
    private TextField visiblepasswordtext;

    @FXML
    private Button togglePasswordButton;

    private boolean isPasswordVisible = false;

    @FXML
    public void togglePasswordVisibility(ActionEvent event) {
        if (isPasswordVisible) {
            // Switch to PasswordField
            staffpasswordtext.setText(visiblepasswordtext.getText());
            staffpasswordtext.setVisible(true);
            visiblepasswordtext.setVisible(false);
            togglePasswordButton.setText("👁"); // Eye icon
            isPasswordVisible = false;
        } else {
            // Switch to TextField
            visiblepasswordtext.setText(staffpasswordtext.getText());
            visiblepasswordtext.setVisible(true);
            staffpasswordtext.setVisible(false);
            togglePasswordButton.setText("🔒"); // Lock icon
            isPasswordVisible = true;
        }
    }

    public Controller1() {
        this.dbHandler = new dbHandler(); // Initialize the database handler
    }
    @FXML
    public void switchtomain(ActionEvent event) throws IOException
    {
     root = FXMLLoader.load(getClass().getResource("/application/main.fxml"));
     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
     scene = new Scene(root);
     String css= this.getClass().getResource("/application/application.css").toExternalForm();
     scene.getStylesheets().add(css);
     stage.setScene(scene);
     stage.show();
    }
    @FXML
    public void switchtostaff(ActionEvent event) throws IOException
    {
     root = FXMLLoader.load(getClass().getResource("/application/staffLogin.fxml"));
     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
     scene = new Scene(root);
     String css= this.getClass().getResource("/application/application.css").toExternalForm();
     scene.getStylesheets().add(css);
     stage.setScene(scene);
     stage.show();
    }
    @FXML
    public void switchtospecificstaff(ActionEvent event) throws IOException {
        String username = staffusernametext.getText();
        String password = staffpasswordtext.getText();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
        	showAlert(AlertType.WARNING, "Validation Error", "Please enter both username and password.");
            return;
        }

        String role = dbHandler.authenticateUser(username, password);

        if (role == null) {
        	showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
            return; // Stop further processing if authentication fails
        }

        String fxmlFile = "";

        // Determine the FXML file based on the role
        switch (role) {
            case "manager":
                fxmlFile = "/application/mainManager.fxml";
                break;
            case "gym manager":
                fxmlFile = "/application/gymManager.fxml";
                break;
            case "crew":
                fxmlFile = "/application/crew.fxml";
                break;
            case "restaurant staff":
                fxmlFile = "/application/restaurantStaff.fxml";
                break;
            default:
            	showAlert(AlertType.ERROR, "Error", "Role not recognized.");
                return;
        }

        // Load and switch to the corresponding FXML file
        root = FXMLLoader.load(getClass().getResource(fxmlFile));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css= this.getClass().getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }
    
    //////////////////////////GUEST/////////////////////////
    
    @FXML
    public void switchtoguest(ActionEvent event)throws IOException
    {
      root = FXMLLoader.load(getClass().getResource("/application/guest.fxml"));
     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
     scene = new Scene(root);
     String css= this.getClass().getResource("/application/application.css").toExternalForm();
     scene.getStylesheets().add(css);
     stage.setScene(scene);
     stage.show();
    }
    @FXML
    public void clickbookRoom(ActionEvent event) throws IOException
    {
     root = FXMLLoader.load(getClass().getResource("/application/bookrooms.fxml"));
     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
     scene = new Scene(root);
     String css= this.getClass().getResource("/application/application.css").toExternalForm();
     scene.getStylesheets().add(css);
     stage.setScene(scene);
     stage.show();
    }
    @FXML
    public void clickbookTable(ActionEvent event) throws IOException
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
    public void clickCheckIn(ActionEvent event) throws IOException
    {
     root = FXMLLoader.load(getClass().getResource("/application/checkIn.fxml"));
     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
     scene = new Scene(root);
     String css= this.getClass().getResource("/application/application.css").toExternalForm();
     scene.getStylesheets().add(css);
     stage.setScene(scene);
     stage.show();
    }
    @FXML
    public void clickCheckOut(ActionEvent event) throws IOException
    {
     root = FXMLLoader.load(getClass().getResource("/application/checkOutVerification.fxml"));
     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
     scene = new Scene(root);
     String css= this.getClass().getResource("/application/application.css").toExternalForm();
     scene.getStylesheets().add(css);
     stage.setScene(scene);
     stage.show();
    }
    @FXML
    public void clickVerify(ActionEvent event) throws IOException
    {
     root = FXMLLoader.load(getClass().getResource("/application/checkOut.fxml"));
     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
     scene = new Scene(root);
     String css= this.getClass().getResource("/application/application.css").toExternalForm();
     scene.getStylesheets().add(css);
     stage.setScene(scene);
     stage.show();
    }
    @FXML
    public void clickMakePayment(ActionEvent event) throws IOException
    {
    	if("checkedout".equalsIgnoreCase(CheckOutDB.getCheckInStatus())) {
    		showAlert(Alert.AlertType.WARNING, "Warning", "You have already made payment");
    	}
    	else {
		     root = FXMLLoader.load(getClass().getResource("/application/makePayment.fxml"));
		     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		     scene = new Scene(root);
		        String css= this.getClass().getResource("/application/application.css").toExternalForm();
		        scene.getStylesheets().add(css);
		     stage.setScene(scene);
		     stage.show();
    	}
    }
    @FXML
    public void clickGiveFeedback(ActionEvent event) throws IOException
    {
     root = FXMLLoader.load(getClass().getResource("/application/GiveFeedback.fxml"));
     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
     scene = new Scene(root);
     String css= this.getClass().getResource("/application/application.css").toExternalForm();
     scene.getStylesheets().add(css);
     stage.setScene(scene);
     stage.show();
    }
    @FXML
    public void backtoguest(ActionEvent event)throws IOException
    {
    	if("checkedin".equalsIgnoreCase(CheckOutDB.getCheckInStatus())) {
    		showAlert(Alert.AlertType.WARNING, "Warning", "You have not made payment, please do it");
    	}
    	else {
		     root = FXMLLoader.load(getClass().getResource("/application/guest.fxml"));
		     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		     scene = new Scene(root);
		        String css= this.getClass().getResource("/application/application.css").toExternalForm();
		        scene.getStylesheets().add(css);
		     stage.setScene(scene);
		     stage.show();
    	}
    }
    @FXML
    public void clickCancelBooking(ActionEvent event) throws IOException
    {
     root = FXMLLoader.load(getClass().getResource("/application/cancelBookingVerification.fxml"));
     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
     scene = new Scene(root);
     String css= this.getClass().getResource("/application/application.css").toExternalForm();
     scene.getStylesheets().add(css);
     stage.setScene(scene);
     stage.show();
    }
    @FXML
    public void clickCancelRoomBooking(ActionEvent event) throws IOException
    {
     root = FXMLLoader.load(getClass().getResource("/application/cancelroombooking.fxml"));
     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
     scene = new Scene(root);
     String css= this.getClass().getResource("/application/application.css").toExternalForm();
     scene.getStylesheets().add(css);
     stage.setScene(scene);
     stage.show();
    }
    @FXML
    public void clickCancelTableBooking(ActionEvent event) throws IOException
    {
     root = FXMLLoader.load(getClass().getResource("/application/canceltablebooking.fxml"));
     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
     scene = new Scene(root);
     String css= this.getClass().getResource("/application/application.css").toExternalForm();
     scene.getStylesheets().add(css);
     stage.setScene(scene);
     stage.show();
    }
    
    
//    @FXML
//    public void clickviewroom(ActionEvent event) throws IOException
//    {
//     root = FXMLLoader.load(getClass().getResource("/application/viewroom.fxml"));
//     stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//     scene = new Scene(root);
//     String css= this.getClass().getResource("/application/application.css").toExternalForm();
//     scene.getStylesheets().add(css);
//     stage.setScene(scene);
//     stage.show();
//    }
    
    @FXML
    private void openGoogle(ActionEvent event) {
        try {
            // Open the default browser to Google's homepage
            java.awt.Desktop.getDesktop().browse(new java.net.URI("https://www.google.com/search?q=stellarstays@gmail.com"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
 // Utility method to show alert dialogs
    public void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // You can add a header if needed
        alert.setContentText(message);
        alert.showAndWait();
    }   
}
