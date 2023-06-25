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
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.CannotBookItemException;
import ru.practicum.shareit.exception.NotFoundException;

import ru.practicum.shareit.item.model.Item;

import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private User booker;
    private BookingDto bookingDto;
    private BookingResponseDto bookingResponseDto;
    private LocalDateTime start;
    private LocalDateTime end;
    
    @MockBean
    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        booker = new User(1, "user", "user@user.com");

        User owner = new User(2, "newUser", "newUser@user.com");

        Item item = new Item(1, "Дрель", "Простая дрель", owner, true, null);

        start = LocalDateTime.now().plusMinutes(1).withNano(0);
        end = start.plusDays(1).withNano(0);

        bookingDto = new BookingDto(1, 1, start, end, null);

        bookingResponseDto = BookingResponseDto
                .builder()
                .id(bookingDto.getId())
                .status(Status.WAITING)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(new BookingResponseDto.Item(item.getId(), item.getName()))
                .booker(new BookingResponseDto.Booker(booker.getId(), booker.getName()))
                .build();

    }

    @Test
    public void shouldCreateBooking() throws Exception {
        when(bookingService.create(any(), anyInt())).thenReturn(bookingResponseDto);

        String jsonBooking = objectMapper.writeValueAsString(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header(HttpHeaders.USER_ID, booker.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBooking))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.item.id").value(1))
                .andExpect(jsonPath("$.item.name").value("Дрель"))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())
                .andExpect(jsonPath("$.booker.id").value(1))
                .andExpect(jsonPath("$.booker.name").value("user"));
    }

    @Test
    public void shouldGetBookingById() throws Exception {
        Integer bookingId = 1;
        Integer userId = 1;

        when(bookingService.getById(anyInt(), anyInt())).thenReturn(bookingResponseDto);

        mockMvc.perform(get("/bookings/{id}", bookingId)
                        .header(HttpHeaders.USER_ID, userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.item.id").value(1))
                .andExpect(jsonPath("$.item.name").value("Дрель"))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.start").isNotEmpty())
                .andExpect(jsonPath("$.end").isNotEmpty())
                .andExpect(jsonPath("$.booker.id").value(1))
                .andExpect(jsonPath("$.booker.name").value("user"));
    }

    @Test
    public void shouldUpdateBooking() throws Exception {
        Integer bookingId = 1;
        Integer userId = 1;
        bookingResponseDto.setStatus(Status.APPROVED);

        when(bookingService.setApproved(anyInt(), anyInt(), anyBoolean())).thenReturn(bookingResponseDto);

        mockMvc.perform(patch("/bookings/{bookingId}?approved=true", bookingId)
                        .header(HttpHeaders.USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    public void shouldNotUpdateBookingWhenUserWithoutBooking() throws Exception {
        Integer bookingId = 99;
        Integer userId = 1;

        when(bookingService.setApproved(anyInt(), anyInt(), anyBoolean()))
                .thenThrow(new NotFoundException("Booking не найден"));

        mockMvc.perform(patch("/bookings/{bookingId}?approved=true", bookingId)
                        .header(HttpHeaders.USER_ID, userId))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error", is("Booking не найден")));
    }

    @Test
    public void shouldNotUpdateBookingsWhenUserWithApproved() throws Exception {
        Integer bookingId = 1;
        Integer userId = 1;

        when(bookingService.setApproved(anyInt(), anyInt(), anyBoolean()))
                .thenThrow(new CannotBookItemException("Статус APPROVED уже установлен"));

        mockMvc.perform(patch("/bookings/{bookingId}?approved=true", bookingId)
                        .header(HttpHeaders.USER_ID, userId))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error", is("Статус APPROVED уже установлен")));

    }

    @Test
    public void shouldUpdateBookingsWithApprovedFalse() throws Exception {
        Integer bookingId = 1;
        Integer userId = 1;
        bookingResponseDto.setStatus(Status.REJECTED);

        when(bookingService.setApproved(anyInt(), anyInt(), anyBoolean())).thenReturn(bookingResponseDto);

        mockMvc.perform(patch("/bookings/{bookingId}?approved=false", bookingId)
                        .header(HttpHeaders.USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));

    }

    @Test
    public void shouldNotUpdateBookingsWithUnknownUser() throws Exception {
        Integer bookingId = 1;
        Integer userId = 100;

        when(bookingService.setApproved(anyInt(), anyInt(), anyBoolean()))
                .thenThrow(new NotFoundException("Booking не найден"));

        mockMvc.perform(patch("/bookings/{bookingId}?approved=true", bookingId)
                        .header(HttpHeaders.USER_ID, userId))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.error", is("Booking не найден")));
    }

    @Test
    public void shouldGetAllReserveBookings() throws Exception {
        Integer userId = 2;

        when(bookingService.getAllReserve(anyInt(), any(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponseDto, bookingResponseDto, bookingResponseDto));

        mockMvc.perform(get("/bookings")
                        .header(HttpHeaders.USER_ID, userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void shouldGetAllReserveBookingsByOwner() throws Exception {
        Integer userId = 2;

        when(bookingService.getAllReserve(anyInt(), any(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponseDto, bookingResponseDto));

        mockMvc.perform(get("/bookings/owner")
                        .header(HttpHeaders.USER_ID, userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void shouldNotGetBookingWithNegativeSize() throws Exception {
        Integer bookingId = 1;
        Integer userId = 1;

        mockMvc.perform(get("/bookings/owner?size=-1&from=0", bookingId)
                        .header(HttpHeaders.USER_ID, userId))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotGetBookingsWithNegativeFrom() throws Exception {
        Integer bookingId = 1;
        Integer userId = 1;

        mockMvc.perform(get("/bookings/owner?size=10&from=-1", bookingId)
                        .header(HttpHeaders.USER_ID, userId))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}