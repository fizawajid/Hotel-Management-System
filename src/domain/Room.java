package domain;

import java.io.IOException;
import java.time.LocalDate;

import businessLogic.GuestCredentialsController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import database.GuestDbhandler;

public class Room {
    private int roomNo;
    private String roomType;
    private double price;

    public Room(int roomNo, String roomType, double price) {
        this.roomNo = roomNo;
        this.roomType = roomType;
        this.price = price;
    }

    // Getters and Setters
    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Method to check room availability
    public static void checkRoomAvailability(DatePicker checkinDatePicker, DatePicker checkoutDatePicker, TableView<Room> roomTable) {
        if (checkinDatePicker.getValue() == null || checkoutDatePicker.getValue() == null) {
            showAlert("Validation Error", "Please select both check-in and check-out dates.");
            return;
        }

        LocalDate checkin = checkinDatePicker.getValue();
        LocalDate checkout = checkoutDatePicker.getValue();
        LocalDate today = LocalDate.now();

        if (checkin.isBefore(today)) {
            showAlert("Validation Error", "Check-in date cannot be before today's date.");
            return;
        }
        if (checkout.isBefore(checkin)) {
            showAlert("Validation Error", "Check-out date cannot be before check-in date.");
            return;
        }
        if (checkout.isEqual(checkin)) {
            showAlert("Validation Error", "Check-in and check-out dates cannot be the same.");
            return;
        }

        ObservableList<Room> availableRooms = GuestDbhandler.getAvailableRooms(checkin.toString(), checkout.toString());
        roomTable.setItems(availableRooms);
    }

    // Method to navigate to the next page
    public static void clickNextARooms(ActionEvent event, String checkinDate, String checkoutDate, String roomNo, TableView<Room> roomTable) throws IOException {
        int roomNoInt;
        try {
            roomNoInt = Integer.parseInt(roomNo);
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Room number must be a valid number.");
            return;
        }

        boolean roomExists = roomTable.getItems().stream()
                .anyMatch(room -> room.getRoomNo() == roomNoInt);

        if (!roomExists) {
            showAlert("Validation Error", "The entered room number does not match any available rooms.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(Room.class.getResource("/application/bookRoomCredentials.fxml"));
        Parent root = loader.load();

        GuestCredentialsController credentialsController = loader.getController();
        credentialsController.setBookingDetails(checkinDate, checkoutDate, roomNoInt);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = Room.class.getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    // Utility method to display alerts
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
