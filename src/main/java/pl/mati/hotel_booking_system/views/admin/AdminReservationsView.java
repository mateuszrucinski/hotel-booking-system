package pl.mati.hotel_booking_system.views.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import pl.mati.hotel_booking_system.entity.GuestRoom;
import pl.mati.hotel_booking_system.service.GuestRoomService;

@Route("admin/reservations")
@RolesAllowed("ADMIN")
public class AdminReservationsView extends VerticalLayout {

    private final Grid<GuestRoom> reservationGrid = new Grid<>(GuestRoom.class);

    public AdminReservationsView(GuestRoomService guestRoomService) {
        setSizeFull();
        setHeightFull();

        add(buildTopBar());

        reservationGrid.setItems(guestRoomService.getAllReservations());
        reservationGrid.removeAllColumns();
        reservationGrid.addColumn(gr -> gr.getGuest().getLogin()).setHeader("Guest");
        reservationGrid.addColumn(gr -> gr.getRoom().getRoomId()).setHeader("Room ID");
        reservationGrid.addColumn(GuestRoom::getReservationCodeId).setHeader("Code");
        reservationGrid.addColumn(GuestRoom::isPaid).setHeader("Paid");
        reservationGrid.addColumn(GuestRoom::isCheckIn).setHeader("Checked In");
        reservationGrid.addColumn(GuestRoom::isCheckOut).setHeader("Checked Out");

        add(new H2("Reservations"), reservationGrid);
    }

    private HorizontalLayout buildTopBar() {
        Button backButton = new Button("← Back", e -> getUI().ifPresent(ui -> ui.navigate("admin")));
        Button rooms = new Button("Rooms", e -> getUI().ifPresent(ui -> ui.navigate("admin/rooms")));
        Button reservations = new Button("Reservations", e -> getUI().ifPresent(ui -> ui.navigate("admin/reservations")));
        Button users = new Button("Users", e -> getUI().ifPresent(ui -> ui.navigate("admin/users")));

        HorizontalLayout nav = new HorizontalLayout(backButton, rooms, reservations, users);
        nav.setSpacing(true);
        return nav;
    }
}