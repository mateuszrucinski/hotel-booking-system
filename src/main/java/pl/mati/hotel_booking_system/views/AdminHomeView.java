package pl.mati.hotel_booking_system.views;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import pl.mati.hotel_booking_system.service.RoomService;

@Route("admin")
@RolesAllowed("ADMIN")
public class AdminHomeView extends VerticalLayout {
    public AdminHomeView(RoomService roomService) {
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        //top bar
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setWidthFull();
        topBar.setPadding(true);
        topBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topBar.setAlignItems(FlexComponent.Alignment.CENTER);

        H1 title = new H1("Elllo");
        Avatar avatar = new Avatar();

        topBar.add(title, avatar);

        add(topBar);

//        //filter bar
//        ComboBox<RoomType> typeFilter = new ComboBox<>("Typ pokoju");
//        typeFilter.setItems(RoomType.values());
//
//        NumberField minPrice = new NumberField("Cena od");
//        NumberField maxPrice = new NumberField("Cena do");
//
//        ComboBox<RoomState> stateFilter = new ComboBox<>("Stan");
//        stateFilter.setItems(RoomState.values());
//
//        Button filterButton = new Button("Filtruj");
//
//        HorizontalLayout filterBar = new HorizontalLayout(typeFilter, minPrice, maxPrice, stateFilter, filterButton);
//        filterBar.setPadding(true);
//        filterBar.setAlignItems(FlexComponent.Alignment.END);
//
//
//        //room grid
//        Grid<Room> grid = new Grid<>(Room.class);
//        grid.setItems(roomService.getAllAvailableRooms());
//        grid.removeAllColumns();
//        grid.addColumn(Room::getRoomId).setHeader("ID");
//        grid.addColumn(Room::getRoomType).setHeader("Typ pokoju");
//        grid.addColumn(Room::getPriceRange).setHeader("Cena");
//        grid.addColumn(Room::getState).setHeader("Stan");
//
//        add(topBar, grid);
    }
}
