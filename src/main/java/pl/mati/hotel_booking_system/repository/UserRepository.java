package pl.mati.hotel_booking_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.mati.hotel_booking_system.entity.HotelUser;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<HotelUser, Long> {
    Optional<HotelUser> findByLogin(String login);
}
