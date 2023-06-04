package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@Slf4j
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto create(@RequestHeader(USER_ID_HEADER) Integer userId,
                          @Valid @RequestBody ItemDto dto) {
        log.info("Получен запрос POST /items create с headers {}", userId);
        return itemService.create(dto, userId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(USER_ID_HEADER) int userId) {
        log.info("Получен запрос GET: /items getAll с headers {}", userId);
        return itemService.getAll(userId);
    }

    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable("id") int itemId) {
        log.info("Получен запрос GET: /items geById с id={}", itemId);
        return itemService.getById(itemId);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(USER_ID_HEADER) int userId,
                          @PathVariable("id") int itemId,
                          @RequestBody ItemDto dto) {
        log.info("Получен запрос PATCH: /items update с ItemId={} с headers {}", itemId, userId);
        return itemService.update(itemId, dto, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int userId) {
        log.info("Получен запрос DELETE: /items delete с id={}", userId);
        itemService.delete(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        log.info("Получен запрос GET: items/search с text: {}", text);
        return itemService.search(text);
    }
}