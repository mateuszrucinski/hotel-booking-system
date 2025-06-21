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
    void getRoomById_shouldReturnRoom_whenExists() {
        Room room = new Room();
        room.setRoomId(1L);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Room result = roomService.getRoomById(1L);
        assertEquals(room, result);
    }

    @Test
    void getRoomById_shouldThrow_whenNotExists() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.getRoomById(1L));
    }

    @Test
    void getAllRooms_shouldReturnAll() {
        List<Room> allRooms = List.of(new Room(), new Room());
        when(roomRepository.findAll()).thenReturn(allRooms);

        List<Room> result = roomService.getAllRooms();
        assertEquals(allRooms, result);
    }

    @Test
    void getAllAvailableRooms_shouldReturnAvailable() {
        List<Room> available = List.of(new Room());
        when(roomRepository.findAllByState(RoomState.AVAILABLE)).thenReturn(available);

        List<Room> result = roomService.getAllAvailableRooms();
        assertEquals(available, result);
    }

    @Test
    void getFilteredRooms_shouldFilterByTypeAndPrice() {
        Room r1 = new Room(); r1.setRoomType(RoomType.SINGLE); r1.setPrice(100.0f);
        Room r2 = new Room(); r2.setRoomType(RoomType.DOUBLE); r2.setPrice(200.0f);
        Room r3 = new Room(); r3.setRoomType(RoomType.SINGLE); r3.setPrice(300.0f);

        when(roomRepository.findAllByState(RoomState.AVAILABLE)).thenReturn(List.of(r1, r2, r3));

        List<Room> filtered = roomService.getFilteredRooms(RoomType.SINGLE, 50, 150);
        assertEquals(1, filtered.size());
        assertEquals(r1, filtered.get(0));
    }

    @Test
    void getFilteredRooms_shouldIgnoreRoomTypeIfNull() {
        Room r1 = new Room(); r1.setRoomType(RoomType.SINGLE); r1.setPrice(100.0f);
        Room r2 = new Room(); r2.setRoomType(RoomType.DOUBLE); r2.setPrice(90.0f);

        when(roomRepository.findAllByState(RoomState.AVAILABLE)).thenReturn(List.of(r1, r2));

        List<Room> filtered = roomService.getFilteredRooms(null, 50, 100);
        assertEquals(2, filtered.size());
    }

    @Test
    void addRoom_shouldSaveRoom() {
        Room newRoom = new Room();
        roomService.addRoom(newRoom);
        verify(roomRepository).save(newRoom);
    }

    @Test
    void updateRoomState_shouldUpdateRoom_whenExists() {
        Room room = new Room();
        room.setRoomId(1L);
        room.setState(RoomState.AVAILABLE);

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        roomService.updateRoomState(room, RoomState.OCCUPIED);

        assertEquals(RoomState.OCCUPIED, room.getState());
        verify(roomRepository).save(room);
    }

    @Test
    void updateRoomState_shouldThrow_whenRoomNotExists() {
        Room room = new Room();
        room.setRoomId(1L);

        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.updateRoomState(room, RoomState.OCCUPIED));
    }

    @Test
    void updateRoom_shouldSaveUpdatedRoom_whenExists() {
        Room room = new Room();
        room.setRoomId(1L);

        when(roomRepository.existsById(1L)).thenReturn(true);

        roomService.updateRoom(room);
        verify(roomRepository).save(room);
    }

    @Test
    void updateRoom_shouldThrow_whenRoomNotExists() {
        Room room = new Room();
        room.setRoomId(1L);

        when(roomRepository.existsById(1L)).thenReturn(false);

        assertThrows(RoomNotFoundException.class, () -> roomService.updateRoom(room));
    }

    @Test
    void deleteRoom_shouldDeleteById() {
        roomService.deleteRoom(1L);
        verify(roomRepository).deleteById(1L);
    }
}

