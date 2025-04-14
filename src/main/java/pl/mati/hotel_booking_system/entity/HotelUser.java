package pl.mati.hotel_booking_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import pl.mati.hotel_booking_system.util.UserRole;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class HotelUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NonNull
    private String login;

    @NonNull
    private String password;

    @NonNull
    private UserRole userRole;
}
