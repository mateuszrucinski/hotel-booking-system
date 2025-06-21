package pl.mati.hotel_booking_system.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pl.mati.hotel_booking_system.entity.GuestRoom;
import pl.mati.hotel_booking_system.entity.HotelUser;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.repository.GuestRoomRepository;
import pl.mati.hotel_booking_system.util.RoomState;
import pl.mati.hotel_booking_system.util.RoomType;

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
    void reserveRoom_shouldReserveRoomAndSaveGuestRoom() {
        HotelUser guest = new HotelUser();
        guest.setUserId(1L);

        Room room = new Room();
        room.setRoomId(1L);
        room.setRoomType(RoomType.SINGLE);
        room.setPrice(100.0f);
        room.setState(RoomState.AVAILABLE);

        when(roomService.getRoomById(1L)).thenReturn(room);

        guestRoomService.reserveRoom(guest, 1L);

        assertEquals(RoomState.RESERVED, room.getState());
        verify(roomService).addRoom(room);

        ArgumentCaptor<GuestRoom> captor = ArgumentCaptor.forClass(GuestRoom.class);
        verify(guestRoomRepository).save(captor.capture());

        GuestRoom saved = captor.getValue();
        assertEquals(guest, saved.getGuest());
        assertEquals(room, saved.getRoom());
        assertNotNull(saved.getReservedDate());
    }

    @Test
    void getReservationsByUserWhereIsNotCheckOut_shouldReturnList() {
        HotelUser user = new HotelUser();
        List<GuestRoom> expectedList = List.of(new GuestRoom());

        when(guestRoomRepository.findAllByGuestAndIsCheckOutFalse(user)).thenReturn(expectedList);

        List<GuestRoom> result = guestRoomService.getReservationsByUserWhereIsNotCheckOut(user);

        assertEquals(expectedList, result);
        verify(guestRoomRepository).findAllByGuestAndIsCheckOutFalse(user);
    }

    @Test
    void markReservationAsCheckedIn_shouldUpdateReservationIfPresent() {
        HotelUser user = new HotelUser();
        Room room = new Room();
        GuestRoom reservation = new GuestRoom();

        when(guestRoomRepository.findByGuestAndRoomAndIsCheckInFalse(user, room))
                .thenReturn(Optional.of(reservation));

        guestRoomService.markReservationAsCheckedIn(user, room);

        assertTrue(reservation.isCheckIn());
        assertNotNull(reservation.getCheckInDate());
        verify(guestRoomRepository).save(reservation);
    }

    @Test
    void markReservationAsCheckedIn_shouldDoNothingIfNotPresent() {
        HotelUser user = new HotelUser();
        Room room = new Room();

        when(guestRoomRepository.findByGuestAndRoomAndIsCheckInFalse(user, room))
                .thenReturn(Optional.empty());

        guestRoomService.markReservationAsCheckedIn(user, room);

        verify(guestRoomRepository, never()).save(any());
    }

    @Test
    void markReservationAsCheckedOut_shouldUpdateReservationIfPresent() {
        HotelUser user = new HotelUser();
        Room room = new Room();
        GuestRoom reservation = new GuestRoom();

        when(guestRoomRepository.findByGuestAndRoomAndIsCheckOutFalse(user, room))
                .thenReturn(Optional.of(reservation));

        guestRoomService.markReservationAsCheckedOut(user, room);

        assertTrue(reservation.isCheckOut());
        assertNotNull(reservation.getCheckOutDate());
        verify(guestRoomRepository).save(reservation);
    }

    @Test
    void markReservationAsCheckedOut_shouldDoNothingIfNotPresent() {
        HotelUser user = new HotelUser();
        Room room = new Room();

        when(guestRoomRepository.findByGuestAndRoomAndIsCheckOutFalse(user, room))
                .thenReturn(Optional.empty());

        guestRoomService.markReservationAsCheckedOut(user, room);

        verify(guestRoomRepository, never()).save(any());
    }

    @Test
    void getAllReservations_shouldReturnList() {
        List<GuestRoom> allReservations = List.of(new GuestRoom(), new GuestRoom());
        when(guestRoomRepository.findAll()).thenReturn(allReservations);

        List<GuestRoom> result = guestRoomService.getAllReservations();

        assertEquals(allReservations, result);
        verify(guestRoomRepository).findAll();
    }
}
