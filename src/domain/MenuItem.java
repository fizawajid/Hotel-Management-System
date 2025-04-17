package domain;

public class MenuItem {
	    private int itemId;
	    private String itemName;
	    private double price;
	    private String category;

	    public MenuItem(int itemId, String itemName, double price, String category) {
	        this.itemId = itemId;
	        this.itemName = itemName;
	        this.price = price;
	        this.category = category;
	    }

	    // Getters and setters
	    public int getItemId() { return itemId; }
	    public void setItemId(int itemId) { this.itemId = itemId; }

	    public String getItemName() { return itemName; }
	    public void setItemName(String itemName) { this.itemName = itemName; }

	    public double getPrice() { return price; }
	    public void setPrice(double price) { this.price = price; }

	    public String getCategory() { return category; }
	    public void setCategory(String category) { this.category = category; }
}