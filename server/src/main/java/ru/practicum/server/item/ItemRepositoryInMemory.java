/*package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ItemRepositoryInMemory implements ItemRepository {
    private int itemId = 1;
    private final Map<Integer, Item> items = new HashMap<>();

    @Override
    public Item save(Item item) {
        item.setId(itemId);
        items.put(item.getId(), item);
        log.debug("Создана новая вещь: {}", item);
        itemId++;
        return item;
    }

    @Override
    public List<Item> getAll(User user) {
        return items.values().stream().filter(item -> item.getOwner().equals(user)).collect(Collectors.toList());
    }

    @Override
    public Item getById(int id) {
        return items.get(id);
    }

    @Override
    public Item update(int id, Item item) {
        Item updateItem = items.get(id);

        if (!updateItem.getOwner().equals(item.getOwner())) {
            log.warn("Вещь с таким ID = {} не найдена", item.getId());
            throw new NotFoundException("Вещь с таким ID не найдена");
        }

        if (item.getName() != null && !item.getName().isBlank()) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        items.put(id, updateItem);

        return updateItem;
    }

    @Override
    public void delete(int id) {
        items.remove(id);
    }

    @Override
    public List<Item> search(String text) {
        List<Item> itemList = new ArrayList<>();
        if (text.isBlank()) {
            return itemList;
        }

        for (Map.Entry<Integer, Item> entry : items.entrySet()) {
            Item item = entry.getValue();

            if (item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text)) {
                if (item.getAvailable()) {
                    itemList.add(item);
                }
            }
        }
        return itemList;
    }
}*/
