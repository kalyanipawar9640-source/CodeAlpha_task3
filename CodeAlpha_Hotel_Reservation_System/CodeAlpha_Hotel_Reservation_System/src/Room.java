public class Room {
    private final String roomNumber, category;
    private final double pricePerNight;
    private boolean available = true;
    public Room(String roomNumber, String category, double pricePerNight) {
        this.roomNumber = roomNumber; this.category = category; this.pricePerNight = pricePerNight;
    }
    public String getRoomNumber(){ return roomNumber; }
    public String getCategory(){ return category; }
    public double getPricePerNight(){ return pricePerNight; }
    public boolean isAvailable(){ return available; }
    public void setAvailable(boolean available){ this.available = available; }
}
