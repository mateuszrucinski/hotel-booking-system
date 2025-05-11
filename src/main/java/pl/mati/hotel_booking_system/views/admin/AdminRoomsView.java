package pl.mati.hotel_booking_system.views.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.service.RoomService;
import pl.mati.hotel_booking_system.util.RoomState;
import pl.mati.hotel_booking_system.util.RoomType;

@Route("admin/rooms")
@RolesAllowed("ADMIN")
public class AdminRoomsView extends VerticalLayout {

    private final Grid<Room> roomGrid = new Grid<>(Room.class);

    public AdminRoomsView(RoomService roomService) {
        setSizeFull();
        setHeightFull();

        add(buildTopBar());

        //grid setup
        roomGrid.setItems(roomService.getAllRooms());
        roomGrid.removeAllColumns();
        roomGrid.addColumn(Room::getRoomId).setHeader("ID");
        roomGrid.addColumn(Room::getRoomType).setHeader("Type");
        roomGrid.addColumn(Room::getPrice).setHeader("Price");
        roomGrid.addColumn(Room::getState).setHeader("State");
        roomGrid.addComponentColumn(room -> {
            Button edit = new Button("Edit", e -> openRoomEditDialog(room, roomService));
            Button delete = new Button("Delete", e -> {
                roomService.deleteRoom(room.getRoomId());
                roomGrid.setItems(roomService.getAllRooms());
            });
            return new HorizontalLayout(edit, delete);
        }).setHeader("Actions");

        Button addRoomButton = new Button("Add New Room", e -> openRoomAddDialog(roomService));

        add(new H2("Rooms"), addRoomButton, roomGrid);
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

    private void openRoomEditDialog(Room room, RoomService roomService) {
        Dialog dialog = new Dialog();
        TextField priceField = new TextField("Price", String.valueOf(room.getPrice()));
        ComboBox<RoomType> typeField = new ComboBox<>("Type", RoomType.values());
        typeField.setValue(room.getRoomType());
        ComboBox<RoomState> stateField = new ComboBox<>("State", RoomState.values());
        stateField.setValue(room.getState());

        Button saveButton = new Button("Save", e -> {
            room.setPrice(Float.parseFloat(priceField.getValue()));
            room.setRoomType(typeField.getValue());
            room.setState(stateField.getValue());
            roomService.addRoom(room);
            roomGrid.setItems(roomService.getAllRooms());
            dialog.close();
        });

        dialog.add(priceField, typeField, stateField, saveButton);
        dialog.open();
    }

    private void openRoomAddDialog(RoomService roomService) {
        Dialog dialog = new Dialog();
        TextField priceField = new TextField("Price");
        ComboBox<RoomType> typeField = new ComboBox<>("Type", RoomType.values());
        ComboBox<RoomState> stateField = new ComboBox<>("State", RoomState.values());

        Button addButton = new Button("Add", e -> {
            Room newRoom = new Room();
            newRoom.setPrice(Float.parseFloat(priceField.getValue()));
            newRoom.setRoomType(typeField.getValue());
            newRoom.setState(stateField.getValue());
            roomService.addRoom(newRoom);
            roomGrid.setItems(roomService.getAllRooms());
            dialog.close();
        });

        dialog.add(priceField, typeField, stateField, addButton);
        dialog.open();
    }
}
