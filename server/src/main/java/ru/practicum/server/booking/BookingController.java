package ru.practicum.server.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.HttpHeaders;
import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.booking.dto.BookingResponseDto;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto addReservation(@RequestHeader(HttpHeaders.USER_ID) int userId,
                                             @RequestBody BookingDto dto) {
        log.info("Получен запрос к эндпоинту POST/bookings addReservation с headers {}", userId);
        return bookingService.create(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateStatus(@RequestHeader(HttpHeaders.USER_ID) Integer userId,
                                           @PathVariable("bookingId") Integer bookingId,
                                           @RequestParam("approved") Boolean approved) {
        log.info("Получен запрос к эндпоинту PATCH/bookings updateStatus с headers {}, с bookingId {}, статус {}",
                userId, bookingId, approved);
        return bookingService.setApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(@RequestHeader(HttpHeaders.USER_ID) Integer userId,
                                      @PathVariable("bookingId") Integer bookingId) {
        log.info("Получен запрос к эндпоинту GET/bookings getById с headers {}, с bookingId {}", userId, bookingId);
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getAllReservation(@RequestHeader(HttpHeaders.USER_ID) Integer userId,
                                                      @RequestParam(value = "state", defaultValue = "ALL") State state,
                                                      @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @RequestParam(name = "size", defaultValue = "10") Integer size)
            throws Throwable {

        log.info("Получен запрос к эндпоинту GET/bookings getAllReservation с state {}", state);
        return bookingService.getAllReserve(userId, state, "booker", from, size);

    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getReservationForOwner(@RequestHeader(HttpHeaders.USER_ID) Integer userId,
                                                           @RequestParam(value = "state", defaultValue = "ALL") State state,
                                                           @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                           @RequestParam(name = "size", defaultValue = "10") Integer size)
            throws Throwable {
        log.info("Получен запрос к эндпоинту GET/bookings getAllReservation с state {}", state);
        return bookingService.getAllReserve(userId, state, "owner", from, size);
    }
}
