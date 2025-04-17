package domain;

public class FoodItem {
    private String item;
    private String category;
    private double itemPrice;
    private int quantity;
    private double totalPrice;

    public FoodItem(String item, String category, double itemPrice, int quantity, double totalPrice) {
        this.item = item;
        this.category = category;
        this.itemPrice = itemPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getItem() {
        return item;
    }

    public String getCategory() {
        return category;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
