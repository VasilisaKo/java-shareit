package ru.practicum.shareit.jsondto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class JsonItemDtoTest {
    @Autowired
    private JacksonTester<Item> jsonItem;
    @Autowired
    private JacksonTester<ItemDto> jsonItemDto;

    @Autowired
    private JacksonTester<CommentResponseDto> jsonCommentResponseDto;

    @Test
    void testItemDto() throws Exception {
        User user = new User(1, "Test User", "test@example.com");
        Item item = new Item(1, "Test Item", "Test Description", user, true, 1);
        ItemDto itemDto = ItemMapper.toItemDto(item);

        ItemDto itemDtoNew = new ItemDto(2, "Test ItemDto", "Test DescriptionDto", user, true, 1);
        Item newItem = ItemMapper.toItem(itemDtoNew, new User());

        JsonContent<Item> result = jsonItem.write(newItem);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Test ItemDto");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test DescriptionDto");
    }
}
