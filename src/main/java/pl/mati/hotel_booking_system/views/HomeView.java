package pl.mati.hotel_booking_system.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import jakarta.annotation.security.RolesAllowed;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.service.RoomService;
import pl.mati.hotel_booking_system.util.RoomState;
import pl.mati.hotel_booking_system.util.RoomType;

@Route("home")
@RolesAllowed("GUEST")
public class HomeView extends VerticalLayout {

    public HomeView(RoomService roomService) {
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        //top bar
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setWidthFull();
        topBar.setPadding(true);
        topBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topBar.setAlignItems(Alignment.CENTER);

        H1 title = new H1("Hotelling");
        Avatar avatar = new Avatar();

        topBar.add(title, avatar);

        // filter bar
        ComboBox<RoomType> typeFilter = new ComboBox<>("Room type");
        typeFilter.setItems(RoomType.values());

        NumberField minPrice = new NumberField("Price from");
        NumberField maxPrice = new NumberField("Price to");

        ComboBox<RoomState> stateFilter = new ComboBox<>("State");
        stateFilter.setItems(RoomState.values());

        Button filterButton = new Button("Filter");

        HorizontalLayout filterBar = new HorizontalLayout(typeFilter, minPrice, maxPrice, stateFilter, filterButton);
        filterBar.setPadding(true);
        filterBar.setAlignItems(Alignment.END);

        // room grid
        Grid<Room> grid = new Grid<>(Room.class);
        grid.setItems(roomService.getAllAvailableRooms());
        grid.removeAllColumns();
        grid.addColumn(Room::getRoomId).setHeader("ID");
        grid.addColumn(Room::getRoomType).setHeader("Room type");
        grid.addColumn(Room::getPriceRange).setHeader("Price");
        grid.addColumn(Room::getState).setHeader("State");

        add(topBar, grid);
    }
}