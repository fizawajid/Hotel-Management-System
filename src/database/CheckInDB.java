package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import domain.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CheckInDB {

	 private static Integer loggedInGuestId; // Store the guest ID globally (nullable)

	 public static void authenticateGuest(Integer guestid) {
	     loggedInGuestId = guestid;
	     return; 
	 }

	 public static Integer getLoggedInGuestId() {
	     return loggedInGuestId;
	 }
	 
	public static boolean verifyGuestInfo(int guestId, String guestName) {
	    String query = "SELECT COUNT(*) FROM Guest WHERE GuestID = ? AND CONCAT(FirstName, ' ', LastName) = ?";
	    try (Connection conn=DatabaseManager.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setInt(1, guestId);
	        stmt.setString(2, guestName);
	
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public static boolean hasRoomBooking(int guestId) {
	        
        String query = "SELECT COUNT(*) FROM BookRoom WHERE guest_id = ?";
        try (Connection conn=DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, guestId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
       
        return false;
    }
	
	public static boolean hasTableReservation(int guestId) {
		
        
        String query = "SELECT COUNT(*) FROM BookTable WHERE guest_id = ?";
        try (Connection conn=DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, guestId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
	
	public static LocalTime getStartTime(int guestId) {
        String query = "SELECT start_time FROM BookTable WHERE guest_id = ?";
        try (Connection conn=DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, guestId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getTime("start_time").toLocalTime();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	 public static LocalTime getEndTime(int guestId) {
	        String query = "SELECT end_time FROM BookTable WHERE guest_id = ? ";
	        try (Connection conn=DatabaseManager.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, guestId);

	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                return rs.getTime("end_time").toLocalTime();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	 public static LocalDate getReservationDate(int guestId) {
		    String query = "SELECT date_of_reservation FROM BookTable WHERE guest_id = ?";
		    try (Connection connection = DatabaseManager.getConnection();
		         PreparedStatement ps = connection.prepareStatement(query)) {
		        ps.setInt(1, guestId);
		        ResultSet rs = ps.executeQuery();
		        if (rs.next()) {
		            return rs.getDate("date_of_reservation").toLocalDate();
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return null;
		}
	 public static boolean updateTableCheckInStatus(int guestId) {
		 String updateTableQuery = "UPDATE BookTable SET checkin_status = 'checkedin' WHERE guest_id = ? AND checkin_status = 'pending'";
		 String updateGuestQuery = "UPDATE Guest SET CheckinStatus = 'checkedin' WHERE GuestID = ?";

		    try (Connection conn = DatabaseManager.getConnection()) {
		        // Disable auto-commit for transaction
		        conn.setAutoCommit(false);

		        try (PreparedStatement stmtTable = conn.prepareStatement(updateTableQuery);
		             PreparedStatement stmtGuest = conn.prepareStatement(updateGuestQuery)) {

		            // Update BookTable
		            stmtTable.setInt(1, guestId);
		            int tableRowsAffected = stmtTable.executeUpdate();

		            // Update Guest
		            stmtGuest.setInt(1, guestId);
		            int guestRowsAffected = stmtGuest.executeUpdate();

		            // Commit if successful
		            if (tableRowsAffected > 0 && guestRowsAffected > 0) {
		                conn.commit();
		                return true;
		            } else {
		                conn.rollback();
		            }
		        } catch (SQLException e) {
		            conn.rollback();
		            e.printStackTrace();
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return false;
	    }
	
	public static String getBookingStatus(int guestId) {
        String query = "SELECT CheckinStatus FROM Guest WHERE GuestID = ?";
        try (Connection conn=DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, guestId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("CheckinStatus");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static String getBookingType(int guestId) {
        String query = "SELECT BookingType FROM Guest WHERE GuestID = ?";
        try (Connection conn=DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, guestId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("BookingType");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	
	public static LocalDate getCheckInDate(int guestId) {
        String query = "SELECT checkin_date FROM BookRoom WHERE guest_id = ? ";
        try (Connection conn=DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, guestId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDate("checkin_date").toLocalDate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static LocalDate getCheckOutDate(int guestId) {
        String query = "SELECT checkout_date FROM BookRoom WHERE guest_id = ?";
        try (Connection conn=DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, guestId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDate("checkout_date").toLocalDate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	 public static boolean updateRoomCheckInStatus(int guestId) {
		 String updateRoomQuery = "UPDATE BookRoom SET checkin_status = 'checkedin' WHERE guest_id = ? AND checkin_status = 'pending'";
		    String updateGuestQuery = "UPDATE Guest SET CheckinStatus = 'checkedin' WHERE GuestID = ?";

		    try (Connection conn = DatabaseManager.getConnection()) {
		        // Disable auto-commit for transaction
		        conn.setAutoCommit(false);

		        try (PreparedStatement stmtRoom = conn.prepareStatement(updateRoomQuery);
		             PreparedStatement stmtGuest = conn.prepareStatement(updateGuestQuery)) {

		            // Update BookRoom
		            stmtRoom.setInt(1, guestId);
		            int roomRowsAffected = stmtRoom.executeUpdate();

		            // Update Guest
		            stmtGuest.setInt(1, guestId);
		            int guestRowsAffected = stmtGuest.executeUpdate();

		            // Commit if successful
		            if (roomRowsAffected > 0 && guestRowsAffected > 0) {
		                conn.commit();
		                return true;
		            } else {
		                conn.rollback();
		            }
		        } catch (SQLException e) {
		            conn.rollback();
		            e.printStackTrace();
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    
		    return false;
	    }
  	//housekeeping service
  	 public static int getRoomNoByGuestId(int guestId) throws SQLException {
  		    String query = "SELECT room_no FROM BookRoom WHERE guest_id = ?";
  		    try (Connection conn = DatabaseManager.getConnection();
  			    	PreparedStatement stmt = conn.prepareStatement(query)) {
  		        stmt.setInt(1, guestId);
  		        try (ResultSet rs = stmt.executeQuery()) {
  		            if (rs.next()) {
  		                return rs.getInt("room_no");
  		            }
  		        }
  		    }
  		    return -1; // Return -1 if no room number is found
  		}
  	 
  	public static void insertHouseKeepingRequest(int guestid,int roomNo, String serviceType) throws SQLException {
	    String query = "INSERT INTO HouseKeepingService (Guest_id, room_no, service_type) VALUES (?,?, ?)";
	    try (Connection conn = DatabaseManager.getConnection();
		    	PreparedStatement ps = conn.prepareStatement(query)) {
	    	ps.setInt(1, guestid);
	        ps.setInt(2, roomNo);
	        ps.setString(3, serviceType);
	        ps.executeUpdate();
	    }			    
	}
  	
 public static boolean isServiceRequestPending(int guestid,int roomNo, String serviceType) throws SQLException {
        String query = "SELECT COUNT(*) FROM HousekeepingService WHERE Guest_id = ? AND room_no = ? AND service_type = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
        	stmt.setInt(1, guestid);
            stmt.setInt(2, roomNo);
            stmt.setString(3, serviceType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // If there is at least one pending request for this service
                }
            }
        }
        return false; // No pending request found
    }

  //order food
  	 public static ObservableList<MenuItem> getMenuItems() throws SQLException {
  		    ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();
  		    String query = "SELECT * FROM Menu"; // Adjust this query as per your database schema

  		    try (Connection conn = DatabaseManager.getConnection();
  		         Statement stmt = conn.createStatement();
  		         ResultSet rs = stmt.executeQuery(query)) {

  		        while (rs.next()) {
  		            int itemId = rs.getInt("ItemId");
  		            String itemName = rs.getString("ItemName");
  		            double price = rs.getDouble("Price");
  		            String category = rs.getString("Category");

  		            MenuItem menuItem = new MenuItem(itemId, itemName, price, category);
  		            menuItems.add(menuItem);
  		        }
  		    } catch (SQLException e) {
  		        e.printStackTrace();
  		        throw new SQLException("Failed to fetch menu items.", e);
  		    }

  		    return menuItems;
  		}

  	public static boolean insertFoodOrder(int guestId, int itemId, int quantity) throws SQLException {
		 String query = "INSERT INTO OrderFood (guest_id, item_id, quantity,status) VALUES (?, ?, ?,'requested')";
		    try (Connection conn = DatabaseManager.getConnection();
		         PreparedStatement stmt = conn.prepareStatement(query)) {
		        stmt.setInt(1, guestId);
		        stmt.setInt(2, itemId);
		        stmt.setInt(3, quantity);
		        return stmt.executeUpdate() > 0; // Returns true if insertion is successful
		    }
		}
  	
  	 public static boolean isMenuItemValid(int itemId) throws SQLException {
  		    String query = "SELECT COUNT(*) FROM Menu WHERE ItemId = ?";
  		    try (Connection conn = DatabaseManager.getConnection();
  		         PreparedStatement stmt = conn.prepareStatement(query)) {
  		        stmt.setInt(1, itemId);
  		        ResultSet rs = stmt.executeQuery();
  		        if (rs.next()) {
  		            return rs.getInt(1) > 0; // Returns true if item exists
  		        }
  		    }
  		    return false;
  		}
}
