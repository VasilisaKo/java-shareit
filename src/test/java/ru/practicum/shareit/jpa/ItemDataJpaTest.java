package ru.practicum.shareit.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemDataJpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired

    private UserRepository userRepository;

    @Test
    void testItemRequestDto() {
        User user = new User(1, "John Doe", "johndoe@example.com");
        User savedUser = userRepository.save(user);

        Item item = new Item(1, "Веник", "Домашний", savedUser, true, null);
        Item savedItem = itemRepository.save(item);

        Integer savedItemId = savedItem.getId();

        Item retrievedItem = entityManager.find(Item.class, savedItemId);

        assertThat(retrievedItem).isEqualTo(savedItem);
    }
}