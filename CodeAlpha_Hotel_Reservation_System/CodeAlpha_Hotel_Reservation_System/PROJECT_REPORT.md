# CodeAlpha Java Programming Internship - Task 4
## Hotel Reservation System

### Objective
Build a Java system to search, book and manage hotel rooms with categories, cancellation, payment simulation and booking details.

### Features
- Room search and category filter
- Standard, Deluxe and Suite rooms
- Availability tracking
- Reservation creation
- Total amount calculation
- Simulated payment
- Booking details
- Reservation cancellation
- File-based persistence
- OOP architecture

### OOP
`Room` and `Booking` are model classes. `HotelSystem` handles business logic and File I/O. `HotelReservationApp` provides the Swing GUI. Private fields with getters/setters demonstrate encapsulation and responsibilities are separated between classes.

### File I/O
`data/rooms.txt` stores room availability. `data/bookings.txt` stores booking records. Files are created automatically.

### Test flow
1. Start application.
2. Search/filter rooms.
3. Select Available room.
4. Enter guest name, phone and dates.
5. Confirm simulated payment.
6. Open My Reservations and view details.
7. Cancel a reservation and verify room becomes Available.

### Limitations
Payment is simulated; this project does not process real money. File storage is intended for an internship project rather than high-concurrency production use.

### Future scope
MySQL integration, authentication, admin dashboard, real payment gateway, email/SMS confirmation and web deployment.
