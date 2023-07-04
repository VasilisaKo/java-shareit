package ru.practicum.gateway.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.HttpHeaders;
import ru.practicum.gateway.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@RestController
@RequestMapping(path = "/requests")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(HttpHeaders.USER_ID) @Positive Integer userId,
                                         @Valid @RequestBody ItemRequestDto dto) {
        log.info("Получен запрос к эндпоинту POST/requests create с headers {}", userId);
        return requestClient.create(userId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> getForUser(@RequestHeader(HttpHeaders.USER_ID) Integer userId) {
        log.info("Получен запрос к эндпоинту GET/requests getForUser с headers {}", userId);
        return requestClient.getForUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherUsers(@RequestHeader(HttpHeaders.USER_ID) Integer userId,
                                                      @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                      @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос к эндпоинту GET/requests getOtherUsers с headers {}, from{}, size{}", userId, from, size);
        return requestClient.getOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(HttpHeaders.USER_ID) Integer userId,
                                                 @PathVariable(name = "requestId") Integer requestId) {
        log.info("Получен запрос к эндпоинту GET/requests getOtherUsers с headers {}, c requestId {}", userId, requestId);
        return requestClient.getRequestById(userId, requestId);
    }
}
