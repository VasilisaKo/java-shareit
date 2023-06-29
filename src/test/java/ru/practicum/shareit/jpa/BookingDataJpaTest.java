package ru.practicum.shareit.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookingDataJpaTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void testBookingDto() {
        User user = new User(1, "John Doe", "johndoe@example.com");
        User savedUser = userRepository.save(user);

        User booker = new User(2, "booker Doe", "booker@example.com");
        User savedUserBooker = userRepository.save(booker);

        Item item = new Item(1, "Веник", "Домашний", savedUser, true, null);
        Item savedItem = itemRepository.save(item);

        Booking booking = new Booking(1, savedItem, LocalDateTime.now(), LocalDateTime.now().plusDays(1), savedUserBooker, Status.WAITING);
        Booking savedBooking = bookingRepository.save(booking);

        Integer savedBookingId = savedBooking.getId();

        Booking retrievedBooking = entityManager.find(Booking.class, savedBookingId);

        assertThat(retrievedBooking).isEqualTo(savedBooking);
    }
}
