package ru.practicum.shareit.item.dto;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Integer id;

    @NotBlank(message = "Отзыв не может быть пустым")
    private String text;

    private Item item;

    private String authorName;

    private LocalDateTime created;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private int id;
        private String name;
    }
}