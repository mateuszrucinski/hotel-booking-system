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
        //example of polymorphism
        Set<Room> filteredRoomsSet = new HashSet<>();

        if (inputRoomType != null) {
            filteredRoomsSet.addAll(getAvailableRoomsByRoomType(inputRoomType));
        }

        if (minimumPrice != 0 || maximumPrice != 0) {
            filteredRoomsSet.addAll(getAvailableRoomsByPriceRange(minimumPrice, maximumPrice));
        }

        return filteredRoomsSet.stream().toList();
    }


    private List<Room> getAvailableRoomsByRoomType(RoomType inputRoomType) {
        List<Room> allAvailableRooms = getAllAvailableRooms();

        List<Room> inputRoomTypeAvailableRooms = allAvailableRooms.stream()
                .filter(room -> room.getRoomType() == inputRoomType)
                .toList();
        return inputRoomTypeAvailableRooms;
    }

    private List<Room> getAvailableRoomsByPriceRange(int minimumPrice, int maximumPrice) {
        List<Room> allAvailableRooms = getAllAvailableRooms();

        List<Room> priceRageAvailableRooms;
        if (maximumPrice != 0) {
            priceRageAvailableRooms = allAvailableRooms.stream()
                    .filter(room -> room.getPrice() >= minimumPrice &&
                            room.getPrice() <= maximumPrice
                    ).toList();
        } else {
            priceRageAvailableRooms = allAvailableRooms.stream()
                    .filter(room -> room.getPrice() >= minimumPrice
                    ).toList();
        }
        return priceRageAvailableRooms;
    }


    public void addRoom(Room newRoom) {
        roomRepository.save(newRoom);
    }
}
