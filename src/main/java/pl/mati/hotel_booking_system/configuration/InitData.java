package pl.mati.hotel_booking_system.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.repository.RoomRepository;
import pl.mati.hotel_booking_system.util.RoomState;
import pl.mati.hotel_booking_system.util.RoomType;

@Configuration
public class InitData {

    @Bean
    public CommandLineRunner loadData(RoomRepository roomRepository) {
        return args -> {
            Room room = new Room();
            room.setRoomType(RoomType.SINGLE);
            room.setPrice(19.12F);
            room.setState(RoomState.AVAILABLE);
            roomRepository.save(room);

            Room room2 = new Room();
            room2.setRoomType(RoomType.DOUBLE);
            room2.setPrice(20.00F);
            room2.setState(RoomState.OCCUPIED);
            roomRepository.save(room2);

            Room room3 = new Room();
            room3.setRoomType(RoomType.DOUBLE);
            room3.setPrice(80.50F);
            room3.setState(RoomState.AVAILABLE);
            roomRepository.save(room3);
        };
    }
}