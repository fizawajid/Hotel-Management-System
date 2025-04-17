package domain;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import businessLogic.TableGuestCredentialsController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import database.GuestDbhandler;

public class Table {
    private int TableNo;
    private int Tablecapacity;

    public Table(int TableNo, int Tablecapacity) {
        this.TableNo = TableNo;
        this.Tablecapacity = Tablecapacity;
    }

    // Getters and Setters
    public int getTableNo() {
        return TableNo;
    }

    public void setTableNo(int TableNo) {
        this.TableNo = TableNo;
    }

    public int getCapacity() {
        return Tablecapacity;
    }

    public void setCapacity(int Tablecapacity) {
        this.Tablecapacity = Tablecapacity;
    }

    // Method to check table availability
    public static void checkTableAvailability(DatePicker reservationDatePicker, TextField capacityField, 
                                              TextField starttimeField, TextField endtimeField, 
                                              TableView<Table> tableView) {
        try {
            if (reservationDatePicker.getValue() == null || capacityField.getText().isEmpty()
                    || starttimeField.getText().isEmpty() || endtimeField.getText().isEmpty()) {
                showAlert("Missing Input", "Please fill in all required fields.");
                return;
            }

            String reservationDate = reservationDatePicker.getValue().toString();
            int capacity = Integer.parseInt(capacityField.getText());
            String startReservationTime = starttimeField.getText();
            String endReservationTime = endtimeField.getText();

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            LocalTime startTime;
            LocalTime endTime;

            LocalDate today = LocalDate.now();
            LocalDate reservationDateValue = reservationDatePicker.getValue();

            if (reservationDateValue.isBefore(today)) {
                showAlert("Validation Error", "Reservation date cannot be in the past.");
                return;
            }

            try {
                startTime = LocalTime.parse(startReservationTime, timeFormatter);
                endTime = LocalTime.parse(endReservationTime, timeFormatter);
            } catch (DateTimeParseException e) {
                showAlert("Validation Error", "Please enter a valid time in HH:mm format.");
                return;
            }

            if (reservationDateValue.equals(today) && startTime.isBefore(LocalTime.now())) {
                showAlert("Validation Error", "Start time cannot be in the past for today's date.");
                return;
            }

            if (endTime.isBefore(startTime)) {
                showAlert("Validation Error", "End time cannot be before start time.");
                return;
            }

            if (startTime.equals(endTime)) {
                showAlert("Validation Error", "Start time and end time cannot be the same.");
                return;
            }

            ObservableList<Table> availableTables = GuestDbhandler.getAvailableTables(reservationDate, capacity, startReservationTime, endReservationTime);
            tableView.setItems(availableTables);

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for capacity.");
        }
    }

    // Method to navigate to the next screen
    public static void clickNextAtables(ActionEvent event, DatePicker reservationDatePicker, TextField capacityField,
                                        TextField starttimeField, TextField endtimeField, TextField tablenofield) throws IOException {
        if (reservationDatePicker.getValue() == null || capacityField.getText().isEmpty()
                || starttimeField.getText().isEmpty() || endtimeField.getText().isEmpty()
                || tablenofield.getText().isEmpty()) {
            showAlert("Missing Input", "Please fill in all required fields.");
            return;
        }

        String reservationDate = reservationDatePicker.getValue().toString();
        int capacity = Integer.parseInt(capacityField.getText());
        String starttime = starttimeField.getText();
        String endtime = endtimeField.getText();
        int tableNo = Integer.parseInt(tablenofield.getText());

        FXMLLoader loader = new FXMLLoader(Table.class.getResource("/application/bookTableCredentials.fxml"));
        Parent root = loader.load();

        TableGuestCredentialsController credentialsController = loader.getController();
        credentialsController.settableBookingDetails(reservationDate, starttime, endtime, capacity, tableNo);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        String css = Table.class.getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    // Utility method for alerts
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
