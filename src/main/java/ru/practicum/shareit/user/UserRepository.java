package ru.practicum.shareit.user;


import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User save(User user);

    List<User> getAll();

    User getById(int id);

    User update(int id, User user);

    void delete(int id);
}
