package pl.mati.hotel_booking_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.mati.hotel_booking_system.configuration.SecurityConfig;
import pl.mati.hotel_booking_system.entity.Room;
import pl.mati.hotel_booking_system.service.RoomService;
import pl.mati.hotel_booking_system.util.RoomState;
import pl.mati.hotel_booking_system.util.RoomType;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("removal")
    @MockBean
    private RoomService roomService;

    @Test
    @WithMockUser(roles = "GUEST")
    void getHelloWorld_ShouldReturnHelloWorld() throws Exception {
        mockMvc.perform(get("/api/room/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"));
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void getAllRooms_ShouldReturnAllRooms() throws Exception {
        // Given
        Room room1 = createTestRoom(1L, RoomType.SINGLE, 100f, RoomState.AVAILABLE);
        Room room2 = createTestRoom(2L, RoomType.DOUBLE, 200f, RoomState.OCCUPIED);
        given(roomService.getAllRooms()).willReturn(List.of(room1, room2));

        // When & Then
        mockMvc.perform(get("/api/room/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].roomType").value("SINGLE"))
                .andExpect(jsonPath("$[1].roomType").value("DOUBLE"));
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void getAllAvailableRooms_ShouldReturnAvailableRooms() throws Exception {
        // Given
        Room availableRoom = createTestRoom(1L, RoomType.SINGLE, 100f, RoomState.AVAILABLE);
        given(roomService.getAllAvailableRooms()).willReturn(List.of(availableRoom));

        // When & Then
        mockMvc.perform(get("/api/room/rooms/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].state").value("AVAILABLE"))
                .andExpect(jsonPath("$[0].roomType").value("SINGLE"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addRoom_ShouldAddNewRoom() throws Exception {
        // Given
        Room newRoom = createTestRoom(null, RoomType.SUITE, 300f, RoomState.AVAILABLE);

        // When & Then
        mockMvc.perform(post("/api/room")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRoom)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void addRoom_ShouldBeForbiddenForNonAdmin() throws Exception {
        Room newRoom = createTestRoom(null, RoomType.SINGLE, 100f, RoomState.AVAILABLE);

        mockMvc.perform(post("/api/room")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRoom)))
                .andExpect(status().isForbidden());
    }

    private Room createTestRoom(Long id, RoomType type, Float price, RoomState state) {
        Room room = new Room();
        room.setRoomId(id);
        room.setRoomType(type);
        room.setPrice(price);
        room.setState(state);
        return room;
    }
}