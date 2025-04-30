package pl.mati.hotel_booking_system.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.mati.hotel_booking_system.entity.HotelUser;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.security.UserDetailsImpl;
import pl.mati.hotel_booking_system.service.GuestRoomService;
import pl.mati.hotel_booking_system.service.RoomService;
import pl.mati.hotel_booking_system.util.RoomState;

@Route("home/reservation/room")
@PageTitle("My Reserved Room")
@RolesAllowed("GUEST")
public class ReservationRoomDetailsView extends VerticalLayout implements HasUrlParameter<Long> {

    private final RoomService roomService;
    private final GuestRoomService guestRoomService;

    public ReservationRoomDetailsView(RoomService roomService, GuestRoomService guestRoomService) {
        this.roomService = roomService;
        this.guestRoomService = guestRoomService;
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent event, Long roomId) {
        removeAll();

        try {
            Room room = roomService.getRoomById(roomId);
            add(new H1("Reserved Room Details"));
            add(new Paragraph("Room ID: " + room.getRoomId()));
            add(new Paragraph("Type: " + room.getRoomType()));
            add(new Paragraph("Price: $" + room.getPrice()));
            add(new Paragraph("Current State: " + room.getState()));

            HotelUser currentUser = ((UserDetailsImpl) SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal()).getHotelUser();

            if (room.getState() == RoomState.RESERVED) {
                Button checkInButton = new Button("Check In", e -> {
                    Dialog confirmDialog = new Dialog();
                    confirmDialog.add(new H2("Are you sure you want to check in?"));

                    Button yesButton = new Button("Yes", ev -> {
                        roomService.updateRoomState(room, RoomState.OCCUPIED);

                        //changing boolean isCheckIn to true
                        guestRoomService.markReservationAsCheckedIn(currentUser, room);
                        Notification.show("Checked in successfully!");
                        confirmDialog.close();
                        getUI().ifPresent(ui -> ui.navigate("home"));
                    });

                    Button noButton = new Button("No", ev -> confirmDialog.close());

                    HorizontalLayout buttons = new HorizontalLayout(yesButton, noButton);
                    buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
                    buttons.setWidthFull();

                    confirmDialog.add(buttons);
                    confirmDialog.open();
                });
                add(checkInButton);

            } else if (room.getState() == RoomState.OCCUPIED) {
                Button checkOutButton = new Button("Check Out", e -> {
                    Dialog confirmDialog = new Dialog();
                    confirmDialog.add(new H2("Are you sure you want to check out?"));

                    Button yesButton = new Button("Yes", ev -> {
                        roomService.updateRoomState(room, RoomState.AVAILABLE);

                        //changing boolean isCheckOut to true
                        guestRoomService.markReservationAsCheckedOut(currentUser, room);
                        Notification.show("Checked out successfully!");
                        confirmDialog.close();
                        getUI().ifPresent(ui -> ui.navigate("home"));
                    });

                    Button noButton = new Button("No", ev -> confirmDialog.close());

                    HorizontalLayout buttons = new HorizontalLayout(yesButton, noButton);
                    buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
                    buttons.setWidthFull();
                    confirmDialog.add(buttons);
                    confirmDialog.open();
                });
                add(checkOutButton);
            }

            Button backButton = new Button("Back to Home", e -> getUI().ifPresent(ui -> ui.navigate("home")));
            add(backButton);

        } catch (Exception e) {
            add(new H1("Room not found 😕"));
        }
    }
}