package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {
    private Integer id;

    private Item item;

    private LocalDateTime start;

    private LocalDateTime end;

    private Booker booker;

    private Status status;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Booker {
        private Integer id;
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private Integer id;
        private String name;
    }
}
