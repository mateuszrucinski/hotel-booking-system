package pl.mati.hotel_booking_system.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.service.RoomService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void testGetHelloWorld() throws Exception {
        mockMvc.perform(get("/api/room/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"));
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void testGetAllRooms() throws Exception {
        when(roomService.getAllRooms()).thenReturn(List.of(new Room(), new Room()));

        mockMvc.perform(get("/api/room/rooms"))
                .andExpect(status().isOk());
        verify(roomService).getAllRooms();
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void testGetAllAvailableRooms() throws Exception {
        when(roomService.getAllAvailableRooms()).thenReturn(List.of(new Room(), new Room()));

        mockMvc.perform(get("/api/room/rooms/available"))
                .andExpect(status().isOk());
        verify(roomService).getAllAvailableRooms();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddRoom() throws Exception {
        Room mockRoom = new Room();
        mockRoom.setRoomType(pl.mati.hotel_booking_system.util.RoomType.SINGLE);
        mockRoom.setPrice(100.0f);
        mockRoom.setState(pl.mati.hotel_booking_system.util.RoomState.AVAILABLE);

        doNothing().when(roomService).addRoom(any(Room.class));

        mockMvc.perform(
                        post("/api/room")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"roomType\":\"SINGLE\",\"price\":100.0,\"state\":\"AVAILABLE\"}")
                )
                .andExpect(status().isOk());
        verify(roomService).addRoom(any(Room.class));
    }
}