package domain;

public class RoomBooking extends Booking {
    private int roomNo;

    // Constructor
    public RoomBooking(int id, int guestId, int roomNo, int noOfGuests, String status) {
        super(id, guestId, noOfGuests, status);
        this.roomNo = roomNo;
    }

    // Getter
    public int getRoomNo() {
        return roomNo;
    }
}