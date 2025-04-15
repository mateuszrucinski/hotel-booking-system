package pl.mati.hotel_booking_system.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pl.mati.hotel_booking_system.entity.HotelUser;
import pl.mati.hotel_booking_system.repository.UserRepository;
import pl.mati.hotel_booking_system.util.UserRole;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                //to enable frames in order to see them in web
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/room/hello", "/room/rooms", "/room/rooms/available").hasAnyRole( UserRole.GUEST.name())
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //for testing to handle auth manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            HotelUser admin = new HotelUser();
            admin.setLogin("admin");
            admin.setPassword(passwordEncoder.encode("123"));
            admin.setUserRole(UserRole.ADMIN);
            userRepository.save(admin);

            HotelUser guest = new HotelUser();
            guest.setLogin("guest");
            guest.setPassword(passwordEncoder.encode("456"));
            guest.setUserRole(UserRole.GUEST);
            userRepository.save(guest);
        };
    }
}