/*package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserAlreadyExistException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryInMemory implements UserRepository {
    private int userId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        if (isUserExistsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("Пользователь с такой почтой уже существует");
        }
        user.setId(userId++);
        users.put(user.getId(), user);
        log.debug("Создан новый пользователь: {}", user);
        return user;
    }

    public boolean isUserExistsByEmail(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public List<User> getAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getById(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        throw new NotFoundException("Пользователь c id" + id + "не найден");
    }

    public User update(int id, User user) {
        User updateUser = getById(id);
        if (isUserExistsByEmail(user.getEmail())) {
            if (!updateUser.getEmail().equals(user.getEmail())) {
                throw new UserAlreadyExistException("Пользователь с такой почтой уже существует");
            }
        }
        if (!users.containsKey(id)) {
            log.error("Пользователь с id {} не найден", user.getId());
            throw new NotFoundException("Пользователь с id " + user.getId() + "не найден");
        } else {
            if (user.getName() != null && !user.getName().isBlank()) {
                updateUser.setName(user.getName());
            }
            if (user.getEmail() != null && !user.getEmail().isBlank()) {
                updateUser.setEmail(user.getEmail());
            }
            users.put(updateUser.getId(), updateUser);
            log.debug("Пользователь обновлен: {}", user);
            return updateUser;
        }
    }

    @Override
    public void delete(int id) {
        User user = getById(id);
        if (users.containsKey(id)) {
            users.remove(id, user);
            log.debug("Пользователь удален: {}", user);
        } else {
            log.error("Пользователь с id {} не найден", id);
            throw new NotFoundException("Пользователь с id " + id + "не найден");
        }
    }
}*/