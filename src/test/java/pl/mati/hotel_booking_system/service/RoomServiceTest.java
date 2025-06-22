package pl.mati.hotel_booking_system.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.excpetion.RoomNotFoundException;
import pl.mati.hotel_booking_system.repository.RoomRepository;
import pl.mati.hotel_booking_system.util.RoomState;
import pl.mati.hotel_booking_system.util.RoomType;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    private RoomRepository roomRepository;
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        roomRepository = mock(RoomRepository.class);
        roomService = new RoomService(roomRepository);
    }

    @Test
    void testGetRoomById() {
        Room room = new Room();
        room.setRoomId(1L);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Room result = roomService.getRoomById(1L);

        assertEquals(1L, result.getRoomId());
        verify(roomRepository).findById(1L);
    }

    @Test
    void testGetRoomByIdThrowsException() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.getRoomById(1L));
    }

    @Test
    void testGetAllRooms() {
        List<Room> rooms = List.of(new Room(), new Room());

        when(roomRepository.findAll()).thenReturn(rooms);

        List<Room> result = roomService.getAllRooms();

        assertEquals(2, result.size());
        verify(roomRepository).findAll();
    }

    @Test
    void testGetAllAvailableRooms() {
        List<Room> rooms = List.of(new Room(), new Room());

        when(roomRepository.findAllByState(RoomState.AVAILABLE)).thenReturn(rooms);

        List<Room> result = roomService.getAllAvailableRooms();

        assertEquals(2, result.size());
        verify(roomRepository).findAllByState(RoomState.AVAILABLE);
    }

    @Test
    void testGetFilteredRooms() {
        Room room1 = new Room();
        room1.setRoomType(RoomType.SINGLE);
        room1.setPrice(100.0f);

        Room room2 = new Room();
        room2.setRoomType(RoomType.DOUBLE);
        room2.setPrice(200.0f);

        when(roomRepository.findAllByState(RoomState.AVAILABLE)).thenReturn(List.of(room1, room2));

        List<Room> result = roomService.getFilteredRooms(RoomType.SINGLE, 50, 150);

        assertEquals(1, result.size());
        assertEquals(RoomType.SINGLE, result.get(0).getRoomType());
    }

    @Test
    void testAddRoom() {
        Room room = new Room();

        roomService.addRoom(room);

        verify(roomRepository).save(room);
    }

    @Test
    void testUpdateRoomState() {
        Room room = new Room();
        room.setRoomId(1L);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        roomService.updateRoomState(room, RoomState.OCCUPIED);

        assertEquals(RoomState.OCCUPIED, room.getState());
        verify(roomRepository).save(room);
    }

    @Test
    void testUpdateRoomStateThrowsException() {
        Room room = new Room();
        room.setRoomId(1L);

        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.updateRoomState(room, RoomState.OCCUPIED));
    }

    @Test
    void testUpdateRoom() {
        Room room = new Room();
        room.setRoomId(1L);

        when(roomRepository.existsById(1L)).thenReturn(true);

        roomService.updateRoom(room);

        verify(roomRepository).save(room);
    }

    @Test
    void testUpdateRoomThrowsException() {
        Room room = new Room();
        room.setRoomId(1L);

        when(roomRepository.existsById(1L)).thenReturn(false);

        assertThrows(RoomNotFoundException.class, () -> roomService.updateRoom(room));
    }

    @Test
    void testDeleteRoom() {
        roomService.deleteRoom(1L);

        verify(roomRepository).deleteById(1L);
    }
}