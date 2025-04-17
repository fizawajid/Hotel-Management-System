package businessLogic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.SQLException;

public class CashPaymentController extends PaymentController {
    @FXML
    private Button payBtn;
    @FXML
    private TextField amountField;
    @FXML
    private Button cashPaymentBackbtn;

    @FXML
    public void initialize() {
        initializeGrandTotal();
    }

    @FXML
	public void handlePayAction(ActionEvent event) throws IOException {
        try {
            double enteredAmount = Double.parseDouble(amountField.getText());
            if (enteredAmount == grandTotal) {
                processPayment(0);
                showAlert("Payment Successful", "Your payment was successful!", AlertType.INFORMATION);
                switchtoback(event);
            } else if (enteredAmount > grandTotal) {
                double returnAmount = enteredAmount - grandTotal;
                processPayment(returnAmount);
                showAlert("Payment Successful", "Return Amount: " + returnAmount, AlertType.INFORMATION);
                switchtoback(event);
            } else {
                showAlert("Insufficient Amount", "Entered amount is less than the grand total.", AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid numeric amount.", AlertType.ERROR);
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while processing the payment.", AlertType.ERROR);
            e.printStackTrace();
        }
        
    }

    @Override
    protected void processPayment(double returnAmount) throws SQLException {
        int guestID = database.CheckOutDB.getGuestId();
        String bookType = database.CheckOutDB.getBookType();
        if ("room".equalsIgnoreCase(bookType)) {
            database.CheckOutDB.insertRoomPayment(
                database.CheckOutDB.getRoomOrTableNo(),
                grandTotal,
                guestID,
                database.CheckOutDB.getRoomPrice(),
                database.CheckOutDB.getFoodPrice(),
                database.CheckOutDB.getPaymentDate()
            );
            database.CheckOutDB.updateCheckinStatus("BookRoom", guestID);
        } else if ("table".equalsIgnoreCase(bookType)) {
            database.CheckOutDB.insertTablePayment(
                database.CheckOutDB.getRoomOrTableNo(),
                guestID,
                grandTotal,
                database.CheckOutDB.getPaymentDate()
            );
            database.CheckOutDB.updateCheckinStatus("BookTable", guestID);
        }
    }
}