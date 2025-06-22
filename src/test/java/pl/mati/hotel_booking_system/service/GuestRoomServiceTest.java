package pl.mati.hotel_booking_system.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import pl.mati.hotel_booking_system.entity.GuestRoom;
import pl.mati.hotel_booking_system.entity.HotelUser;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.repository.GuestRoomRepository;
import pl.mati.hotel_booking_system.util.RoomState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GuestRoomServiceTest {

    private GuestRoomRepository guestRoomRepository;
    private RoomService roomService;
    private GuestRoomService guestRoomService;

    @BeforeEach
    void setUp() {
        guestRoomRepository = mock(GuestRoomRepository.class);
        roomService = mock(RoomService.class);
        guestRoomService = new GuestRoomService(guestRoomRepository, roomService);
    }

    @Test
    void testReserveRoom() {
        HotelUser guest = new HotelUser();
        Room room = new Room();
        room.setRoomId(1L);
        room.setState(RoomState.AVAILABLE);

        when(roomService.getRoomById(1L)).thenReturn(room);

        guestRoomService.reserveRoom(guest, 1L);

        verify(roomService).addRoom(room);
        verify(guestRoomRepository).save(any(GuestRoom.class));

        assertEquals(RoomState.RESERVED, room.getState());
    }

    @Test
    void testGetReservationsByUserWhereIsNotCheckOut() {
        HotelUser user = new HotelUser();
        List<GuestRoom> reservations = List.of(new GuestRoom(), new GuestRoom());

        when(guestRoomRepository.findAllByGuestAndIsCheckOutFalse(user)).thenReturn(reservations);

        List<GuestRoom> result = guestRoomService.getReservationsByUserWhereIsNotCheckOut(user);

        assertEquals(2, result.size());
        verify(guestRoomRepository).findAllByGuestAndIsCheckOutFalse(user);
    }

    @Test
    void testMarkReservationAsCheckedIn() {
        HotelUser guest = new HotelUser();
        Room room = new Room();
        GuestRoom reservation = new GuestRoom();

        when(guestRoomRepository.findByGuestAndRoomAndIsCheckInFalse(guest, room))
                .thenReturn(Optional.of(reservation));

        guestRoomService.markReservationAsCheckedIn(guest, room);

        assertTrue(reservation.isCheckIn());
        assertNotNull(reservation.getCheckInDate());
        verify(guestRoomRepository).save(reservation);
    }

    @Test
    void testMarkReservationAsCheckedOut() {
        HotelUser guest = new HotelUser();
        Room room = new Room();
        GuestRoom reservation = new GuestRoom();

        when(guestRoomRepository.findByGuestAndRoomAndIsCheckOutFalse(guest, room))
                .thenReturn(Optional.of(reservation));

        guestRoomService.markReservationAsCheckedOut(guest, room);

        assertTrue(reservation.isCheckOut());
        assertNotNull(reservation.getCheckOutDate());
        verify(guestRoomRepository).save(reservation);
    }

    //make test for getAllReservations
    @Test
    void testGetAllReservations() {
        List<GuestRoom> reservations = List.of(new GuestRoom(), new GuestRoom());

        when(guestRoomRepository.findAll()).thenReturn(reservations);

        List<GuestRoom> result = guestRoomService.getAllReservations();

        assertEquals(2, result.size());
        verify(guestRoomRepository).findAll();
    }
}