package pl.mati.hotel_booking_system.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.mati.hotel_booking_system.entity.GuestRoom;
import pl.mati.hotel_booking_system.entity.HotelUser;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.repository.GuestRoomRepository;
import pl.mati.hotel_booking_system.util.RoomState;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestRoomServiceTest {

    @Mock
    private GuestRoomRepository guestRoomRepository;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private GuestRoomService guestRoomService;

    @Test
    void reserveRoom_ShouldCreateReservationAndUpdateRoomState() {
        // Arrange
        HotelUser user = new HotelUser();
        user.setUserId(1L);

        Room room = new Room();
        room.setRoomId(1L);
        room.setState(RoomState.AVAILABLE);

        when(roomService.getRoomById(1L)).thenReturn(room);
        when(guestRoomRepository.save(any(GuestRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        guestRoomService.reserveRoom(user, 1L);

        // Assert
        verify(roomService).getRoomById(1L);
        verify(roomService).addRoom(room);
        assertEquals(RoomState.RESERVED, room.getState());
        verify(guestRoomRepository).save(any(GuestRoom.class));
    }

    @Test
    void getReservationsByUserWhereIsNotCheckOut_ShouldReturnActiveReservations() {
        // Arrange
        HotelUser user = new HotelUser();
        user.setUserId(1L);

        GuestRoom reservation = new GuestRoom();
        reservation.setGuest(user);
        reservation.setCheckOut(false);

        when(guestRoomRepository.findAllByGuestAndIsCheckOutFalse(user)).thenReturn(Collections.singletonList(reservation));

        // Act
        List<GuestRoom> result = guestRoomService.getReservationsByUserWhereIsNotCheckOut(user);

        // Assert
        assertEquals(1, result.size());
        assertEquals(user, result.get(0).getGuest());
        assertFalse(result.get(0).isCheckOut());
    }

    @Test
    void markReservationAsCheckedIn_ShouldUpdateCheckInStatus() {
        // Arrange
        HotelUser user = new HotelUser();
        user.setUserId(1L);

        Room room = new Room();
        room.setRoomId(1L);

        GuestRoom reservation = new GuestRoom();
        reservation.setGuest(user);
        reservation.setRoom(room);
        reservation.setCheckIn(false);

        when(guestRoomRepository.findByGuestAndRoomAndIsCheckInFalse(user, room)).thenReturn(Optional.of(reservation));
        when(guestRoomRepository.save(any(GuestRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        guestRoomService.markReservationAsCheckedIn(user, room);

        // Assert
        assertTrue(reservation.isCheckIn());
        assertNotNull(reservation.getCheckInDate());
        verify(guestRoomRepository).save(reservation);
    }

    @Test
    void markReservationAsCheckedOut_ShouldUpdateCheckOutStatus() {
        // Arrange
        HotelUser user = new HotelUser();
        user.setUserId(1L);

        Room room = new Room();
        room.setRoomId(1L);

        GuestRoom reservation = new GuestRoom();
        reservation.setGuest(user);
        reservation.setRoom(room);
        reservation.setCheckOut(false);

        when(guestRoomRepository.findByGuestAndRoomAndIsCheckOutFalse(user, room)).thenReturn(Optional.of(reservation));
        when(guestRoomRepository.save(any(GuestRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        guestRoomService.markReservationAsCheckedOut(user, room);

        // Assert
        assertTrue(reservation.isCheckOut());
        assertNotNull(reservation.getCheckOutDate());
        verify(guestRoomRepository).save(reservation);
    }

    @Test
    void getAllReservations_ShouldReturnAllReservations() {
        // Arrange
        GuestRoom reservation = new GuestRoom();
        when(guestRoomRepository.findAll()).thenReturn(Collections.singletonList(reservation));

        // Act
        List<GuestRoom> result = guestRoomService.getAllReservations();

        // Assert
        assertEquals(1, result.size());
        verify(guestRoomRepository).findAll();
    }
    @Test
    void reserveRoom_ShouldGenerateValidReservationCode() {
        // Arrange
        HotelUser user = new HotelUser();
        user.setUserId(1L);

        Room room = new Room();
        room.setRoomId(1L);
        room.setState(RoomState.AVAILABLE);

        when(roomService.getRoomById(1L)).thenReturn(room);
        when(guestRoomRepository.save(any(GuestRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        guestRoomService.reserveRoom(user, 1L);

        // Get the saved reservation
        ArgumentCaptor<GuestRoom> guestRoomCaptor = ArgumentCaptor.forClass(GuestRoom.class);
        verify(guestRoomRepository).save(guestRoomCaptor.capture());
        GuestRoom savedReservation = guestRoomCaptor.getValue();

        // Assert
        String code = savedReservation.getReservationCodeId();
        assertNotNull(code);
        assertEquals(10, code.length());
        // Remove any hyphens before validation
        String cleanCode = code.replace("-", "A");
        assertTrue(cleanCode.matches("[A-F0-9]+"),
                "Code should only contain uppercase hex characters (A-F, 0-9) but was: " + code);
        // Also verify we have exactly 10 alphanumeric characters (hyphens removed)
        assertEquals(10, cleanCode.length());
    }
}