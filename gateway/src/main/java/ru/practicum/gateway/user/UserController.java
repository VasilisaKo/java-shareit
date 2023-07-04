package ru.practicum.gateway.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.gateway.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto user) {
        log.info("Получен запрос POST /users c user={}" + user.toString());
        return userClient.create(user);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Получен запрос GET /users.");
        return userClient.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Integer userId) {
        log.info("Получен запрос GET: /users getById с id={}", userId);
        return userClient.getById(userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Integer userId,
                          @RequestBody UserDto dto) {
        log.info("Получен запрос PATCH: /users update с id={}", userId);
        return userClient.update(userId, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer userId) {
        log.info("Получен запрос DELETE: /users delete с id={}", userId);
        userClient.delete(userId);
    }
}