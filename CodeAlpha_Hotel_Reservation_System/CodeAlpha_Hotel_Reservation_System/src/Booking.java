import java.time.LocalDate;
public class Booking {
    private final String id, guest, phone, room, category, paymentMethod;
    private final LocalDate checkIn, checkOut;
    private final double amount;
    private final String paymentStatus;
    private String status;
    public Booking(String id,String guest,String phone,String room,String category,LocalDate in,LocalDate out,
                   double amount,String paymentStatus,String status,String paymentMethod){
        this.id=id;this.guest=guest;this.phone=phone;this.room=room;this.category=category;
        this.checkIn=in;this.checkOut=out;this.amount=amount;this.paymentStatus=paymentStatus;
        this.status=status;this.paymentMethod=paymentMethod;
    }
    public String getBookingId(){return id;} public String getGuestName(){return guest;} public String getPhone(){return phone;}
    public String getRoomNumber(){return room;} public String getCategory(){return category;}
    public LocalDate getCheckIn(){return checkIn;} public LocalDate getCheckOut(){return checkOut;}
    public double getTotalAmount(){return amount;} public String getPaymentStatus(){return paymentStatus;}
    public String getStatus(){return status;} public void setStatus(String s){status=s;}
    public String getPaymentMethod(){return paymentMethod;}
}
