package domain;

public class RestaurantService {
	private int id;
    private int guestid;
    private String item;
    private int quantity;

    // Constructor
    public RestaurantService(int id, int guestid, String item, int quantity) {
        this.id = id;
        this.guestid = guestid;
        this.item = item;
        this.quantity = quantity;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public int getGuestid() {
        return guestid;
    }

    public String getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setGuestid(int guestid) {
        this.guestid = guestid;
    }

    public void setItem(String item) {
        this.item = item;
    }
     
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}