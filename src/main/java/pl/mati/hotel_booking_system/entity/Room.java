package pl.mati.hotel_booking_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import pl.mati.hotel_booking_system.util.RoomState;
import pl.mati.hotel_booking_system.util.RoomType;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @NonNull
    private RoomType roomType;
    @NonNull
    private Float price;
    @NonNull
    private RoomState state;

    private Long reservedRoomNumber;
}
