package ru.practicum.server.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.server.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByItemId(Integer itemId);

    List<Comment> findAllByAndAuthorName(String author);

}