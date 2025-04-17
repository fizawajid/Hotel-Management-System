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

import domain.Room;
import domain.Table;
import domain.TableBooking;
import domain.RoomBooking;

public class GuestDbhandler {

    public static boolean addBooking(Connection conn, int guestId, int roomNo, int noOfGuests, String checkinDate, String checkoutDate, String status) {
        String query = "INSERT INTO BookRoom (guest_id, room_no, no_of_guests, checkin_date, checkout_date, checkin_status) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, guestId);
            stmt.setInt(2, roomNo);
            stmt.setInt(3, noOfGuests);
            stmt.setString(4, checkinDate);
            stmt.setString(5, checkoutDate);
            stmt.setString(6, status);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
        public static int addGuest(String firstName, String lastName, String phoneNumber, String email, String bookingType, String checkinStatus, int noOfGuests, int roomNo, String checkinDate, String checkoutDate) {
            String query = "INSERT INTO Guest (FirstName, LastName, PhoneNumber, Email, BookingType, CheckinStatus) VALUES (?, ?, ?, ?, ?, ?)";
            
            try (Connection conn = DatabaseManager.getConnection();
            	PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, phoneNumber);
                stmt.setString(4, email);
                stmt.setString(5, bookingType); // Optional: Default as "Room Booking" or set dynamically
                stmt.setString(6, checkinStatus); // Optional: Default as "Pending" or set dynamically

                int rowsInserted = stmt.executeUpdate();

                // Check if insertion was successful and get the generated guest ID
                if (rowsInserted > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int guestId = generatedKeys.getInt(1); // Get the generated guest ID
                            // Now add the booking for this guest
                            //return addBooking(conn, guestId, roomNo, noOfGuests, checkinDate, checkoutDate, checkinStatus); // Pass the connection to the addBooking method
                            boolean bookingSuccess = addBooking(conn, guestId, roomNo, noOfGuests, checkinDate, checkoutDate, checkinStatus);
                            return bookingSuccess ? guestId : -1; // Return guestId if successful
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        }

    public static ObservableList<Room> getAvailableRooms(String checkinDate, String checkoutDate) {
    	
        ObservableList<Room> availableRooms = FXCollections.observableArrayList();
        String query = """
            SELECT r.RoomNo, r.RoomType, r.Price
			FROM Room r
			WHERE r.RoomNo NOT IN (
			    SELECT br.room_no
			    FROM BookRoom br
			    WHERE br.checkin_status IN ('checkedin', 'pending') 
			      AND NOT (
			          br.checkout_date <= ? OR br.checkin_date >= ?
			      )
			);

        """;

        try (Connection conn = DatabaseManager.getConnection();
        	PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, checkinDate);
            stmt.setString(2, checkoutDate);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int roomNo = rs.getInt("RoomNo");
                String roomType = rs.getString("RoomType");
                double price = rs.getDouble("Price");

                availableRooms.add(new Room(roomNo, roomType, price)); // Assuming you have a Room class
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return availableRooms;
    }

    public static boolean addBookingforTable(Connection conn, int guestId, int tableNo, int capacity, String starttime, String endtime, String reservationDate, String status) {
    	String bookTableQuery = "INSERT INTO BookTable (guest_id, table_no, no_of_guests, start_time, end_time, date_of_reservation, checkin_status) VALUES (?, ?, ?, ?, ?, ?, ?)";  
        try (PreparedStatement bookTableStmt = conn.prepareStatement(bookTableQuery)) {  
            bookTableStmt.setInt(1, guestId);  
            bookTableStmt.setInt(2, tableNo);  
            bookTableStmt.setInt(3, capacity);  
            bookTableStmt.setString(4, starttime);  
            bookTableStmt.setString(5, endtime);  
            bookTableStmt.setString(6, reservationDate);  
            bookTableStmt.setString(7, status);  // Fix: Set the value for checkin_status

            int tableRowsInserted = bookTableStmt.executeUpdate();  
            return tableRowsInserted > 0;  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return false;
    	
    }
    public static int addGuestfortable(String firstName, String lastName, String phoneNumber, String email, String bookingType, String checkinStatus, int capacity, int tableNo, String starttime, String endtime, String reservationDate) {
        String query = "INSERT INTO Guest (FirstName, LastName, PhoneNumber, Email, BookingType, CheckinStatus) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, phoneNumber);
            stmt.setString(4, email);
            stmt.setString(5, bookingType); // Optional: Default as "Room Booking" or set dynamically
            stmt.setString(6, checkinStatus); // Optional: Default as "Pending" or set dynamically

            int rowsInserted = stmt.executeUpdate();

            // Check if insertion was successful and get the generated guest ID
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int guestId = generatedKeys.getInt(1); // Get the generated guest ID
                        // Now add the booking for this guest
                        boolean bookingSuccess = addBookingforTable(conn, guestId, tableNo, capacity, starttime, endtime, reservationDate, checkinStatus);
                        return bookingSuccess ? guestId : -1; // Return guestId if successful
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if the operation fails
    }
    public static ObservableList<Table> getAvailableTables(String reservationDate, int capacity, String startReservationTime, String endReservationTime) {
        ObservableList<Table> availableTables = FXCollections.observableArrayList();
        String query = """
        		 SELECT t.TableNo, t.Capacity
            FROM TableInfo t
             WHERE t.Capacity >= ?
            AND t.TableNo NOT IN (
                SELECT bt.table_no
                FROM BookTable bt
                WHERE NOT (
                    bt.date_of_reservation >= ?  AND (
                            ? < bt.end_time AND 
                            ? > bt.start_time
                        )

                )
            );
            """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, capacity);
            stmt.setString(2, reservationDate);
           
            stmt.setString(3, endReservationTime); // Compare the requested end time
            stmt.setString(4, startReservationTime); // Compare the requested start time

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int tableNo = rs.getInt("TableNo");
                int tableCapacity = rs.getInt("Capacity");

                availableTables.add(new Table(tableNo, tableCapacity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return availableTables;
    }
    private static int cancelguestId;
    public static boolean verifyGuestFromDatabase(int guestId, String firstName, String lastName) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT 1 FROM Guest WHERE GuestID = ? AND FirstName = ? AND LastName = ? AND CheckinStatus = 'pending'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, guestId);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            cancelguestId=guestId;
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Return true if a match is found
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static int getCancelGuestId() {
    	return cancelguestId;
    }
    public static List<RoomBooking> getPendingBookings() {
        List<RoomBooking> bookings = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT id, guest_id, room_no, no_of_guests, checkin_status FROM BookRoom WHERE guest_id = ? AND checkin_status = 'pending'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, cancelguestId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int guestId = resultSet.getInt("guest_id");
                int roomNo = resultSet.getInt("room_no");
                int noOfGuests = resultSet.getInt("no_of_guests");
                String status = resultSet.getString("checkin_status");
                bookings.add(new RoomBooking(id, guestId, roomNo, noOfGuests, status));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public static boolean cancelBooking(int roomNo) {
        try (Connection connection = DatabaseManager.getConnection()) {
            // Check if the room booking exists
        	//System.out.println("Cancel Guest ID: " + cancelguestId);

            String checkQuery = "SELECT 1 FROM BookRoom WHERE guest_id = ? AND room_no = ? AND checkin_status = 'pending'";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, cancelguestId);
            checkStmt.setInt(2, roomNo);

            ResultSet resultSet = checkStmt.executeQuery();
            if (!resultSet.next()) {
                return false; // No matching booking found
            }

            // Delete the booking
            String deleteQuery = "DELETE FROM BookRoom WHERE guest_id = ? AND room_no = ?";
            PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, cancelguestId);
            deleteStmt.setInt(2, roomNo);
            deleteStmt.executeUpdate();

            // Check if the guest has any remaining bookings
            String checkRemainingQuery = "SELECT 1 FROM BookRoom WHERE guest_id = ?";
            PreparedStatement checkRemainingStmt = connection.prepareStatement(checkRemainingQuery);
            checkRemainingStmt.setInt(1, cancelguestId);

            ResultSet remainingResult = checkRemainingStmt.executeQuery();
            if (!remainingResult.next()) {
                // Delete guest if no bookings remain
                String deleteGuestQuery = "DELETE FROM Guest WHERE GuestID = ?";
                PreparedStatement deleteGuestStmt = connection.prepareStatement(deleteGuestQuery);
                deleteGuestStmt.setInt(1, cancelguestId);
                deleteGuestStmt.executeUpdate();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<TableBooking> getPendingTableBookings() {
        List<TableBooking> bookings = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection()) {
            String query = "SELECT id, guest_id, table_no, no_of_guests, checkin_status FROM BookTable WHERE guest_id = ? AND checkin_status = 'pending'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, cancelguestId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int guestId = resultSet.getInt("guest_id");
                int tableNo = resultSet.getInt("table_no");
                int noOfGuests = resultSet.getInt("no_of_guests");
                String status = resultSet.getString("checkin_status");
                bookings.add(new TableBooking(id, guestId, tableNo, noOfGuests, status));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public static boolean cancelTableBooking(int tableNo) {
        try (Connection connection = DatabaseManager.getConnection()) {
            // Check if the room booking exists
        	//System.out.println("Cancel Guest ID: " + cancelguestId);

            String checkQuery = "SELECT 1 FROM BookTable WHERE guest_id = ? AND table_no = ? AND checkin_status = 'pending'";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, cancelguestId);
            checkStmt.setInt(2, tableNo);

            ResultSet resultSet = checkStmt.executeQuery();
            if (!resultSet.next()) {
                return false; // No matching booking found
            }
            
            // Delete dependent records in OrderFood (if any)
            String deleteOrderFoodQuery = "DELETE FROM OrderFood WHERE guest_id = ?";
            PreparedStatement deleteOrderFoodStmt = connection.prepareStatement(deleteOrderFoodQuery);
            deleteOrderFoodStmt.setInt(1, cancelguestId);
            deleteOrderFoodStmt.executeUpdate();
            
            // Delete the booking
            String deleteQuery = "DELETE FROM BookTable WHERE guest_id = ? AND table_no = ?";
            PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, cancelguestId);
            deleteStmt.setInt(2, tableNo);
            deleteStmt.executeUpdate();

            // Check if the guest has any remaining bookings
            String checkRemainingQuery = "SELECT 1 FROM BookTable WHERE guest_id = ?";
            PreparedStatement checkRemainingStmt = connection.prepareStatement(checkRemainingQuery);
            checkRemainingStmt.setInt(1, cancelguestId);

            ResultSet remainingResult = checkRemainingStmt.executeQuery();
            if (!remainingResult.next()) {
                // Delete guest if no bookings remain
                String deleteGuestQuery = "DELETE FROM Guest WHERE GuestID = ?";
                PreparedStatement deleteGuestStmt = connection.prepareStatement(deleteGuestQuery);
                deleteGuestStmt.setInt(1, cancelguestId);
                deleteGuestStmt.executeUpdate();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }	
}
