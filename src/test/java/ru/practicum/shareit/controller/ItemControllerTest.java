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
import ru.practicum.shareit.HttpHeaders;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private User owner;
    private ItemDto itemDto;
    private ItemResponseDto itemResponseDto;
    @MockBean
    private ItemService itemService;

    @BeforeEach
    public void setUp() {
        owner = new User(2, "newUser", "newUser@user.com");

        Item item = new Item(1, "Дрель", "Простая дрель", owner, true, null);

        itemResponseDto = ItemResponseDto.builder()
                .id(1)
                .name(item.getName())
                .description(item.getDescription())
                .owner(new ItemResponseDto.Owner(owner.getId(), owner.getName()))
                .available(true)
                .build();

        itemDto = ItemDto.builder()
                .id(1)
                .name(item.getName())
                .description(item.getDescription())
                .owner(owner)
                .available(true)
                .build();

    }

    @Test
    public void shouldCreateItem() throws Exception {
        when(itemService.create(any(), anyInt())).thenReturn(itemResponseDto);

        String json = objectMapper.writeValueAsString(itemDto);

        mockMvc.perform(post("/items")
                        .header(HttpHeaders.USER_ID, owner.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Дрель"))
                .andExpect(jsonPath("$.description").value("Простая дрель"))
                .andExpect(jsonPath("$.owner.id").value(2))
                .andExpect(jsonPath("$.owner.name").value("newUser"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    public void shouldGetItemById() throws Exception {
        Integer itemId = 1;
        Integer userId = 1;

        when(itemService.getById(anyInt(), anyInt())).thenReturn(itemResponseDto);

        mockMvc.perform(get("/items/{id}", itemId)
                        .header(HttpHeaders.USER_ID, userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Дрель"))
                .andExpect(jsonPath("$.description").value("Простая дрель"))
                .andExpect(jsonPath("$.available").value("true"));
    }

    @Test
    public void shouldNotPostItemWithoutXSharerUserId() throws Exception {
        Item item = new Item(2, "Дрель", "Простая дрель", owner, true, null);
        String jsonItem = objectMapper.writeValueAsString(item);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotPostItemWithoutAvailable() throws Exception {
        Item item = new Item(2, "Дрель", "Простая дрель", owner, null, null);
        String jsonItem = objectMapper.writeValueAsString(item);
        Long userId = 2L;

        mockMvc.perform(post("/items")
                        .header(HttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotPostItemWitEmptyName() throws Exception {
        Item item = new Item(2, "", "Простая дрель", owner, true, null);
        String jsonItem = objectMapper.writeValueAsString(item);
        Integer userId = 2;

        mockMvc.perform(post("/items")
                        .header(HttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void shouldNotPostItemWithEmptyDescription() throws Exception {
        Item item = new Item(2, "Дрель", "", owner, true, null);
        String jsonItem = objectMapper.writeValueAsString(item);
        Integer userId = 2;

        mockMvc.perform(post("/items")
                        .header(HttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonItem))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldGetAllItem() throws Exception {
        Integer userId = 1;

        when(itemService.getAll(anyInt())).thenReturn(List.of(itemResponseDto, itemResponseDto, itemResponseDto));

        mockMvc.perform(get("/items")
                        .header(HttpHeaders.USER_ID, userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Дрель"))
                .andExpect(jsonPath("$[0].description").value("Простая дрель"))
                .andExpect(jsonPath("$[0].available").value("true"));
    }

    @Test
    public void shouldUpdateItem() throws Exception {
        Integer itemId = 1;
        Integer userId = 1;
        String json = objectMapper.writeValueAsString(itemDto);

        when(itemService.update(anyInt(), any(), anyInt())).thenReturn(itemResponseDto);

        itemResponseDto.setName("Дрель+");

        mockMvc.perform(patch("/items/{id}", itemId)
                        .header(HttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Дрель+"))
                .andExpect(jsonPath("$.description").value("Простая дрель"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    public void shouldDeleteItem() throws Exception {
        Integer userId = 1;

        mockMvc.perform(delete("/items/1")
                        .header(HttpHeaders.USER_ID, userId))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSearchItem() throws Exception {
        Integer userId = 1;

        when(itemService.search(anyString())).thenReturn(List.of(itemResponseDto, itemResponseDto));

        mockMvc.perform(get("/items/search?text=дрель")
                        .header(HttpHeaders.USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Дрель"))
                .andExpect(jsonPath("$[0].description").value("Простая дрель"))
                .andExpect(jsonPath("$[0].available").value("true"));
    }
}
