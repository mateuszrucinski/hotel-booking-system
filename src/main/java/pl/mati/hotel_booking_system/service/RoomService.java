package pl.mati.hotel_booking_system.service;

import org.springframework.stereotype.Service;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.repository.RoomRepository;
import pl.mati.hotel_booking_system.util.RoomState;
import pl.mati.hotel_booking_system.util.RoomType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public List<Room> getFilteredRooms(RoomType inputRoomType, int minimumPrice, int maximumPrice) {
        return getAllAvailableRooms().stream()
                .filter(room -> inputRoomType == null || room.getRoomType() == inputRoomType)
                .filter(room -> room.getPrice() >= minimumPrice)
                .filter(room -> maximumPrice == 0 || room.getPrice() <= maximumPrice)
                .toList();
    }

    public void addRoom(Room newRoom) {
        roomRepository.save(newRoom);
    }
}
