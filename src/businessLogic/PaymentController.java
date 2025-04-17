package businessLogic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.sql.SQLException;

import database.CheckOutDB;

public abstract class PaymentController {
    protected double grandTotal;

    @FXML
    public abstract void handlePayAction(ActionEvent event) throws IOException, SQLException;

    protected void initializeGrandTotal() {
        grandTotal = database.CheckOutDB.getFoodPrice() + database.CheckOutDB.getRoomPrice();
    }
    protected void processPayment(double returnAmount) throws SQLException {
        int guestID = CheckOutDB.getGuestId();
        String bookType = CheckOutDB.getBookType();

        if ("room".equalsIgnoreCase(bookType)) {
            CheckOutDB.insertRoomPayment(
                CheckOutDB.getRoomOrTableNo(),
                grandTotal,
                guestID,
                CheckOutDB.getRoomPrice(),
                CheckOutDB.getFoodPrice(),
                CheckOutDB.getPaymentDate()
            );
            CheckOutDB.updateCheckinStatus("BookRoom", guestID);
        } else if ("table".equalsIgnoreCase(bookType)) {
            CheckOutDB.insertTablePayment(
                CheckOutDB.getRoomOrTableNo(),
                guestID,
                grandTotal,
                CheckOutDB.getPaymentDate()
            );
            CheckOutDB.updateCheckinStatus("BookTable", guestID);
        }
    }
    protected void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    protected void switchtoback(ActionEvent event) throws IOException {
        Controller1 control = new Controller1();
        control.clickVerify(event);
    }
}