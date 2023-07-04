package ru.practicum.server.item.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.server.booking.dto.BookingDtoForItem;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponseDto {
    private Integer id;
    private String name;
    private String description;
    private Owner owner;
    private Boolean available;
    private Integer requestId;
    private BookingDtoForItem lastBooking;
    private BookingDtoForItem nextBooking;
    private List<CommentResponseDto> comments;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Owner {
        private int id;
        private String name;
    }
}
