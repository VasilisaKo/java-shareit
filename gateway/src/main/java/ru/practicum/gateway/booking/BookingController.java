package ru.practicum.gateway.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.booking.dto.State;
import ru.practicum.gateway.HttpHeaders;
import ru.practicum.gateway.booking.dto.BookingRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addReservation(@RequestHeader(HttpHeaders.USER_ID) int userId,
                                             @Valid @RequestBody BookingRequestDto requestDto) {
        log.info("Получен запрос к эндпоинту POST/bookings addReservation с headers {}", userId);
        return bookingClient.create(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader(HttpHeaders.USER_ID) Integer userId,
                                           @PathVariable("bookingId") Integer bookingId,
                                           @RequestParam("approved") Boolean approved) {
        log.info("Получен запрос к эндпоинту PATCH/bookings updateStatus с headers {}, с bookingId {}, статус {}",
                userId, bookingId, approved);
        return bookingClient.setApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader(HttpHeaders.USER_ID) Integer userId,
                                      @PathVariable("bookingId") Integer bookingId) {
        log.info("Получен запрос к эндпоинту GET/bookings getById с headers {}, с bookingId {}", userId, bookingId);
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllReservation(@RequestHeader(HttpHeaders.USER_ID) Integer userId,
                                                    @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                    @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        State state = State.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Получен запрос к эндпоинту GET/bookings getAllReservation с state {}", stateParam);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getReservationForOwner(@RequestHeader(HttpHeaders.USER_ID) Integer userId,
                                                           @RequestParam(value = "state", defaultValue = "ALL") String stateParam,
                                                           @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                           @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос к эндпоинту GET/bookings getAllReservation с state {}", stateParam);
        State state = State.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getAllReserve(userId, state, from, size);
    }
}
