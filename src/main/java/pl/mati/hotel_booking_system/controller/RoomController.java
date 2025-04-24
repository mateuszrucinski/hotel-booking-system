package pl.mati.hotel_booking_system.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAnyRole('GUEST', 'ADMIN')")
    public String getHelloWorld() {
        return "Hello World";
    }

    @GetMapping("/rooms")
    @PreAuthorize("hasAnyRole('GUEST', 'ADMIN')")
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @GetMapping("/rooms/available")
    @PreAuthorize("hasAnyRole('GUEST', 'ADMIN')")
    public List<Room> getAllAvailableRooms() {
        return roomService.getAllAvailableRooms();
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void addRoom(@RequestBody Room newRoom) {
        roomService.addRoom(newRoom);
    }
}
