package ru.practicum.shareit.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserDataJpaTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired

    private UserRepository userRepository;

    @Test
    void testUserDto() throws Exception {
        LocalDateTime time = LocalDateTime.now().withNano(000000);

        User user = new User(2, "John Doe", "johndoe@example.com");
        User savedUser = userRepository.save(user);

        Integer savedUserId = savedUser.getId();

        User retrievedUser = entityManager.find(User.class, savedUserId);

        assertThat(retrievedUser).isEqualTo(savedUser);
    }

    /*@Test
    void testUseWithDuplicateEmail() throws Exception {
        LocalDateTime time = LocalDateTime.now().withNano(000000);

        User user = new User(2, "John Doe", "johndoe@example.com");
        User savedUser = userRepository.save(user);

        Integer savedUserId = savedUser.getId();

        User retrievedUser = entityManager.find(User.class, savedUserId);

        assertThat(retrievedUser).isEqualTo(savedUser);

        assertThrows(DataIntegrityViolationException.class, () -> {
            User userDuplicate = new User(3, "New DoeS", "johndoe@example.com");
            User savedUserDuplicate = userRepository.save(user);
        });
    }*/
}