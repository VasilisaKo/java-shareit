package ru.practicum.gateway.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.HttpHeaders;
import ru.practicum.gateway.item.dto.CommentDto;
import ru.practicum.gateway.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/items")
@Slf4j
@AllArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(HttpHeaders.USER_ID) Integer userId,
                                         @Valid @RequestBody ItemDto dto) {
        log.info("Получен запрос POST /items create с headers {}", userId);
        return itemClient.create(dto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(HttpHeaders.USER_ID) int userId) {
        log.info("Получен запрос GET: /items getAll с headers {}", userId);
        return itemClient.getAll(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@RequestHeader(HttpHeaders.USER_ID) @Positive int userId,
                                   @PathVariable("id") @Positive int itemId) {
        log.info("Получен запрос GET: /items geById с id={}", itemId);
        return itemClient.getById(itemId, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestHeader(HttpHeaders.USER_ID) int userId,
                                  @PathVariable("id") int itemId,
                                  @RequestBody ItemDto dto) {
        log.info("Получен запрос PATCH: /items update с ItemId={} с headers {}", itemId, userId);
        //return itemService.update(itemId, dto, userId);
        return itemClient.update(itemId, userId, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int itemId) {
        log.info("Получен запрос DELETE: /items delete с id={}", itemId);
        itemClient.delete(itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("text") String text) {
        log.info("Получен запрос GET: items/search с text: {}", text);
        return itemClient.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(HttpHeaders.USER_ID) int userId,
                                         @PathVariable("itemId") @Positive int itemId,
                                         @Valid @RequestBody CommentDto comment) {
        log.info("Получен запрос к эндпоинту POST: /items{itemId}/comment addComment с headers {}, с itemId {}",
                userId, itemId);
        return itemClient.createComment(itemId, userId, comment);
    }
}