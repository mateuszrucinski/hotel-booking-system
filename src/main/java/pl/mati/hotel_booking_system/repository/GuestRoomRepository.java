package pl.mati.hotel_booking_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mati.hotel_booking_system.entity.GuestRoom;

public interface GuestRoomRepository extends JpaRepository<GuestRoom, Long> {
}
