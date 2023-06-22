package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Integer id;

    @NotBlank(message = "Отзыв не может быть пустым")
    private String text;

    private Item item;

    private String authorName;

    private LocalDateTime created;

}