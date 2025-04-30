package pl.mati.hotel_booking_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mati.hotel_booking_system.entity.GuestRoom;
import pl.mati.hotel_booking_system.entity.HotelUser;
import pl.mati.hotel_booking_system.entity.Room;

import java.util.List;
import java.util.Optional;

public interface GuestRoomRepository extends JpaRepository<GuestRoom, Long> {
    List<GuestRoom> findAllByGuestAndIsCheckOutFalse(HotelUser user);
    List<GuestRoom> findAllByRoom(Room room);
    Optional<GuestRoom> findByGuestAndRoomAndIsCheckInFalse(HotelUser guest, Room room);
    Optional<GuestRoom> findByGuestAndRoomAndIsCheckOutFalse(HotelUser guest, Room room);
}
