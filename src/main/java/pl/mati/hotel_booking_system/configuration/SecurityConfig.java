package pl.mati.hotel_booking_system.configuration;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pl.mati.hotel_booking_system.entity.HotelUser;
import pl.mati.hotel_booking_system.repository.UserRepository;
import pl.mati.hotel_booking_system.util.UserRole;
import pl.mati.hotel_booking_system.views.LoginView;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig extends VaadinWebSecurity {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    //security configuration for vaadin views - login form
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    //http basic configuration for api endpoints
    //configuration for h2 database console
    @Bean
    @Order(1)
    public SecurityFilterChain apiAndH2FilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(request -> {
                    String path = request.getRequestURI();
                    return path.startsWith("/api/") || path.startsWith("/h2-console/");
                })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").hasRole("ADMIN")
                        .requestMatchers("/api/**").authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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