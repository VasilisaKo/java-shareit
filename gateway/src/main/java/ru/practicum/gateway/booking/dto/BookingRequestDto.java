package ru.practicum.gateway.booking.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class BookingRequestDto {

    private Integer id;

    @NotNull(message = "itemId не может быть пустым")
    private Integer itemId;

    @NotNull(message = "Поле start бронирования не может быть пустым")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @FutureOrPresent(message = "Дата start бронирования не может быть в прошлом")
    private LocalDateTime start;

    @NotNull(message = "Поле end бронирования не может быть пустым")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @FutureOrPresent(message = "Дата end бронирования не может быть в прошлом")
    private LocalDateTime end;

    private Status status;
}
