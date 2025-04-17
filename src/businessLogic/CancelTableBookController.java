package businessLogic;

import java.io.IOException;

import javafx.collections.FXCollections;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import database.GuestDbhandler;
import domain.TableBooking;

public class CancelTableBookController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
    @FXML
    private TableView<TableBooking> tableBookTable;

    @FXML
    private TableColumn<TableBooking, Integer> idColumn;

    @FXML
    private TableColumn<TableBooking, Integer> guestIdColumn;

    @FXML
    private TableColumn<TableBooking, Integer> tableNoColumn;

    @FXML
    private TableColumn<TableBooking, Integer> noOfGuestsColumn;

    @FXML
    private TableColumn<TableBooking, String> statusColumn;

    @FXML
    private TextField tableIdField;
    private int guestId; // Guest ID to filter bookings

    @FXML
    public void initialize() {
        // Configure table columns
    	 idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    	 guestIdColumn.setCellValueFactory(new PropertyValueFactory<>("guestId"));
    	 tableNoColumn.setCellValueFactory(new PropertyValueFactory<>("tableNo"));
    	 noOfGuestsColumn.setCellValueFactory(new PropertyValueFactory<>("noOfGuests"));
    	 statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    	    
        guestId=GuestDbhandler.getCancelGuestId();
        // Load guest's pending bookings into the table
        loadPendingBookings();
    }

    @FXML
    private void handleEnter(ActionEvent event) {
        String tableNoInput = tableIdField.getText().trim();
        if (tableNoInput.isEmpty()) {
            showAlert("Error", "Please enter a table number.");
            return;
        }

        try {
            int tableNo = Integer.parseInt(tableNoInput);

            boolean success = GuestDbhandler.cancelTableBooking(tableNo);
            if (success) {
                showAlert("Success", "Table booking cancelled.");
                loadPendingBookings();
            } else {
                showAlert("Error", "No pending booking found for the specified table.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid table number.");
        }
    }

    @FXML
    private void handleDone(ActionEvent event) throws IOException {
        String tableNoInput = tableIdField.getText().trim();

        if (!tableNoInput.isEmpty()) {
            try {
                int roomNo = Integer.parseInt(tableNoInput);
                boolean success = GuestDbhandler.cancelTableBooking(roomNo);
                if (success) {
                    showAlert("Success", "Table booking cancelled.");
                } else {
                    showAlert("Error", "No pending booking found for the specified table.");
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid table number.");
                return;
            }
        }

        switchtoback(event);
    }
    @FXML
    public void switchtoback(ActionEvent event) throws IOException
    {
         root = FXMLLoader.load(getClass().getResource("/application/cancelbooking.fxml"));
         stage = (Stage)((Node)event.getSource()).getScene().getWindow();
         scene = new Scene(root);
         String css= this.getClass().getResource("/application/application.css").toExternalForm();
         scene.getStylesheets().add(css);
         stage.setScene(scene);
         stage.show();
    }
    private void loadPendingBookings() {
        ObservableList<TableBooking> pendingBookings = FXCollections.observableArrayList(GuestDbhandler.getPendingTableBookings());
        tableBookTable.setItems(pendingBookings);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
