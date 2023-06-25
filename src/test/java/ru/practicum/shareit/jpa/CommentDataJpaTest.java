package ru.practicum.shareit.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommentDataJpaTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;


    @Test
    void testItemRequestDto() {
        User user = new User(1, "John Doe", "johndoe@example.com");
        User savedUser = userRepository.save(user);

        User booker = new User(2, "booker Doe", "booker@example.com");
        User savedUserBooker = userRepository.save(booker);

        Item item = new Item(1, "Веник", "Домашний", savedUser, true, null);
        Item savedItem = itemRepository.save(item);

        Comment comment = new Comment(1, "BlaBlaBla", savedItem, savedUserBooker.getName(), LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);

        Integer savedCommentId = savedComment.getId();

        Comment retrievedComment = entityManager.find(Comment.class, savedCommentId);

        assertThat(retrievedComment).isEqualTo(savedComment);
    }
}
