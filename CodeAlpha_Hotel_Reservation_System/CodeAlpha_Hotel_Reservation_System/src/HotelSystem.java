import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class HotelSystem {
    private final List<Room> rooms=new ArrayList<>(), bookingsRooms=new ArrayList<>();
    private final List<Booking> bookings=new ArrayList<>();
    private final File dir=new File("data"), roomsFile=new File(dir,"rooms.txt"), bookingsFile=new File(dir,"bookings.txt");

    public HotelSystem(){dir.mkdirs(); seedRooms();}
    private void seedRooms(){
        rooms.clear();
        rooms.add(new Room("101","Standard",1800)); rooms.add(new Room("102","Standard",1800));
        rooms.add(new Room("103","Standard",2000)); rooms.add(new Room("201","Deluxe",3000));
        rooms.add(new Room("202","Deluxe",3200)); rooms.add(new Room("203","Deluxe",3500));
        rooms.add(new Room("301","Suite",5500)); rooms.add(new Room("302","Suite",6500));
        rooms.add(new Room("303","Suite",7500));
    }
    public void loadData(){loadRooms();loadBookings();}
    private void loadRooms(){
        if(!roomsFile.exists()){saveRooms();return;}
        try(BufferedReader br=new BufferedReader(new FileReader(roomsFile))){
            String s; while((s=br.readLine())!=null){String[] p=s.split("\\|",-1); if(p.length==4){
                Room r=findRoom(p[0]); if(r!=null)r.setAvailable(Boolean.parseBoolean(p[3]));
            }}
        }catch(Exception e){saveRooms();}
    }
    private void loadBookings(){
        bookings.clear(); if(!bookingsFile.exists()){saveBookings();return;}
        try(BufferedReader br=new BufferedReader(new FileReader(bookingsFile))){
            String s; while((s=br.readLine())!=null){String[] p=s.split("\\|",-1);
                if(p.length>=11) bookings.add(new Booking(p[0],p[1],p[2],p[3],p[4],LocalDate.parse(p[5]),LocalDate.parse(p[6]),
                    Double.parseDouble(p[7]),p[8],p[9],p[10]));
            }
        }catch(Exception ignored){}
    }
    public List<Room> searchRooms(String search,String category){
        List<Room> out=new ArrayList<>(); String s=search==null?"":search.trim().toLowerCase();
        for(Room r:rooms){
            boolean c=category==null||category.equals("All")||r.getCategory().equalsIgnoreCase(category);
            boolean q=s.isEmpty()||r.getRoomNumber().toLowerCase().contains(s)||r.getCategory().toLowerCase().contains(s)
                ||String.valueOf(r.getPricePerNight()).contains(s);
            if(c&&q)out.add(r);
        } return out;
    }
    public Room findRoom(String n){for(Room r:rooms)if(r.getRoomNumber().equals(n))return r;return null;}
    public Booking bookRoom(String room,String guest,String phone,LocalDate in,LocalDate out){
        Room r=findRoom(room); if(r==null||!r.isAvailable())throw new IllegalArgumentException("Room is not available.");
        long nights=java.time.temporal.ChronoUnit.DAYS.between(in,out);
        if(nights<=0)throw new IllegalArgumentException("Check-out must be after check-in.");
        String id="BK"+(1001+bookings.size());
        Booking b=new Booking(id,guest,phone,room,r.getCategory(),in,out,nights*r.getPricePerNight(),
            "PAID","CONFIRMED","Simulated Card Payment");
        bookings.add(b);r.setAvailable(false);saveRooms();saveBookings();return b;
    }
    public boolean cancelBooking(String id){
        Booking b=findBooking(id); if(b==null||b.getStatus().equals("CANCELLED"))return false;
        b.setStatus("CANCELLED");Room r=findRoom(b.getRoomNumber());if(r!=null)r.setAvailable(true);
        saveRooms();saveBookings();return true;
    }
    public Booking findBooking(String id){for(Booking b:bookings)if(b.getBookingId().equals(id))return b;return null;}
    public List<Booking> getBookings(){return Collections.unmodifiableList(bookings);}
    private void saveRooms(){try(PrintWriter w=new PrintWriter(new FileWriter(roomsFile))){
        for(Room r:rooms)w.println(r.getRoomNumber()+"|"+r.getCategory()+"|"+r.getPricePerNight()+"|"+r.isAvailable());
    }catch(IOException e){}}
    private void saveBookings(){try(PrintWriter w=new PrintWriter(new FileWriter(bookingsFile))){
        for(Booking b:bookings)w.println(String.join("|",clean(b.getBookingId()),clean(b.getGuestName()),clean(b.getPhone()),b.getRoomNumber(),
            b.getCategory(),b.getCheckIn().toString(),b.getCheckOut().toString(),String.valueOf(b.getTotalAmount()),
            b.getPaymentStatus(),b.getStatus(),clean(b.getPaymentMethod())));
    }catch(IOException e){}}
    private String clean(String s){return s.replace("|","/").replace("\n"," ");}
}
