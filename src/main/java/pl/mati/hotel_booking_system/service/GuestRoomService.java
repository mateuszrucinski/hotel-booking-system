package pl.mati.hotel_booking_system.service;

import org.springframework.stereotype.Service;
import pl.mati.hotel_booking_system.entity.GuestRoom;
import pl.mati.hotel_booking_system.entity.HotelUser;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.repository.GuestRoomRepository;
import pl.mati.hotel_booking_system.util.RoomState;

import java.util.List;
import java.util.UUID;

@Service
public class GuestRoomService {

    private final GuestRoomRepository guestRoomRepository;
    private final RoomService roomService;

    public GuestRoomService(GuestRoomRepository guestRoomRepository, RoomService roomService) {
        this.guestRoomRepository = guestRoomRepository;
        this.roomService = roomService;
    }

    public void reserveRoom(HotelUser guest, Long roomId) {
        Room room = roomService.getRoomById(roomId);

        //set state as reserved
        room.setState(RoomState.RESERVED);

        roomService.addRoom(room);

        //create reservation and saving in db
        GuestRoom guestRoom = new GuestRoom();
        guestRoom.setGuest(guest);
        guestRoom.setRoom(room);
        guestRoom.setReservationCodeId(generateReservationCode());
        guestRoom.setPaid(true);
        guestRoomRepository.save(guestRoom);
    }

    public List<GuestRoom> getReservationsByUser(HotelUser user) {
        return guestRoomRepository.findAllByGuest(user);
    }

    private String generateReservationCode() {
        return UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }
}
