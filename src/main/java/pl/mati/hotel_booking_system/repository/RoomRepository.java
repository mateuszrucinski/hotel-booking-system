package pl.mati.hotel_booking_system.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.util.RoomState;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByState(RoomState state);
}
