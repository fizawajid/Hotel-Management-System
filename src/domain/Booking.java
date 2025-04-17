package domain;

public abstract class Booking {
    private int id;
    private int guestId;
    private int noOfGuests;
    private String status;

    // Constructor
    public Booking(int id, int guestId, int noOfGuests, String status) {
        this.id = id;
        this.guestId = guestId;
        this.noOfGuests = noOfGuests;
        this.status = status;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getGuestId() {
        return guestId;
    }

    public int getNoOfGuests() {
        return noOfGuests;
    }

    public String getStatus() {
        return status;
    }
}