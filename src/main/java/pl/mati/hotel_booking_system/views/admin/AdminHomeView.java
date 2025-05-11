package pl.mati.hotel_booking_system.views.admin;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("admin")
@RolesAllowed("ADMIN")
public class AdminHomeView extends VerticalLayout {

    public AdminHomeView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);

        //top bar
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setWidthFull();
        topBar.setPadding(true);
        topBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topBar.setAlignItems(Alignment.CENTER);
        topBar.add(new H1("Admin Panel"), new Avatar());
        add(topBar);

        //welcome section
        add(new Paragraph("Welcome to the Admin Panel! Choose a section below:"));

        //navigation buttoms
        Button roomsButton = new Button("Manage Rooms", e -> getUI().ifPresent(ui -> ui.navigate("admin/rooms")));
        Button reservationsButton = new Button("View Reservations", e -> getUI().ifPresent(ui -> ui.navigate("admin/reservations")));
        Button usersButton = new Button("Manage Users", e -> getUI().ifPresent(ui -> ui.navigate("admin/users")));

        roomsButton.setWidth("200px");
        reservationsButton.setWidth("200px");
        usersButton.setWidth("200px");

        VerticalLayout buttonLayout = new VerticalLayout(roomsButton, reservationsButton, usersButton);
        buttonLayout.setAlignItems(Alignment.CENTER);
        buttonLayout.setSpacing(true);

        add(buttonLayout);
    }
}
