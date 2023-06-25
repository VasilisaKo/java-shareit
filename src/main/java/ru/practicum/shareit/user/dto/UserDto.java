package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email;
}
