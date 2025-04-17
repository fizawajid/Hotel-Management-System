package domain;

public class RoomPayment {
    private int roomNo, amount, guestId, roomPrice, foodPrice;
    private String paymentDate;

    public RoomPayment(int roomNo, int amount, int guestId, int roomPrice, int foodPrice, String paymentDate) {
        this.roomNo = roomNo;
        this.amount = amount;
        this.guestId = guestId;
        this.roomPrice = roomPrice;
        this.foodPrice = foodPrice;
        this.paymentDate = paymentDate;
    }

    // Getters and setters
    public int getRoomNo() { return roomNo; }
    public void setRoomNo(int roomNo) { this.roomNo = roomNo; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public int getGuestId() { return guestId; }
    public void setGuestId(int guestId) { this.guestId = guestId; }
    public int getRoomPrice() { return roomPrice; }
    public void setRoomPrice(int roomPrice) { this.roomPrice = roomPrice; }
    public int getFoodPrice() { return foodPrice; }
    public void setFoodPrice(int foodPrice) { this.foodPrice = foodPrice; }
    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
}