package pl.mati.hotel_booking_system.excpetion;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(Long roomId) {
        super("Room with ID " + roomId + " not found.");
    }
}