package pl.mati.hotel_booking_system.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.repository.RoomRepository;
import pl.mati.hotel_booking_system.util.RoomState;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        List<Room> allRooms = roomRepository.findAll();
        return allRooms;
    }

    public List<Room> getAllAvailableRooms() {
        List<Room> allAvailableRooms = roomRepository.findAllByState(RoomState.AVAILABLE);
        return allAvailableRooms;
    }

    public void addRoom(@RequestBody Room newRoom) {
        roomRepository.save(newRoom);
    }
}
