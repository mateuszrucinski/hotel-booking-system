package pl.mati.hotel_booking_system.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 header = new H1("Hotelling");

        LoginForm loginForm = new LoginForm();
        loginForm.setAction("login");

        Button goToRegister = new Button("Register", e -> UI.getCurrent().navigate("register"));
        add(header, loginForm, goToRegister);
    }
}
