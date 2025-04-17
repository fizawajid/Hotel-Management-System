package domain;

public class HouseKeepingService {
    private int id;
    private int guestId; // Changed field name to match Java naming conventions
    private int roomNo;
    private String serviceType;

    // Constructor
    public HouseKeepingService(int id, int guestId, int roomNo, String serviceType) {
        this.id = id;
        this.guestId = guestId;
        this.roomNo = roomNo;
        this.serviceType = serviceType;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuestId() { 
        return guestId;
    }

    public void setGuestId(int guestId) { 
        this.guestId = guestId;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}