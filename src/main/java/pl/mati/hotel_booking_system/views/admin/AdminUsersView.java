package pl.mati.hotel_booking_system.views.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import pl.mati.hotel_booking_system.entity.HotelUser;
import pl.mati.hotel_booking_system.repository.UserRepository;

@Route("admin/users")
@RolesAllowed("ADMIN")
public class AdminUsersView extends VerticalLayout {

    private final Grid<HotelUser> userGrid = new Grid<>(HotelUser.class);

    public AdminUsersView(UserRepository userRepository) {
        setSizeFull();
        setHeightFull();

        add(buildTopBar());

        userGrid.setItems(userRepository.findAll());
        userGrid.removeAllColumns();
        userGrid.addColumn(HotelUser::getUserId).setHeader("ID");
        userGrid.addColumn(HotelUser::getLogin).setHeader("Login");
        userGrid.addColumn(HotelUser::getUserRole).setHeader("Role");

        add(new H2("Users"), userGrid);
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