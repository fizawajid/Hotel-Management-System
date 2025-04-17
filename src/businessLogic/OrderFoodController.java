package businessLogic;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

import java.sql.SQLException;

import database.CheckInDB;
import domain.MenuItem;

public class OrderFoodController {

    @FXML
    private TableView<MenuItem> menuTable;
    @FXML
    private TableColumn<MenuItem, Integer> itemIdColumn;
    @FXML
    private TableColumn<MenuItem, String> itemNameColumn;
    @FXML
    private TableColumn<MenuItem, Double> priceColumn;
    @FXML
    private TableColumn<MenuItem, String> categoryColumn;
    @FXML
    private TextField itemIdField;
    @FXML
    private TextField quantityField;
    @FXML
    private Button enterButton;
    @FXML
    private Button doneButton;
 
    @FXML
    public void initialize() throws SQLException {
        // Set up the columns with the model class properties
    	itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
    	itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
    	priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    	categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        // Load data into the table view for checked-in orders
        ObservableList<MenuItem> ordersData = CheckInDB.getMenuItems();
        menuTable.setItems(ordersData);
    }

    // Handle Enter button action to add an item to the order
    @FXML
    public void handleEnterButton(ActionEvent event) {
    	String guestIdText = itemIdField.getText();
        String guestName =quantityField.getText();
        
       
        
        if (guestIdText.isEmpty() || guestName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter both Item ID and Quantity.");
            return;
        }
        try {
            // Get itemId and quantity from user input
            int itemId = Integer.parseInt(itemIdField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            

            if (quantity <= 0) {
                showAlert(Alert.AlertType.WARNING, "Invalid Quantity", "Please enter a valid quantity.");
                return;
            }

            // Verify if the entered item ID exists in the Menu table
            if (!CheckInDB.isMenuItemValid(itemId)) {
                showAlert(Alert.AlertType.ERROR, "Error", "No menu item found with the provided ID.");
                return;
            }

            // Insert the order into the OrderFood table
            int guestId = CheckInDB.getLoggedInGuestId(); // Assuming you have this method
            if (CheckInDB.insertFoodOrder(guestId, itemId, quantity)) {
                showAlert(Alert.AlertType.INFORMATION, "Order Added", "Your food order has been added.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to place the order.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input. Please enter valid Item ID and Quantity.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to place the order.");
        }
    }

    // Handle Done button action to finalize the order and switch to the room service screen
    @FXML
    public void handleDoneButton(ActionEvent event) throws IOException, SQLException {
    	
    	String guestIdText = itemIdField.getText();
        String guestName =quantityField.getText();
               
        if (guestIdText.isEmpty() && guestName.isEmpty()) {
            switchtoback(event);
            return;
        }
        if(guestIdText.isEmpty() && guestName.isEmpty()) {
        	showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter both Item ID and Quantity.");
            return;
        }
        try {
            // Get itemId and quantity from user input
            int itemId = Integer.parseInt(itemIdField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            

            if (quantity <= 0) {
                showAlert(Alert.AlertType.WARNING, "Invalid Quantity", "Please enter a valid quantity.");
                return;
            }

            // Verify if the entered item ID exists in the Menu table
            if (!CheckInDB.isMenuItemValid(itemId)) {
                showAlert(Alert.AlertType.ERROR, "Error", "No menu item found with the provided ID.");
                return;
            }

            // Insert the order into the OrderFood table
            int guestId = CheckInDB.getLoggedInGuestId(); // Assuming you have this method
            if (CheckInDB.insertFoodOrder(guestId, itemId, quantity)) {
                showAlert(Alert.AlertType.INFORMATION, "Order Added", "Your food order has been added.");
                switchtoback(event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to place the order.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input. Please enter valid Item ID and Quantity.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to place the order.");
        }
            
    }
         
    // Method to switch to the room service screen (roomservice.fxml)
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

    // Method to show an alert dialog with a specific message
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}