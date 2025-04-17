package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import domain.StockReport;
import domain.TablePayment;
import domain.Feedback;
import domain.HouseKeepingService;
import domain.RestaurantService;
import domain.RoomPayment;

public class dbHandler {
	
    private static String loggedInUserRole; // Store the role globally
    /////////////////////Staff Login/////////////////////
    public String authenticateUser(String username, String password) {
        String role = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT Role FROM Staff WHERE Username = ? AND Password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                role = rs.getString("Role"); // Get the role of the user
                loggedInUserRole = role; // Store the role
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return role;
    }
    public static String getLoggedInUserRole() {
        return loggedInUserRole;
    }
    ///////////////////////////View Feedback/////////////////////////////
    public ObservableList<Feedback> loadFeedbackData(String user) {
        ObservableList<Feedback> feedbackList = FXCollections.observableArrayList();
        String query = null;
        if("gym manager".equalsIgnoreCase(user))
        {
        	query = "SELECT * FROM Feedback where Service != 'Food Quality' and Service != 'Room Service'";
        }
        else if("crew".equalsIgnoreCase(user))
        {
        	query = "SELECT * FROM Feedback where Service != 'Food Quality' and Service != 'Gym Service'";
        }
        else if("restaurant staff".equalsIgnoreCase(user))
        {
        	query = "SELECT * FROM Feedback where Service != 'Room Service' and Service != 'Gym Service'";
        }
        else if("manager".equalsIgnoreCase(user))
        {
        	query = "SELECT * FROM Feedback";
        }
        try (Connection conn = DatabaseManager.getConnection();
        	Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                feedbackList.add(new Feedback(
                        rs.getInt("ID"),
                        rs.getInt("GuestID"),
                        rs.getString("Service"),
                        rs.getInt("Rating")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedbackList;
    }
    //////////////////////Report Stock/////////////////////////////
    public boolean addStockReport(String handler, String inventory, int quantity) {
        String query = "INSERT INTO reportStock (handler, inventory, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
        	PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, handler); // Role of the user
            stmt.setString(2, inventory); // Inventory item
            stmt.setInt(3, quantity); // Quantity

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Return true if the insertion was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //////////////////////////////Manage Rooms//////////////////////////////
    public static ObservableList<HouseKeepingService> loadRequestedServices() {
    	ObservableList<HouseKeepingService> serviceList = FXCollections.observableArrayList();
        String query = "SELECT * FROM HouseKeepingService";

        try (Connection conn = DatabaseManager.getConnection();
        	PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                serviceList.add(new HouseKeepingService(
                        rs.getInt("id"),
                        rs.getInt("Guest_id"),
                        rs.getInt("room_no"),
                        rs.getString("service_type")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceList;
    }
 // Check if the service ID exists in the database
    public static boolean isServiceIdValid(int serviceId) {
        String query = "SELECT COUNT(*) FROM HouseKeepingService WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
        	PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, serviceId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete the service with the given ID from the database
    public static boolean deleteService(int serviceId) {
        String query = "DELETE FROM HouseKeepingService WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
        	PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, serviceId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    ////////////////////////Handle Restaurant Services/////////////////////////
    public static ObservableList<RestaurantService> loadCheckedInOrders() {
        ObservableList<RestaurantService> orderList = FXCollections.observableArrayList();
        String query = "SELECT OrderFood.id, OrderFood.guest_id, Menu.ItemName, OrderFood.quantity " +
                       "FROM OrderFood " +
                       "JOIN Menu ON OrderFood.item_id = Menu.ItemId " +
                       "JOIN Guest ON OrderFood.guest_id = Guest.GuestID " +
                       "WHERE Guest.CheckinStatus = 'checkedin' and OrderFood.status = 'requested'";

        try (Connection conn=DatabaseManager.getConnection();
        	PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                orderList.add(new RestaurantService(
                        rs.getInt("id"),
                        rs.getInt("guest_id"),
                        rs.getString("ItemName"),  // This retrieves the item name
                        rs.getInt("quantity")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }   
    public static boolean isOrderValid(int orderId) {
    	String query = "SELECT COUNT(*) FROM OrderFood WHERE id = ? and status = 'requested'";

         try (Connection conn = DatabaseManager.getConnection();
        	PreparedStatement stmt = conn.prepareStatement(query)) {
             stmt.setInt(1, orderId);
             ResultSet rs = stmt.executeQuery();

             if (rs.next()) {
                 return rs.getInt(1) > 0;
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
    	
    	return false;
    }
    
    public static boolean UpdateOrder(int orderId) {
      	 String query = "Update OrderFood set status='provided' WHERE id = ?";

           try (Connection conn=DatabaseManager.getConnection();
          	PreparedStatement stmt = conn.prepareStatement(query)) {
               stmt.setInt(1, orderId);
               int rowsAffected = stmt.executeUpdate();
               return rowsAffected > 0;
           } catch (Exception e) {
               e.printStackTrace();
           }
      	return false;
      }
    ////////////////////////////////Update Inventory//////////////////////////////
    public ObservableList<StockReport> displayStockReport(String handler) {
    	ObservableList<StockReport> stockReports = FXCollections.observableArrayList();

        String query = "SELECT id, handler, inventory, quantity FROM reportStock WHERE handler = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, handler);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String handler1 = rs.getString("handler");
                String inventory = rs.getString("inventory");
                int quantity = rs.getInt("quantity");
                
                stockReports.add(new domain.StockReport(id, handler1, inventory, quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockReports;
    }
    public static boolean isStockIdValid(int serviceId, String handler1) throws SQLException {
    	String query = "SELECT COUNT(*) FROM reportStock WHERE id = ? and handler = ?";
        System.out.println("Checking stock ID validity...");
        
        Connection conn = DatabaseManager.getConnection(); // Ensure a valid connection
        if (conn == null) {
            System.out.println("Connection could not be established.");
            return false;
        }
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, serviceId);
            stmt.setString(2, handler1);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean removeStockReportById(int id, String handler1) {
    	
        String query = "DELETE FROM reportStock WHERE id = ? and handler = ?";
        try (Connection conn = DatabaseManager.getConnection();
        	PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, handler1);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Return true if a row was deleted
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
  /////////////////////////////View Bills///////////////////////////////
    public static List<RoomPayment> fetchRoomPayments() {
        List<RoomPayment> payments = new ArrayList<>();
        String query = "SELECT * FROM RoomPayment";

        try (Connection conn = DatabaseManager.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                payments.add(new RoomPayment(
                        resultSet.getInt("RoomNo"),
                        resultSet.getInt("Amount"),
                        resultSet.getInt("GuestID"),
                        resultSet.getInt("RoomPrice"),
                        resultSet.getInt("FoodPrice"),
                        resultSet.getString("PaymentDate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public static List<TablePayment> fetchTablePayments() {
        List<TablePayment> payments = new ArrayList<>();
        String query = "SELECT * FROM TablePayment";

        try (Connection conn = DatabaseManager.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                payments.add(new TablePayment(
                        resultSet.getInt("TableNo"),
                        resultSet.getInt("GuestID"),
                        resultSet.getInt("Amount"),
                        resultSet.getString("PaymentDate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

}