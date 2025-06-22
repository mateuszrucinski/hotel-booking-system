package pl.mati.hotel_booking_system.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.excpetion.RoomNotFoundException;
import pl.mati.hotel_booking_system.repository.RoomRepository;
import pl.mati.hotel_booking_system.util.RoomState;
import pl.mati.hotel_booking_system.util.RoomType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    void getRoomById_ShouldReturnRoomWhenExists() {
        // Arrange
        Room expectedRoom = createTestRoom(1L, RoomType.SINGLE, 100f, RoomState.AVAILABLE);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(expectedRoom));

        // Act
        Room result = roomService.getRoomById(1L);

        // Assert
        assertEquals(expectedRoom, result);
    }

    @Test
    void getRoomById_ShouldThrowExceptionWhenNotFound() {
        // Arrange
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoomNotFoundException.class, () -> roomService.getRoomById(1L));
    }

    @Test
    void getAllRooms_ShouldReturnAllRooms() {
        // Arrange
        Room room1 = createTestRoom(1L, RoomType.SINGLE, 100f, RoomState.AVAILABLE);
        Room room2 = createTestRoom(2L, RoomType.DOUBLE, 200f, RoomState.OCCUPIED);
        when(roomRepository.findAll()).thenReturn(List.of(room1, room2));

        // Act
        List<Room> result = roomService.getAllRooms();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getAllAvailableRooms_ShouldReturnOnlyAvailableRooms() {
        // Arrange
        Room availableRoom = createTestRoom(1L, RoomType.SINGLE, 100f, RoomState.AVAILABLE);
        Room occupiedRoom = createTestRoom(2L, RoomType.DOUBLE, 200f, RoomState.OCCUPIED);
        when(roomRepository.findAllByState(RoomState.AVAILABLE)).thenReturn(List.of(availableRoom));

        // Act
        List<Room> result = roomService.getAllAvailableRooms();

        // Assert
        assertEquals(1, result.size());
        assertEquals(RoomState.AVAILABLE, result.get(0).getState());
    }

    @Test
    void getFilteredRooms_ShouldFilterByRoomType() {
        // Arrange
        Room singleRoom = createTestRoom(1L, RoomType.SINGLE, 100f, RoomState.AVAILABLE);
        Room doubleRoom = createTestRoom(2L, RoomType.DOUBLE, 200f, RoomState.AVAILABLE);
        when(roomRepository.findAllByState(RoomState.AVAILABLE)).thenReturn(List.of(singleRoom, doubleRoom));

        // Act
        List<Room> result = roomService.getFilteredRooms(RoomType.SINGLE, 0, 0);

        // Assert
        assertEquals(1, result.size());
        assertEquals(RoomType.SINGLE, result.get(0).getRoomType());
    }

    @Test
    void getFilteredRooms_ShouldFilterByPriceRange() {
        // Arrange
        Room cheapRoom = createTestRoom(1L, RoomType.SINGLE, 100f, RoomState.AVAILABLE);
        Room expensiveRoom = createTestRoom(2L, RoomType.SUITE, 300f, RoomState.AVAILABLE);
        when(roomRepository.findAllByState(RoomState.AVAILABLE)).thenReturn(List.of(cheapRoom, expensiveRoom));

        // Act
        List<Room> result = roomService.getFilteredRooms(null, 200, 400);

        // Assert
        assertEquals(1, result.size());
        assertEquals(300f, result.get(0).getPrice());
    }

    @Test
    void addRoom_ShouldSaveRoom() {
        // Arrange
        Room newRoom = createTestRoom(null, RoomType.DOUBLE, 150f, RoomState.AVAILABLE);
        when(roomRepository.save(newRoom)).thenReturn(newRoom);

        // Act
        roomService.addRoom(newRoom);

        // Assert
        verify(roomRepository).save(newRoom);
    }

    @Test
    void updateRoomState_ShouldUpdateState() {
        // Arrange
        Room existingRoom = createTestRoom(1L, RoomType.SINGLE, 100f, RoomState.AVAILABLE);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(existingRoom)).thenReturn(existingRoom);

        // Act
        roomService.updateRoomState(existingRoom, RoomState.RESERVED);

        // Assert
        assertEquals(RoomState.RESERVED, existingRoom.getState());
        verify(roomRepository).save(existingRoom);
    }

    @Test
    void updateRoomState_ShouldThrowExceptionWhenRoomNotFound() {
        // Arrange
        Room nonExistingRoom = createTestRoom(99L, RoomType.SUITE, 500f, RoomState.AVAILABLE);
        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoomNotFoundException.class,
                () -> roomService.updateRoomState(nonExistingRoom, RoomState.OCCUPIED));
    }

    @Test
    void updateRoom_ShouldUpdateRoom() {
        // Arrange
        Room existingRoom = createTestRoom(1L, RoomType.DOUBLE, 200f, RoomState.AVAILABLE);
        when(roomRepository.existsById(1L)).thenReturn(true);
        when(roomRepository.save(existingRoom)).thenReturn(existingRoom);

        // Act
        roomService.updateRoom(existingRoom);

        // Assert
        verify(roomRepository).save(existingRoom);
    }

    @Test
    void updateRoom_ShouldThrowExceptionWhenRoomNotFound() {
        // Arrange
        Room nonExistingRoom = createTestRoom(99L, RoomType.SUITE, 500f, RoomState.AVAILABLE);
        when(roomRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(RoomNotFoundException.class,
                () -> roomService.updateRoom(nonExistingRoom));
    }

    @Test
    void deleteRoom_ShouldDeleteRoom() {
        // Arrange
        Long roomId = 1L;
        when(roomRepository.existsById(roomId)).thenReturn(true);
        doNothing().when(roomRepository).deleteById(roomId);

        // Act
        roomService.deleteRoom(roomId);

        // Assert
        verify(roomRepository).deleteById(roomId);
    }

    @Test
    void deleteRoom_ShouldThrowExceptionWhenRoomNotFound() {
        // Arrange
        Long nonExistingRoomId = 99L;
        when(roomRepository.existsById(nonExistingRoomId)).thenReturn(false);

        // Act & Assert
        assertThrows(RoomNotFoundException.class,
                () -> roomService.deleteRoom(nonExistingRoomId));
    }

    private Room createTestRoom(Long id, RoomType type, float price, RoomState state) {
        Room room = new Room();
        room.setRoomId(id);
        room.setRoomType(type);
        room.setPrice(price);
        room.setState(state);
        return room;
    }
}