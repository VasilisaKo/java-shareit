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
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentControllerTest {
    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @MockBean
    private ItemService itemService;


    private User booker;
    private User owner;
    private Item item;
    private CommentDto commentDto;
    private CommentResponseDto commentResponseDto;
    LocalDateTime start;
    LocalDateTime end;

    @BeforeEach
    public void setUp() throws Exception {
        booker = new User(1, "user", "user@user.com");

        owner = new User(2, "newUser", "newUser@user.com");

        item = new Item(1, "Дрель", "Простая дрель", owner, true, null);

        Integer bookerId = 2;
        start = LocalDateTime.now().plusMinutes(1);
        end = start.plusDays(1);


        commentDto = new CommentDto(null, "Add comment from user2", item, booker.getName(), end);

        commentResponseDto = CommentResponseDto
                .builder()
                .id(1)
                .authorName(commentDto.getAuthorName())
                .created(commentDto.getCreated())
                .item(new CommentResponseDto.Item(item.getId(), item.getName()))
                .text(commentDto.getText())
                .build();

    }

    @Test
    public void shouldCreateComment() throws Exception {
        when(itemService.createComment(any(), anyInt(), anyInt())).thenReturn(commentResponseDto);

        String jsonCommentDto = objectMapper.writeValueAsString(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", item.getId())
                        .header(USER_ID_HEADER, booker.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommentDto))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.item.id").value(1))
                .andExpect(jsonPath("$.item.name").value("Дрель"))
                .andExpect(jsonPath("$.created").isNotEmpty())
                .andExpect(jsonPath("$.authorName").value("user"))
                .andExpect(jsonPath("$.text").value("Add comment from user2"));
    }

    @Test
    public void shouldNotCreateCommentWithEmptyText() throws Exception {
        when(itemService.createComment(any(), anyInt(), anyInt())).thenReturn(commentResponseDto);

        commentDto.setText("");

        String jsonCommentDto = objectMapper.writeValueAsString(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", item.getId())
                        .header(USER_ID_HEADER, booker.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommentDto))
                .andExpect(status().is4xxClientError());
    }

    /*@Test
    public void shouldNotCreateCommentWithNegativeItem() throws Exception {
        when(itemService.createComment(any(), anyInt(), anyInt())).thenReturn(commentResponseDto);

        String jsonCommentDto = objectMapper.writeValueAsString(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", -1)
                        .header(USER_ID_HEADER, booker.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCommentDto))
                .andExpect(status().is4xxClientError());
    }*/
}
