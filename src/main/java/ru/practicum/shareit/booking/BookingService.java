package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.CannotBookItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional(readOnly = true)
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Transactional
    public BookingResponseDto create(BookingDto dto, Integer bookerId) {
        if (dto.getStart().isAfter(dto.getEnd()) || dto.getStart().equals(dto.getEnd())) {
            throw new CannotBookItemException("Дата начала позже или равна окончанию бронирования");
        }
        Item item = itemRepository.findById(dto.getItemId()).orElseThrow(() -> new NotFoundException("Item не найден"));

        if (!item.getAvailable()) {
            throw new CannotBookItemException("Вещь не доступна для бронирования");
        }

        if (item.getOwner().getId().equals(bookerId)) {
            throw new NotFoundException("Пользователь не может забронировать свою вещь");
        }

        User booker = UserMapper.toUser(userService.getById(bookerId));
        Booking booking = BookingMapper.toBooking(dto, item, booker);

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponseDto setApproved(Integer userId, Integer bookingId, Boolean approved) {
        Booking booking = bookingRepository.findBookingOwner(bookingId, userId);

        if (booking == null) {
            throw new NotFoundException("Booking не найден");
        }

        if (approved) {
            if (booking.getStatus().equals(Status.APPROVED)) {
                throw new CannotBookItemException("Статус APPROVED уже установлен");
            }
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    public BookingResponseDto getById(Integer userId, Integer bookingId) {
        Booking booking = bookingRepository.findBookingOwnerOrBooker(bookingId, userId);
        if (booking == null) {
            throw new NotFoundException("Booking не найден");
        }
        return BookingMapper.toBookingDto(booking);
    }

    public List<BookingResponseDto> getAllReserve(Integer userId, State state, String typeUser, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        List<Booking> list;
        LocalDateTime time = LocalDateTime.now();
        boolean isOwner = typeUser.equals("owner");

        switch (state) {
            case ALL:
                if (isOwner) {
                    list = bookingRepository.findAllByOwnerIdOrderByStartDesc(userId, page);
                } else {
                    list = bookingRepository.findAllByBookerIdOrderByStartDesc(userId, page);
                }
                break;
            case FUTURE:
                if (isOwner) {
                    list = bookingRepository.findAllByOwnerIdAndStartAfterOrderByStartDesc(userId, time, page);
                } else {
                    list = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, time, page);
                }
                break;
            case WAITING:
                if (isOwner) {
                    list = bookingRepository.findAllByOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING, page);
                } else {
                    list = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING, page);
                }
                break;
            case CURRENT:
                if (isOwner) {
                    list = bookingRepository.findAllByOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, time, time, page);
                } else {
                    list = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, time, time, page);
                }
                break;
            case PAST:
                if (isOwner) {
                    list = bookingRepository.findAllByOwnerIdAndEndBeforeOrderByStartDesc(userId, time, page);
                } else {
                    list = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, time, page);
                }
                break;
            case REJECTED:
                if (isOwner) {
                    list = bookingRepository.findAllByOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, page);
                } else {
                    list = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, page);
                }
                break;
            default:
                throw new ValidationException("UNSUPPORTED_STATUS");
        }

        if (list.isEmpty()) {
            throw new NotFoundException("Бронирование не найдено");
        }

        return list.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

}
