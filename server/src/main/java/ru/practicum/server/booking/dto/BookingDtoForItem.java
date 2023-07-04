package ru.practicum.server.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.server.booking.Status;


import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder

public class BookingDtoForItem {

    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Integer bookerId;
    private Status status;
}
