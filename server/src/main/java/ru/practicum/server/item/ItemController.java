package ru.practicum.server.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.HttpHeaders;
import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.item.dto.CommentResponseDto;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.dto.ItemResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto create(@RequestHeader(HttpHeaders.USER_ID) Integer userId,
                                  @RequestBody ItemDto dto) {
        log.info("Получен запрос POST /items create с headers {}", userId);
        return itemService.create(dto, userId);
    }

    @GetMapping
    public List<ItemResponseDto> getAll(@RequestHeader(HttpHeaders.USER_ID) int userId) {
        log.info("Получен запрос GET: /items getAll с headers {}", userId);
        return itemService.getAll(userId);
    }

    @GetMapping("/{id}")
    public ItemResponseDto getById(@RequestHeader(HttpHeaders.USER_ID) int userId,
                                   @PathVariable("id") int itemId) {
        log.info("Получен запрос GET: /items geById с id={}", itemId);
        return itemService.getById(itemId, userId);
    }

    @PatchMapping("/{id}")
    public ItemResponseDto update(@RequestHeader(HttpHeaders.USER_ID) int userId,
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
    public CommentResponseDto addComment(@RequestHeader(HttpHeaders.USER_ID) int userId,
                                         @PathVariable("itemId") int itemId,
                                         @RequestBody CommentDto comment) {
        log.info("Получен запрос к эндпоинту POST: /items{itemId}/comment addComment с headers {}, с itemId {}",
                userId, itemId);
        return itemService.createComment(comment, userId, itemId);
    }
}