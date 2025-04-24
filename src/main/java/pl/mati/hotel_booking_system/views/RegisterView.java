package pl.mati.hotel_booking_system.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.mati.hotel_booking_system.entity.HotelUser;
import pl.mati.hotel_booking_system.repository.UserRepository;
import pl.mati.hotel_booking_system.util.UserRole;

@Route("register")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    public RegisterView(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 header = new H1("Register");

        TextField login = new TextField("Login");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Repeat password");

        Button registerButton = new Button("Register", event -> {
            if (login.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Notification.show("Please fill in all fields");
                return;
            }

            if (!password.getValue().equals(confirmPassword.getValue())) {
                Notification.show("Passwords must match");
                return;
            }

            if (userRepository.findByLogin(login.getValue()).isPresent()) {
                Notification.show("User with this login already exists");
                return;
            }

            HotelUser newUser = new HotelUser();
            newUser.setLogin(login.getValue());
            newUser.setPassword(passwordEncoder.encode(password.getValue()));
            newUser.setUserRole(UserRole.GUEST);

            userRepository.save(newUser);

            Notification.show("Successfully registered");
            UI.getCurrent().navigate("login");
        });

        add(header, login, password, confirmPassword, registerButton);
    }
}