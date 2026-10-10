import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HotelReservationApp extends JFrame {
    private final HotelSystem system=new HotelSystem();
    private final DefaultTableModel roomModel=new DefaultTableModel(new Object[]{"Room","Category","Price/Night","Status"},0);
    private final DefaultTableModel bookingModel=new DefaultTableModel(new Object[]{"Booking ID","Guest","Room","Category","Check-in","Check-out","Amount","Payment"},0);
    private final JTable roomTable=new JTable(roomModel), bookingTable=new JTable(bookingModel);
    private final DateTimeFormatter DATE=DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public HotelReservationApp(){
        system.loadData(); setTitle("CodeAlpha - Hotel Reservation System");setSize(1050,680);setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel root=new JPanel(new BorderLayout(10,10));root.setBorder(new EmptyBorder(12,12,12,12));setContentPane(root);
        JLabel title=new JLabel("🏨 CodeAlpha Hotel Reservation System");title.setFont(new Font("SansSerif",Font.BOLD,24));root.add(title,BorderLayout.NORTH);
        JTabbedPane tabs=new JTabbedPane();

        JPanel rooms=new JPanel(new BorderLayout(8,8));JPanel top=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField search=new JTextField(16);JComboBox<String> cat=new JComboBox<>(new String[]{"All","Standard","Deluxe","Suite"});
        JButton find=new JButton("Search"),refresh=new JButton("Refresh"),book=new JButton("Book Selected Room");
        top.add(new JLabel("Search:"));top.add(search);top.add(new JLabel("Category:"));top.add(cat);top.add(find);top.add(refresh);top.add(book);
        rooms.add(top,BorderLayout.NORTH);roomTable.setRowHeight(25);rooms.add(new JScrollPane(roomTable),BorderLayout.CENTER);tabs.addTab("Search & Rooms",rooms);

        JPanel bs=new JPanel(new BorderLayout(8,8));JPanel bb=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton details=new JButton("View Booking Details"),cancel=new JButton("Cancel Selected Reservation"),br=new JButton("Refresh");
        bb.add(details);bb.add(cancel);bb.add(br);bs.add(bb,BorderLayout.NORTH);bookingTable.setRowHeight(25);bs.add(new JScrollPane(bookingTable),BorderLayout.CENTER);tabs.addTab("My Reservations",bs);

        JTextArea about=new JTextArea("CodeAlpha Java Programming Task 4\n\nHotel Reservation System\n\n"+
            "Features:\n• Search and categorize rooms\n• Book and cancel reservations\n• Booking details view\n• Simulated payment\n• OOP + File I/O\n\nNo external database/library is required.");
        about.setEditable(false);about.setFont(new Font("SansSerif",Font.PLAIN,16));about.setBorder(new EmptyBorder(20,20,20,20));tabs.addTab("About",new JScrollPane(about));
        root.add(tabs,BorderLayout.CENTER);

        refreshRooms();
        find.addActionListener(e->refreshRooms(search.getText(),(String)cat.getSelectedItem()));
        refresh.addActionListener(e->{search.setText("");cat.setSelectedItem("All");refreshRooms();});
        book.addActionListener(e->bookRoom());br.addActionListener(e->refreshBookings());
        details.addActionListener(e->showDetails());cancel.addActionListener(e->cancelBooking());
    }
    private void refreshRooms(){refreshRooms("","All");}
    private void refreshRooms(String s,String c){roomModel.setRowCount(0);for(Room r:system.searchRooms(s,c))
        roomModel.addRow(new Object[]{r.getRoomNumber(),r.getCategory(),"₹"+r.getPricePerNight(),r.isAvailable()?"Available":"Booked"});}
    private void refreshBookings(){bookingModel.setRowCount(0);for(Booking b:system.getBookings())
        bookingModel.addRow(new Object[]{b.getBookingId(),b.getGuestName(),b.getRoomNumber(),b.getCategory(),
            b.getCheckIn().format(DATE),b.getCheckOut().format(DATE),"₹"+b.getTotalAmount(),b.getPaymentStatus()});}
    private void bookRoom(){
        int row=roomTable.getSelectedRow();if(row<0){msg("Select an available room first.");return;}
        String rn=roomModel.getValueAt(row,0).toString();Room r=system.findRoom(rn);if(r==null||!r.isAvailable()){msg("Room is not available.");return;}
        JTextField g=new JTextField(),p=new JTextField(),in=new JTextField(LocalDate.now().format(DATE)),out=new JTextField(LocalDate.now().plusDays(1).format(DATE));
        JPanel f=new JPanel(new GridLayout(0,2,8,8));f.add(new JLabel("Guest Name:"));f.add(g);f.add(new JLabel("Phone:"));f.add(p);
        f.add(new JLabel("Check-in (dd-MM-yyyy):"));f.add(in);f.add(new JLabel("Check-out (dd-MM-yyyy):"));f.add(out);
        if(JOptionPane.showConfirmDialog(this,f,"Book Room "+rn,JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;
        try{
            if(g.getText().trim().isEmpty()||p.getText().trim().isEmpty())throw new IllegalArgumentException("Guest name and phone are required.");
            LocalDate ci=LocalDate.parse(in.getText().trim(),DATE),co=LocalDate.parse(out.getText().trim(),DATE);
            long nights=java.time.temporal.ChronoUnit.DAYS.between(ci,co);if(nights<=0)throw new IllegalArgumentException("Check-out must be after check-in.");
            double amount=nights*r.getPricePerNight();
            if(JOptionPane.showConfirmDialog(this,"Total amount: ₹"+amount+"\n\nProceed with simulated payment?","Payment Simulation",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
            Booking b=system.bookRoom(rn,g.getText().trim(),p.getText().trim(),ci,co);
            JOptionPane.showMessageDialog(this,"Booking successful!\n\nBooking ID: "+b.getBookingId()+"\nRoom: "+b.getRoomNumber()+"\nAmount Paid: ₹"+b.getTotalAmount());
            refreshRooms();refreshBookings();
        }catch(Exception ex){msg(ex.getMessage());}
    }
    private void showDetails(){
        int row=bookingTable.getSelectedRow();if(row<0){msg("Select a reservation first.");return;}
        Booking b=system.findBooking(bookingModel.getValueAt(row,0).toString());if(b==null)return;
        JTextArea t=new JTextArea("BOOKING DETAILS\n\nBooking ID: "+b.getBookingId()+"\nGuest: "+b.getGuestName()+"\nPhone: "+b.getPhone()+
            "\nRoom: "+b.getRoomNumber()+"\nCategory: "+b.getCategory()+"\nCheck-in: "+b.getCheckIn().format(DATE)+
            "\nCheck-out: "+b.getCheckOut().format(DATE)+"\nTotal: ₹"+b.getTotalAmount()+"\nPayment: "+b.getPaymentStatus()+
            "\nStatus: "+b.getStatus());t.setEditable(false);JOptionPane.showMessageDialog(this,t,"Booking Details",JOptionPane.INFORMATION_MESSAGE);
    }
    private void cancelBooking(){
        int row=bookingTable.getSelectedRow();if(row<0){msg("Select a reservation first.");return;}
        String id=bookingModel.getValueAt(row,0).toString();
        if(JOptionPane.showConfirmDialog(this,"Cancel reservation "+id+"?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            if(system.cancelBooking(id)){msg("Reservation cancelled successfully.");refreshBookings();refreshRooms();}else msg("Could not cancel reservation.");
        }
    }
    private void msg(String s){JOptionPane.showMessageDialog(this,s);}
    public static void main(String[] args){SwingUtilities.invokeLater(()->new HotelReservationApp().setVisible(true));}
}
