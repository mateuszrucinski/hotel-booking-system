package pl.mati.hotel_booking_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.service.RoomService;
import pl.mati.hotel_booking_system.util.RoomState;
import pl.mati.hotel_booking_system.util.RoomType;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoomService roomService;

    @WithMockUser(username = "guest", roles = {"GUEST"})  // or "ADMIN"
    @Test
    void getHelloWorld_shouldReturnString() throws Exception {
        mockMvc.perform(get("/api/room/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void getAllRooms_shouldReturnRoomList() throws Exception {
        Room room1 = new Room(); room1.setRoomId(1L); room1.setRoomType(RoomType.SINGLE); room1.setPrice(100.0f); room1.setState(RoomState.AVAILABLE);
        Room room2 = new Room(); room2.setRoomId(2L); room2.setRoomType(RoomType.DOUBLE); room2.setPrice(200.0f); room2.setState(RoomState.OCCUPIED);

        when(roomService.getAllRooms()).thenReturn(List.of(room1, room2));

        mockMvc.perform(get("/api/room/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @WithMockUser(username = "guest", roles = {"GUEST"})
    @Test
    void getAllAvailableRooms_shouldReturnOnlyAvailableRooms() throws Exception {
        Room room = new Room(); room.setRoomId(3L); room.setRoomType(RoomType.SINGLE); room.setPrice(150.0f); room.setState(RoomState.AVAILABLE);

        when(roomService.getAllAvailableRooms()).thenReturn(List.of(room));

        mockMvc.perform(get("/api/room/rooms/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void addRoom_shouldCallService() throws Exception {
        Room newRoom = new Room();
        newRoom.setRoomType(RoomType.SINGLE);
        newRoom.setPrice(120.0f);
        newRoom.setState(RoomState.AVAILABLE);

        mockMvc.perform(post("/api/room")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRoom)))
                .andExpect(status().isOk());

        verify(roomService).addRoom(any(Room.class));
    }
}
