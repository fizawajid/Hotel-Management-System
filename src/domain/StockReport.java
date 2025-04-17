package domain;


public class StockReport {
	private int id;
    private String handler;
    private String inventory;
    private int quantity;

    public StockReport(int id, String handler, String inventory, int quantity) {
    	this.id = id;
        this.handler = handler;
        this.inventory = inventory;
        this.quantity = quantity;
    }

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

