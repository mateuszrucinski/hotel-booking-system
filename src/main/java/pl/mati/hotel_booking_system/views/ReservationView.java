package pl.mati.hotel_booking_system.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.mati.hotel_booking_system.entity.HotelUser;
import pl.mati.hotel_booking_system.security.UserDetailsImpl;
import pl.mati.hotel_booking_system.service.GuestRoomService;

@Route("home/reservation")
@PageTitle("Reservation")
@RolesAllowed("GUEST")
public class ReservationView extends VerticalLayout implements HasUrlParameter<Long> {

    private final GuestRoomService guestRoomService;
    private Long roomId;

    public ReservationView(GuestRoomService guestRoomService) {
        this.guestRoomService = guestRoomService;
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    @Override
    public void setParameter(BeforeEvent event, Long roomId) {
        this.roomId = roomId;

        removeAll();
        add(new H1("Confirm your reservation"));

        Button payButton = new Button("PAYMENT", e -> {
            HotelUser currentUser = ((UserDetailsImpl) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal())
                    .getHotelUser();
            guestRoomService.reserveRoom(currentUser, roomId);

            Notification.show("Reservation completed!");
            getUI().ifPresent(ui -> ui.navigate("home"));
        });

        add(payButton);
    }
}