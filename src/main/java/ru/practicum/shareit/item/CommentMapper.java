package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment, User author) {
        return CommentDto
                .builder()
                .id(comment.getId())
                .authorName(author.getName())
                .created(comment.getCreated())
                .text(comment.getText())
                .build();
    }

    public static Comment toComment(CommentDto dto, User author, Item item) {
        return Comment.builder()
                .authorName(author.getName())
                .created(LocalDateTime.now())
                .text(dto.getText())
                .item(item)
                .build();
    }

    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto
                .builder()
                .id(comment.getId())
                .authorName(comment.getAuthorName())
                .created(LocalDateTime.now())
                .text(comment.getText())
                .item(new CommentResponseDto.Item(
                        comment.getItem().getId(),
                        comment.getItem().getName()))
                .build();
    }
}
