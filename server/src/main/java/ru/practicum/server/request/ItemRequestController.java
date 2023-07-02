package ru.practicum.server.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.HttpHeaders;
import ru.practicum.server.request.dto.ItemRequestDto;
import ru.practicum.server.request.dto.ItemRequestResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestResponseDto create(@RequestHeader(HttpHeaders.USER_ID) @Positive Integer userId,
                                         @Valid @RequestBody ItemRequestDto dto) {
        log.info("Получен запрос к эндпоинту POST/requests create с headers {}", userId);
        return requestService.create(dto, userId);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getForUser(@RequestHeader(HttpHeaders.USER_ID) Integer userId) {
        log.info("Получен запрос к эндпоинту GET/requests getForUser с headers {}", userId);
        return requestService.getForUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getOtherUsers(@RequestHeader(HttpHeaders.USER_ID) Integer userId,
                                                      @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                      @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос к эндпоинту GET/requests getOtherUsers с headers {}, from{}, size{}", userId, from, size);
        return requestService.getOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getRequestById(@RequestHeader(HttpHeaders.USER_ID) Integer userId,
                                                 @PathVariable(name = "requestId") Integer requestId) {
        log.info("Получен запрос к эндпоинту GET/requests getOtherUsers с headers {}, c requestId {}", userId, requestId);
        return requestService.getRequestById(userId, requestId);
    }
}
