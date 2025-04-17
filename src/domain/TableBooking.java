package domain;

public class TableBooking extends Booking {
    private int tableNo;

    // Constructor
    public TableBooking(int id, int guestId, int tableNo, int noOfGuests, String status) {
        super(id, guestId, noOfGuests, status);
        this.tableNo = tableNo;
    }

    // Getter
    public int getTableNo() {
        return tableNo;
    }
}