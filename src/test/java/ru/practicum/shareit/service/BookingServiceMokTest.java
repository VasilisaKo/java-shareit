package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingServiceMokTest {
    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;
    private User booker;
    private User owner;
    private Item item;
    private Booking booking;
    private List<Booking> bookingList;

    @BeforeEach
    public void setUp() {
        booker = new User(1, "user", "user@user.com");

        owner = new User(2, "newUser", "newUser@user.com");

        item = new Item(1, "Дрель", "Простая дрель", owner, true, null);

        LocalDateTime start = LocalDateTime.now().plusMinutes(1).withNano(0);
        LocalDateTime end = start.plusDays(1).withNano(0);

        booking = new Booking(1, item, start, end, booker, null);

        bookingList = new ArrayList<>();
        bookingList.add(booking);
        bookingList.add(booking);
    }


    @Test
    void getAllBookingsStateEmpty() {
        Integer userId = 1;
        State state = State.PAST;
        String typeUser = "owner";
        int from = 0;
        int size = 10;

        List<Booking> bookingList = new ArrayList<>();
        Mockito.when(bookingRepository.findAllByOwnerIdAndEndBeforeOrderByStartDesc(anyInt(), any(), any()))
                .thenReturn(bookingList);

        assertThrows(NotFoundException.class, () -> {
            bookingService.getAllReserve(userId, state, typeUser, from, size);
        });
    }

    @Test
    void getAllBookingsStateFutureOwner() {
        Integer userId = 1;
        State state = State.FUTURE;
        String typeUser = "owner";
        int from = 0;
        int size = 10;

        Mockito.when(bookingRepository.findAllByOwnerIdAndStartAfterOrderByStartDesc(anyInt(), any(), any()))
                .thenReturn(bookingList);

        List<BookingResponseDto> bookingResponseDto = bookingService.getAllReserve(userId, state, typeUser, from, size);
        assertNotNull(bookingResponseDto);
        assertEquals(2, bookingResponseDto.size());
    }

    @Test
    void getAllBookingsStateWAITING() {
        Integer userId = 1;
        State state = State.WAITING;
        String typeUser = "booker";
        int from = 0;
        int size = 10;

        Mockito.when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyInt(), any(), any()))
                .thenReturn(bookingList);

        List<BookingResponseDto> bookingResponseDto = bookingService.getAllReserve(userId, state, typeUser, from, size);
        assertNotNull(bookingResponseDto);
        assertEquals(2, bookingResponseDto.size());
    }

    @Test
    void getAllBookingsStateCURRENT() {
        Integer userId = 1;
        State state = State.CURRENT;
        String typeUser = "booker";
        int from = 0;
        int size = 10;

        Mockito.when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyInt(), any(), any(), any()))
                .thenReturn(bookingList);

        List<BookingResponseDto> bookingResponseDto = bookingService.getAllReserve(userId, state, typeUser, from, size);
        assertNotNull(bookingResponseDto);
        assertEquals(2, bookingResponseDto.size());
    }

    @Test
    void getAllBookingsStateREJECTED() {
        Integer userId = 1;
        State state = State.REJECTED;
        String typeUser = "booker";
        int from = 0;
        int size = 10;

        Mockito.when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(anyInt(), any(), any()))
                .thenReturn(bookingList);

        List<BookingResponseDto> bookingResponseDto = bookingService.getAllReserve(userId, state, typeUser, from, size);
        assertNotNull(bookingResponseDto);
        assertEquals(2, bookingResponseDto.size());
    }
}
