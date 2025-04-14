package pl.mati.hotel_booking_system.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.repository.RoomRepository;
import pl.mati.hotel_booking_system.util.RoomState;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAnyRole('GUEST', 'ADMIN')")
    public String getHelloWorld() {
        return "Hello World";
    }

    @GetMapping("/rooms")
    @PreAuthorize("hasAnyRole('GUEST', 'ADMIN')")
    public List<Room> getAllRooms() {
        List<Room> allRooms = roomRepository.findAll();
        return allRooms;
    }

    @GetMapping("/rooms/available")
    @PreAuthorize("hasAnyRole('GUEST', 'ADMIN')")
    public List<Room> getAllAvailableRooms() {
        List<Room> allAvailableRooms = roomRepository.findAllByState(RoomState.AVAILABLE);
        return allAvailableRooms;
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void addRoom(@RequestBody Room newRoom) {
        roomRepository.save(newRoom);
    }
}
