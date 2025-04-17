package domain;

public class TablePayment {
    private int tableNo, guestId, amount;
    private String paymentDate;

    public TablePayment(int tableNo, int guestId, int amount, String paymentDate) {
        this.tableNo = tableNo;
        this.guestId = guestId;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    // Getters and setters
    public int getTableNo() { return tableNo; }
    public void setTableNo(int tableNo) { this.tableNo = tableNo; }
    public int getGuestId() { return guestId; }
    public void setGuestId(int guestId) { this.guestId = guestId; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
}