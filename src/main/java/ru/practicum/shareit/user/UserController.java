package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) {
        log.info("Получен запрос POST /users.\n" + user.toString());
        return userService.create(user);
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Получен запрос GET /users.");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable("id") Integer userId) {
        log.info("Получен запрос GET: /users getById с id={}", userId);
        return userService.getById(userId);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable("id") Integer userId,
                          @RequestBody UserDto dto) {
        log.info("Получен запрос PATCH: /users update с id={}", userId);
        return userService.update(userId, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer userId) {
        log.info("Получен запрос DELETE: /users delete с id={}", userId);
        userService.delete(userId);
    }
}