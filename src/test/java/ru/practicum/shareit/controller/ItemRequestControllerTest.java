package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private User user;
    private User userTwo;
    private ItemRequestDto item;
    private ItemRequestResponseDto itemResponse;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    @MockBean
    private ItemRequestService itemRequestService;

    @BeforeEach
    public void setUp() throws Exception {
        user = new User(1, "user", "user@user.com");

        userTwo = new User(2, "userNew", "userNew@userNew.com");

        item = new ItemRequestDto("Хотел бы воспользоваться щёткой для обуви");

        itemResponse = ItemRequestResponseDto.builder()
                .id(1)
                .description(item.getDescription())
                .items(new ArrayList<>())
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    public void shouldNotPostItemRequestWithNegativeUser() throws Exception {
        String jsonItem = objectMapper.writeValueAsString(item);
        Integer userId = -99;

        mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotPostItemRequestWithEmptyDescription() throws Exception {
        ItemRequestDto item = new ItemRequestDto(null);
        String jsonItem = objectMapper.writeValueAsString(item);
        Integer userId = 1;

        mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void shouldNotGetItemRequestWithoutUser() throws Exception {
        mockMvc.perform(get("/requests"))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void shouldGetItemRequestWithoutRequest() throws Exception {
        Integer userId = 1;
        when(itemRequestService.getForUser(anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldGetItemRequestWithoutPaginationParams() throws Exception {
        Integer userId = 1;
        when(itemRequestService.getForUser(anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldNotGetItemRequestWithFrom0Size0() throws Exception {
        Integer userId = 1;

        mockMvc.perform(get("/requests/all?from=0&size=0")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotGetItemRequestWithFromNegative() throws Exception {
        Integer userId = 1;

        mockMvc.perform(get("/requests/all?from=-1&size=20")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotGetItemRequestWithFrom0SizeNegative() throws Exception {
        Integer userId = 1;

        mockMvc.perform(get("/requests/all?from=0&size=-1")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldGetItemRequestWithFrom0Size20() throws Exception {
        Integer userId = 1;
        when(itemRequestService.getOtherUsers(anyInt(), anyInt(), anyInt())).thenReturn(List.of(itemResponse));

        mockMvc.perform(get("/requests/all?from=0&size=20")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void shouldPostItemRequest() throws Exception {
        String jsonItem = objectMapper.writeValueAsString(itemResponse);
        Integer userId = 1;

        when(itemRequestService.create(any(), anyInt())).thenReturn(itemResponse);

        mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Хотел бы воспользоваться щёткой для обуви"))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    public void shouldGetItemRequestById() throws Exception {
        when(itemRequestService.getRequestById(anyInt(), anyInt())).thenReturn(itemResponse);

        mockMvc.perform(get("/requests/{requestId}", 1)
                        .header(USER_ID_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Хотел бы воспользоваться щёткой для обуви"))
                .andExpect(jsonPath("$.created").isNotEmpty())
                .andExpect(jsonPath("$.items", hasSize(0)));
    }
}
