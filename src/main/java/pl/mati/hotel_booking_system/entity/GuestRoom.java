package pl.mati.hotel_booking_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class GuestRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestRoomId;

    private String reservationCodeId;

    private boolean isPaid = false;

    //todo dodac kalendarz i dni ile zostaje
    private int numberOfNightsStayed = 1;

    private LocalDate reservedDate = LocalDate.now();

    private boolean isCheckIn = false;

    private boolean isCheckOut = false;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private HotelUser guest;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}