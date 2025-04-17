package businessLogic;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import database.dbHandler;

public class StaffReportStock  implements Initializable{
    
    @FXML
   private ComboBox<String> InventoryComboBox;
    @FXML
    private TextField quantityTextField;
    
    ObservableList<String> list = FXCollections.observableArrayList(
    	    "Treadmills",
    	    "Exercise Bikes",
    	    "Dumbbells",
    	    "Barbells",
    	    "Weight Plates",
    	    "Kettlebells",
    	    "Yoga Mats",
    	    "Resistance Bands",
    	    "Rowing Machines",
    	    "Elliptical Machines",
    	    "Jump Ropes",
    	    "Medicine Balls",
    	    "Punching Bags",
    	    "Balance Balls",
    	    "Foam Rollers",
    	    "Benches (Incline/Flat)",
    	    "Squat Racks",
    	    "Pull-Up Bars",
    	    "Adjustable Weights",
    	    "Gym Towels"
    	);
    
    private ObservableList<String> restaurantItems = FXCollections.observableArrayList(
            "Ovens", "Grills", "Fridges", "Freezers", "Microwaves", "Stoves", "Dishwashers", "Sinks",
            "Coffee Machines", "Blenders", "Cutlery", "Pots and Pans", "Tables", "Chairs", "Serving Trays"
    );
    private ObservableList<String> roomItems = FXCollections.observableArrayList(
    		"Mattresses", "Pillows" ,"Blankets" ,"Towels" ,"Curtains" ,"Soap", "Shampoo", "Conditioner",
    		"Bed Sheets" ,"Mini Fridges", "Dustbins"
    		);
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Check the role of the logged-in user and update the ComboBox accordingly
        String userRole = dbHandler.getLoggedInUserRole();
        if ("gym manager".equals(userRole)) {
            InventoryComboBox.setItems(list); // Set gym items for gym managers
        } else if ("restaurant staff".equals(userRole)) {
            InventoryComboBox.setItems(restaurantItems); // Set kitchen items for restaurant managers
        }else if ("crew".equals(userRole)) {
            InventoryComboBox.setItems(roomItems); // Set kitchen items for restaurant managers
        }
        else {
            InventoryComboBox.setItems(FXCollections.observableArrayList()); // Set an empty list if role is unknown
        }
    }
 
    @FXML
    public void setenter(ActionEvent event) throws IOException {
        String selectedItem = InventoryComboBox.getValue();
        String quantityText = quantityTextField.getText();

        if (selectedItem != null && !quantityText.isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityText); // Parse quantity as an integer
                String handler = dbHandler.getLoggedInUserRole(); // Get the role of the logged-in user

                // Call the database handler method to add the stock report
                dbHandler db = new dbHandler();
                boolean success = db.addStockReport(handler, selectedItem, quantity);

                if (success) {
                    showAlert(AlertType.INFORMATION, "Report Sent",
                            "Reported: " + selectedItem + " (Qty: " + quantity + ")");
                } else {
                    showAlert(AlertType.ERROR, "Error", "Failed to send the report. Try again.");
                }
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid number for quantity.");
            }
        } else {
            showAlert(AlertType.WARNING, "Missing Information", "Please select an item and enter a quantity.");
        }
    }

//
    @FXML
    public void stockDone(ActionEvent event) throws IOException
    {
    	String role = database.dbHandler.getLoggedInUserRole(); // Get the logged-in user's role
        String fxmlFile = null;

        // Determine the target page based on the role
        switch (role) {
        
            case "crew":
                fxmlFile = "/application/crew.fxml";
                break;
            case "gym manager":
                fxmlFile = "/application/gymManager.fxml";
                break;
            case "manager":
                fxmlFile = "/application/mainManager.fxml";
                break;
            case "restaurant staff":
                fxmlFile = "/application/restaurantStaff.fxml";
                break;
            default:
                showAlert(AlertType.ERROR, "Error", "Role not recognized.");
                return; // Exit if role is not recognized     
        }

        // Load the appropriate FXML file
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css= this.getClass().getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
   }
    @FXML
    public void switchtoback(ActionEvent event) throws IOException {
        String role = database.dbHandler.getLoggedInUserRole(); // Get the logged-in user's role
        String fxmlFile = null;

        // Determine the target page based on the role
        switch (role) {
        
            case "crew":
                fxmlFile = "/application/crew.fxml";
                break;
            case "gym manager":
                fxmlFile = "/application/gymManager.fxml";
                break;
            case "manager":
                fxmlFile = "/application/mainManager.fxml";
                break;
            case "restaurant staff":
                fxmlFile = "/application/restaurantStaff.fxml";
                break;
            default:
                showAlert(AlertType.ERROR, "Error", "Role not recognized.");
                return; // Exit if role is not recognized     
        }

        // Load the appropriate FXML file
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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