package businessLogic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class CreditCardPaymentController extends PaymentController {
    @FXML
    private TextField cardNumberField;
    @FXML
    private TextField CVVField;
    @FXML
    private DatePicker expirationDateField;
    @FXML
    private TextField nameOnCardField;
    @FXML
    private TextField amountField;

    @FXML
    public void initialize() {
        initializeGrandTotal();
    }

    @Override
	public void handlePayAction(ActionEvent event) throws IOException {
        try {
            if (!validateInput()) return;

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

    private boolean validateInput() {
        if (cardNumberField.getText().isEmpty() || CVVField.getText().isEmpty() || expirationDateField.getValue() == null
            || amountField.getText().isEmpty() || nameOnCardField.getText().isEmpty()) {
            showAlert("Missing Information", "Please fill in all the fields.", AlertType.ERROR);
            return false;
        }

        if (!isCardNumberValid(cardNumberField.getText())) {
            showAlert("Invalid Card Number", "Please enter a valid 16-digit card number.", AlertType.ERROR);
            return false;
        }

        if (!isCVVValid(CVVField.getText())) {
            showAlert("Invalid CVV", "Please enter a valid 3-digit CVV.", AlertType.ERROR);
            return false;
        }

        if (!isExpirationDateValid(expirationDateField.getValue())) {
            showAlert("Invalid Expiration Date", "The expiration date must be in the future.", AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean isCardNumberValid(String cardNumber) {
        return cardNumber != null && cardNumber.length() == 16 && cardNumber.matches("\\d+");
    }

    private boolean isCVVValid(String cvv) {
        return cvv != null && cvv.length() == 3 && cvv.matches("\\d+");
    }

    private boolean isExpirationDateValid(LocalDate expirationDate) {
        return expirationDate != null && expirationDate.isAfter(LocalDate.now());
    }

}