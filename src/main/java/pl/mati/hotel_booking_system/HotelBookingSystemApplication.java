package pl.mati.hotel_booking_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@Theme("my-theme")
//public class HotelBookingSystemApplication implements AppShellConfigurator {
public class HotelBookingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelBookingSystemApplication.class, args);
    }
}
