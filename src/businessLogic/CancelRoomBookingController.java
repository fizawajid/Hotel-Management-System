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
import domain.RoomBooking;

public class CancelRoomBookingController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
    @FXML
    private TableView<RoomBooking> roomBookTable;

    @FXML
    private TableColumn<RoomBooking, Integer> idColumn;

    @FXML
    private TableColumn<RoomBooking, Integer> guestIdColumn;

    @FXML
    private TableColumn<RoomBooking, Integer> roomNoColumn;

    @FXML
    private TableColumn<RoomBooking, Integer> noOfGuestsColumn;

    @FXML
    private TableColumn<RoomBooking, String> statusColumn;

    @FXML
    private TextField roomIdField;
    private int guestId; // Guest ID to filter bookings

    @FXML
    public void initialize() {
        // Configure table columns
    	 idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    	 guestIdColumn.setCellValueFactory(new PropertyValueFactory<>("guestId"));
    	 roomNoColumn.setCellValueFactory(new PropertyValueFactory<>("roomNo"));
    	 noOfGuestsColumn.setCellValueFactory(new PropertyValueFactory<>("noOfGuests"));
    	 statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    	    
        guestId=GuestDbhandler.getCancelGuestId();
        // Load guest's pending bookings into the table
        loadPendingBookings();
    }

    @FXML
    private void handleEnter(ActionEvent event) {
        String roomNoInput = roomIdField.getText().trim();
        if (roomNoInput.isEmpty()) {
            showAlert("Error", "Please enter a room number.");
            return;
        }

        try {
            int roomNo = Integer.parseInt(roomNoInput);

            boolean success = GuestDbhandler.cancelBooking(roomNo);
            if (success) {
                showAlert("Success", "Room booking cancelled.");
                loadPendingBookings();
            } else {
                showAlert("Error", "No pending booking found for the specified room.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid room number.");
        }
    }

    @FXML
    private void handleDone(ActionEvent event) throws IOException {
        String roomNoInput = roomIdField.getText().trim();

        if (!roomNoInput.isEmpty()) {
            try {
                int roomNo = Integer.parseInt(roomNoInput);
                boolean success = GuestDbhandler.cancelBooking(roomNo);
                if (success) {
                    showAlert("Success", "Room booking cancelled.");
                } else {
                    showAlert("Error", "No pending booking found for the specified room.");
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid room number.");
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
        ObservableList<RoomBooking> pendingBookings = FXCollections.observableArrayList(GuestDbhandler.getPendingBookings());
        roomBookTable.setItems(pendingBookings);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
