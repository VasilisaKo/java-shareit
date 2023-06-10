package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingResponseDto addReservation(@RequestHeader(USER_ID_HEADER) int userId,
                                             @Valid @RequestBody BookingDto dto) {
        log.info("Получен запрос к эндпоинту POST/bookings addReservation с headers {}", userId);
        return bookingService.create(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateStatus(@RequestHeader(USER_ID_HEADER) Integer userId,
                                           @PathVariable("bookingId") Integer bookingId,
                                           @RequestParam("approved") Boolean approved) {
        log.info("Получен запрос к эндпоинту PATCH/bookings updateStatus с headers {}, с bookingId {}, статус {}",
                userId, bookingId, approved);
        return bookingService.setApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(@RequestHeader(USER_ID_HEADER) Integer userId,
                                      @PathVariable("bookingId") Integer bookingId) {
        log.info("Получен запрос к эндпоинту GET/bookings getById с headers {}, с bookingId {}", userId, bookingId);
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getAllReservation(@RequestHeader(USER_ID_HEADER) Integer userId,
                                                      @RequestParam(value = "state", required = false) State state) {
        log.info("Получен запрос к эндпоинту GET/bookings getAllReservation с state {}", state);
        return bookingService.getAllReserve(userId, state, "booker");
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getReservationForOwner(@RequestHeader(USER_ID_HEADER) Integer userId,
                                                           @RequestParam(value = "state", required = false) State state) {
        log.info("Получен запрос к эндпоинту GET/bookings getAllReservation с state {}", state);
        return bookingService.getAllReserve(userId, state, "owner");
    }
}
