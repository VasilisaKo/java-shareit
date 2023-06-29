package ru.practicum.shareit.jsondto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class JsonUserDtoTests {
    @Autowired
    private JacksonTester<UserDto> jsonRequest;

    @Test
    void testUserDto() throws Exception {
        UserDto userDto = new UserDto(2, "John Doe", "johndoe@example.com");

        JsonContent<UserDto> result = jsonRequest.write(userDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
    }
}
