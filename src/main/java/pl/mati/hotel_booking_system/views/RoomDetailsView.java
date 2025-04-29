package pl.mati.hotel_booking_system.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.service.RoomService;

@Route("home/room")
@PageTitle("Room Details")
@RolesAllowed("GUEST")
public class RoomDetailsView extends VerticalLayout implements HasUrlParameter<Long> {

    private final RoomService roomService;

    public RoomDetailsView(RoomService roomService) {
        this.roomService = roomService;
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent event, Long roomId) {
        try {
            Room room = roomService.getRoomById(roomId);

            removeAll();
            add(new H1("Room Details"));

            add(new Paragraph("Room ID: " + room.getRoomId()));
            add(new Paragraph("Type: " + room.getRoomType()));
            add(new Paragraph("Price: $" + room.getPrice()));
            add(new Paragraph("State: " + room.getState()));

            Button reserveButton = new Button("Reserve this room", e -> {
                getUI().ifPresent(ui -> ui.navigate("home/reservation/" + room.getRoomId()));
            });
            add(reserveButton);

            Button backButton = new Button("Back to Home", e -> {
                getUI().ifPresent(ui -> ui.navigate("home"));
            });
            add(backButton);

        } catch (Exception e) {
            removeAll();
            add(new H1("Room not found 😕"));
            Button backButton = new Button("Back to Home", click -> {
                getUI().ifPresent(ui -> ui.navigate("home"));
            });
            add(backButton);
        }
    }
}
