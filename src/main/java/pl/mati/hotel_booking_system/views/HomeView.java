package pl.mati.hotel_booking_system.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.mati.hotel_booking_system.entity.GuestRoom;
import pl.mati.hotel_booking_system.entity.HotelUser;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.security.UserDetailsImpl;
import pl.mati.hotel_booking_system.service.GuestRoomService;
import pl.mati.hotel_booking_system.service.RoomService;
import pl.mati.hotel_booking_system.util.RoomType;

import java.util.List;

@Route("home")
@RolesAllowed("GUEST")
public class HomeView extends VerticalLayout {

    private final Grid<Room> grid = new Grid<>(Room.class);

    public HomeView(RoomService roomService, GuestRoomService guestRoomService) {
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
        add(topBar);

        //reserved rooms list from user
        HotelUser currentUser = ((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getHotelUser();

        List<GuestRoom> userReservations = guestRoomService.getReservationsByUserWhereIsNotCheckOut(currentUser);
        if(!userReservations.isEmpty()) {
            H2 reservationTitle = new H2("My reservation:");
            Grid<GuestRoom> reservationGrid = new Grid<>(GuestRoom.class);
            reservationGrid.setItems(userReservations);
            reservationGrid.removeAllColumns();
            reservationGrid.addColumn(gr -> gr.getRoom().getRoomId()).setHeader("Room ID");
            reservationGrid.addColumn(gr -> gr.getRoom().getRoomType()).setHeader("Type");
            reservationGrid.addColumn(gr -> gr.getRoom().getState()).setHeader("State");
            reservationGrid.addColumn(GuestRoom::getReservationCodeId).setHeader("Reservation Code");

            reservationGrid.addItemClickListener(event -> {
                Room reservedRoom = event.getItem().getRoom();
                UI.getCurrent().navigate("home/reservation/room/" + reservedRoom.getRoomId());
            });
            add(reservationTitle, reservationGrid);
        }

        // Filter bar
        ComboBox<RoomType> typeFilter = new ComboBox<>("Room type");
        typeFilter.setItems(RoomType.values());
        typeFilter.setClearButtonVisible(true);

        NumberField minPrice = new NumberField("Price from");
        minPrice.setStep(1);
        minPrice.setMin(0);

        NumberField maxPrice = new NumberField("Price to");
        maxPrice.setStep(1);
        maxPrice.setMin(0);

        Button filterButton = new Button("Filter");
        filterButton.addClickListener(e -> {
            RoomType selectedType = typeFilter.getValue();
            int min = minPrice.getValue() != null ? minPrice.getValue().intValue() : 0;
            int max = maxPrice.getValue() != null ? maxPrice.getValue().intValue() : 0;

            List<Room> filteredRooms = roomService.getFilteredRooms(selectedType, min, max);
            grid.setItems(filteredRooms);
        });

        HorizontalLayout filterBar = new HorizontalLayout(typeFilter, minPrice, maxPrice, filterButton);
        filterBar.setPadding(true);
        filterBar.setAlignItems(Alignment.END);
        add(filterBar);

        // room grid
        grid.setItems(roomService.getAllAvailableRooms());
        grid.removeAllColumns();
        grid.addColumn(Room::getRoomId).setHeader("ID");
        grid.addColumn(Room::getRoomType).setHeader("Room type");
        grid.addColumn(Room::getPrice).setHeader("Price");
        grid.addColumn(Room::getState).setHeader("State");

        grid.addItemClickListener(event -> {
            Room clickedRoom = event.getItem();
            if (clickedRoom != null) {
                UI.getCurrent().navigate("home/room/" + clickedRoom.getRoomId());
            }
        });
        add(grid);
    }
}