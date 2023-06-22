package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemResponseDto create(@RequestHeader(USER_ID_HEADER) Integer userId,
                          @Valid @RequestBody ItemDto dto) {
        log.info("Получен запрос POST /items create с headers {}", userId);
        return itemService.create(dto, userId);
    }

    @GetMapping
    public List<ItemResponseDto> getAll(@RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Получен запрос GET: /items getAll с headers {}", userId);
        return itemService.getAll(userId);
    }

    @GetMapping("/{id}")
    public ItemResponseDto getById(@RequestHeader(USER_ID_HEADER) @Positive int userId,
                                   @PathVariable("id") @Positive int itemId) {
        log.info("Получен запрос GET: /items geById с id={}", itemId);
        return itemService.getById(itemId, userId);
    }

    @PatchMapping("/{id}")
    public ItemResponseDto update(@RequestHeader(USER_ID_HEADER) int userId,
                          @PathVariable("id") int itemId,
                          @RequestBody ItemDto dto) {
        log.info("Получен запрос PATCH: /items update с ItemId={} с headers {}", itemId, userId);
        return itemService.update(itemId, dto, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int itemId) {
        log.info("Получен запрос DELETE: /items delete с id={}", itemId);
        itemService.delete(itemId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> search(@RequestParam("text") String text) {
        log.info("Получен запрос GET: items/search с text: {}", text);
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@RequestHeader(USER_ID_HEADER) int userId,
                                         @PathVariable("itemId") int itemId,
                                         @Valid @RequestBody CommentDto comment) {
        log.info("Получен запрос к эндпоинту POST: /items{itemId}/comment addComment с headers {}, с itemId {}",
                userId, itemId);
        return itemService.createComment(comment, userId, itemId);
    }
}