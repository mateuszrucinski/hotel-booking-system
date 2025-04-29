package pl.mati.hotel_booking_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mati.hotel_booking_system.entity.GuestRoom;
import pl.mati.hotel_booking_system.entity.HotelUser;

import java.util.List;

public interface GuestRoomRepository extends JpaRepository<GuestRoom, Long> {
    List<GuestRoom> findAllByGuest(HotelUser user);
}
