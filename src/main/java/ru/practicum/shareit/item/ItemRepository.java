package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository {
    Item save(Item item);

    List<Item> getAll(User user);

    Item getById(int id);

    Item update(int id, Item item);

    void delete(int id);

    List<Item> search(String text);
}
