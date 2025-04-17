package businessLogic;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;

import database.CheckOutDB;
import domain.FoodItem;

public class MakePaymentController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private TableView<FoodItem> paymentTable;
    @FXML private TableColumn<FoodItem, String> itemColumn;
    @FXML private TableColumn<FoodItem, String> categoryColumn;
    @FXML private TableColumn<FoodItem, Double> priceColumn;
    @FXML private TableColumn<FoodItem, Integer> quantityColumn;
    @FXML private TableColumn<FoodItem, Double> totalPriceColumn;
    @FXML private Label lblFoodTotal;
    @FXML private Label lblRoomTotal;
    @FXML private Label lblGrandTotal;

    public void initialize() {
        // Set up table columns
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // Determine the booking type and load data accordingly
        if ("room".equalsIgnoreCase(CheckOutDB.getBookType())) {
            loadPaymentData(CheckOutDB.getGuestId(), true);
        } else if ("table".equalsIgnoreCase(CheckOutDB.getBookType())) {
            loadPaymentData(CheckOutDB.getGuestId(), false);
        }
    }

    public void loadPaymentData(int guestID, boolean isRoomGuest) {
        try {
            // Get food data and totals
            ObservableList<FoodItem> foodItems = CheckOutDB.getFoodItems(guestID);
            double foodTotal = CheckOutDB.calculateFoodTotal(guestID);

            // Calculate room total if applicable
            double roomTotal = 0;
            if (isRoomGuest) {
                roomTotal = CheckOutDB.calculateRoomTotal(guestID);
            }

            // Set data in TableView
            paymentTable.setItems(foodItems);

            // Display totals
            lblFoodTotal.setText("Food Total: " + foodTotal);
            lblRoomTotal.setText(isRoomGuest ? "Room Total: " + roomTotal : "Room Total: N/A");
            lblGrandTotal.setText("Grand Total: " + (foodTotal + roomTotal));

        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database Error", "An error occurred while fetching payment data.");
        }
    }

    @FXML
    private void payByCash(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/application/cashPayment.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css= this.getClass().getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void payByCreditCard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/application/creditcardpayment.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css= this.getClass().getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/application/checkOut.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        String css= this.getClass().getResource("/application/application.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
