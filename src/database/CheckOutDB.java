package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import domain.FoodItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CheckOutDB {

	private static double RoomPrice;
	private static int guestId;
    private static String booktype;
    private static java.sql.Date paymentdate;
    private static int roomOrTableNo;
    private static String checkinStatus;
  	private static double FoodPrice;

    public static int getGuestId() {
    	return guestId;
    }
    public static String getBookType() {
    	return booktype;
    }
    public static java.sql.Date getPaymentDate(){
    	return paymentdate;
    }
    public static int getRoomOrTableNo() {
    	return roomOrTableNo;
    }
    public static String getCheckInStatus() {
    	return checkinStatus;
    }
	public static double getFoodPrice() {
		return FoodPrice;
	}
    public static double getRoomPrice() {
    	return RoomPrice;
    }
    
    public static boolean verifyCheckOutGuest(String guestID, String guestName) {

        try (Connection conn = DatabaseManager.getConnection()) {

            // Query to check guest's check-in status and booking type
            String query = "SELECT CheckinStatus, BookingType FROM Guest WHERE GuestID = ? AND CONCAT(FirstName, ' ', LastName) = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(guestID)); // Set the guest ID
            statement.setString(2, guestName); // Set the guest's full name
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                checkinStatus = resultSet.getString("CheckinStatus");
                booktype = resultSet.getString("BookingType");
                if ("checkedin".equalsIgnoreCase(checkinStatus)) {
                    guestId = Integer.parseInt(guestID); // Store guest ID for later use
                    if ("room".equalsIgnoreCase(booktype)) {
                        String roomQuery = "SELECT checkin_date, checkout_date, room_no FROM BookRoom WHERE guest_id = ?";
                        PreparedStatement roomStmt = conn.prepareStatement(roomQuery);
                        roomStmt.setInt(1, guestId);
                        ResultSet roomResult = roomStmt.executeQuery();
                        if (roomResult.next()) {
                            java.sql.Date checkinDate = roomResult.getDate("checkin_date");
                            java.sql.Date checkoutDate = roomResult.getDate("checkout_date");
                            roomOrTableNo = roomResult.getInt("room_no");
                            java.sql.Date todayDate = new java.sql.Date(System.currentTimeMillis());
                            if (todayDate.equals(checkinDate)) {
                                showErrorAlert("Checkout Error", "Guest cannot check out on the check-in date.");
                                return false;
                            } else if (todayDate.after(checkinDate) && !todayDate.equals(checkoutDate)) {
                                // Update the checkout date to today's date
                                String updateQuery = "UPDATE BookRoom SET checkout_date = ? WHERE guest_id = ?";
                                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                                updateStmt.setDate(1, todayDate);
                                updateStmt.setInt(2, guestId);
                                int rowsUpdated = updateStmt.executeUpdate();
                                if (rowsUpdated > 0) {
                                    paymentdate = todayDate; // Update the payment date in memory
                                    return true; // Successfully updated checkout date
                                } else {
                                    showErrorAlert("Update Error", "Failed to update the checkout date.");
                                    return false;
                                }
                            } else if (todayDate.equals(checkoutDate)) {
                                // Proceed with normal checkout
                                paymentdate = checkoutDate;
                                return true;
                            }
                        } else {
                            showErrorAlert("Verification Error", "No booking record found for this guest.");
                            return false;
                        }
                    } else if ("table".equalsIgnoreCase(booktype)) {
                        // Proceed with normal checkout for table bookings
                    	String tableQuery = "SELECT date_of_reservation, table_no FROM BookTable WHERE guest_id = ?";
                        PreparedStatement tableStmt = conn.prepareStatement(tableQuery);
                        tableStmt.setInt(1, guestId);
                        ResultSet tableResult = tableStmt.executeQuery();
                        if (tableResult.next()) {
                            paymentdate = tableResult.getDate("date_of_reservation");
                            roomOrTableNo=tableResult.getInt("table_no");
                        } else {
                            showErrorAlert("Verification Error", "No reservation date found for the table booking.");
                            return false;
                        }
                        return true;
                    } else {
                        showErrorAlert("Verification Error", "Invalid booking type for the guest.");
                        return false;
                    }
                } else {
                    // Guest is not checked in, show an error alert
                    showErrorAlert("Checkout Error", "Guest is not checked in. Please check in first.");
                }
            } else {
                // No guest found with the provided Name and ID
                showErrorAlert("Verification Error", "No guest found with the provided Name and ID.");
            }
        } catch (Exception e) {
            // Handle any database errors
            showErrorAlert("Database Error", "An error occurred while verifying the guest.");
            e.printStackTrace();
        }
        return false; // If guest isn't checked in or there was an error

    }

	public static void insertFeedback(int guestID, String service, int rating) throws SQLException {
	    String query = "INSERT INTO Feedback (GuestID, Service, Rating) VALUES (?, ?, ?)";
	    try (Connection conn = DatabaseManager.getConnection();
	    	PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setInt(1, guestID);
	        stmt.setString(2, service);
	        stmt.setInt(3, rating);
	        stmt.executeUpdate();
	    }
	}
	public static ObservableList<FoodItem> getFoodItems(int guestID) throws SQLException {
        ObservableList<FoodItem> foodItems = FXCollections.observableArrayList();
        String foodQuery = "SELECT m.ItemName, m.Category, m.Price, ofd.Quantity, (m.Price * ofd.Quantity) AS TotalPrice " +
                           "FROM OrderFood ofd " +
                           "JOIN Menu m ON ofd.item_id = m.ItemId " +
                           "WHERE ofd.guest_id = ? and status = 'provided'";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(foodQuery)) {
            stmt.setInt(1, guestID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                foodItems.add(new FoodItem(
                    rs.getString("ItemName"),
                    rs.getString("Category"),
                    rs.getDouble("Price"),
                    rs.getInt("Quantity"),
                    rs.getDouble("TotalPrice")
                ));
            }
        }
        return foodItems;
    }
    public static double calculateFoodTotal(int guestID) throws SQLException {
        String foodTotalQuery = "SELECT SUM(m.Price * ofd.Quantity) AS FoodTotal " +
                                "FROM OrderFood ofd " +
                                "JOIN Menu m ON ofd.item_id = m.ItemId " +
                                "WHERE ofd.guest_id = ? and ofd.status = 'provided'";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(foodTotalQuery)) {
            stmt.setInt(1, guestID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
            	FoodPrice=rs.getDouble("FoodTotal");
                return FoodPrice;
            }
        }
        return 0;
    }
    public static double calculateRoomTotal(int guestID) throws SQLException {
        String roomQuery = "SELECT r.Price, DATEDIFF(DAY, br.checkin_date, br.checkout_date) AS DaysStayed " +
                           "FROM BookRoom br " +
                           "JOIN Room r ON br.room_no = r.RoomNo " +
                           "WHERE br.guest_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(roomQuery)) {
            stmt.setInt(1, guestID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double roomPrice = rs.getDouble("Price");
                int daysStayed = rs.getInt("DaysStayed");
                RoomPrice=roomPrice * daysStayed;
                return RoomPrice;
            }
        }
        return 0;
    }
    public static void insertRoomPayment(int roomNo, double amount, int guestID, double roomPrice, double foodPrice, java.sql.Date paymentDate) throws SQLException {
        String query = "INSERT INTO RoomPayment (RoomNo, Amount, GuestID, RoomPrice, FoodPrice, PaymentDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, roomNo);
            stmt.setDouble(2, amount);
            stmt.setInt(3, guestID);
            stmt.setDouble(4, roomPrice);
            stmt.setDouble(5, foodPrice);
            stmt.setDate(6, paymentDate);
            stmt.executeUpdate();
        }
    }
    public static void insertTablePayment(int tableNo, int guestID, double amount, java.sql.Date paymentDate) throws SQLException {
        String query = "INSERT INTO TablePayment (TableNo, GuestID, Amount, PaymentDate) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, tableNo);
            stmt.setInt(2, guestID);
            stmt.setDouble(3, amount);
            stmt.setDate(4, paymentDate);
            stmt.executeUpdate();
        }
    }
    public static void updateCheckinStatus(String tableName, int guestID) throws SQLException {
    		
    	String query = "UPDATE " + tableName + " SET checkin_status = 'checkedout' WHERE guest_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, guestID);
            stmt.executeUpdate();
        }
        
        // Check if the guest has any other bookings
        boolean hasOtherBookings = checkForOtherBookings(guestID);
        // If no other bookings exist, update the guest's checkin_status to "checkedout"
        if (!hasOtherBookings) {
        	System.out.println("in if");
            updateGuestCheckinStatus(guestID);
            
            // Delete any orders made by the guest
            deleteOrders(guestID);

            // Delete any housekeeping services for the guest
            deleteHousekeepingServices(guestID);
        }
    }
 // Check if the guest has any other bookings
    private static boolean checkForOtherBookings(int guestID) throws SQLException {
        String query = "SELECT COUNT(*) FROM BookRoom WHERE guest_id = ? AND checkin_status != 'checkedout'" +
                       " UNION SELECT COUNT(*) FROM BookTable WHERE guest_id = ? AND checkin_status != 'checkedout'";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, guestID);
            stmt.setInt(2, guestID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;  // Guest has other bookings
                }
            }
        }
        return false;  // No other bookings
    }

    // Update the checkin_status of the guest to 'checkedout'
    private static void updateGuestCheckinStatus(int guestID) throws SQLException {
        String query = "UPDATE Guest SET CheckinStatus = 'checkedout' WHERE GuestID = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, guestID);
            stmt.executeUpdate();
            checkinStatus="checkedout";
        }
    }
 // Delete orders made by the guest
    private static void deleteOrders(int guestID) throws SQLException {
        String query = "DELETE FROM OrderFood WHERE guest_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, guestID);
            stmt.executeUpdate();
        }
    }

    // Delete housekeeping services for the guest
    private static void deleteHousekeepingServices(int guestID) throws SQLException {
        String query = "DELETE FROM HouseKeepingService WHERE guest_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, guestID);
            stmt.executeUpdate();
        }
    }
	private static void showErrorAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
