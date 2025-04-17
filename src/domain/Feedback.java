package domain;

public class Feedback {
    private int id;
    private int guestId;
    private String service;
    private int rating;

    public Feedback(int id, int guestId, String service, int rating) {
        this.id = id;
        this.guestId = guestId;
        this.service = service;
        this.rating = rating;
    }

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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
