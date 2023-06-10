package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoForItem {
    private Integer id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Integer bookerId;

    private Status status;
}
