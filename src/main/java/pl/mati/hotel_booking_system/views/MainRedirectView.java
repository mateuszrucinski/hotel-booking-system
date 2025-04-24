package pl.mati.hotel_booking_system.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("")
@PermitAll
public class MainRedirectView extends VerticalLayout {

    public MainRedirectView() {
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(new H1("Redirecting..."));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        getUI().ifPresent(ui -> {
            var auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.isAuthenticated()) {
                boolean isAdmin = auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                if (isAdmin) {
                    ui.navigate("admin");
                } else {
                    ui.navigate("home");
                }
            } else {
                ui.navigate("login");
            }
        });
    }
}