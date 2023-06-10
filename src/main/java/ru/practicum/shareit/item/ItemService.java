package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.CannotBookItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final UserService service;

    @Transactional
    public ItemDto create(ItemDto dto, int userId) {
        User user = UserMapper.toUser(service.getById(userId));
        Item item = ItemMapper.toItem(dto, user);
        Item newItem = itemRepository.save(item);
        return ItemMapper.toItemDto(newItem);
    }

    public List<ItemResponseDto> getAll(int userId) {
        User user = UserMapper.toUser(service.getById(userId));
        List<Item> itemList = itemRepository.findAllByOwnerOrderById(user);
        List<Integer> itemIdList = itemList.stream().map(Item::getId).collect(Collectors.toList());

        List<Booking> booking = bookingRepository.findAllByOwnerIdAndItemIn(userId, itemIdList);
        List<Comment> comment = commentRepository.findAllByAndAuthorName(user.getName());

        return itemList.stream()
                .map(item -> ItemMapper.toItemResponseDto(item, booking, comment)).collect(Collectors.toList());
    }

    public ItemResponseDto getById(int itemId, int userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item не найден"));
        List<Booking> booking = bookingRepository.findAllByItemIdAndOwnerId(itemId, userId);
        List<Comment> comment = commentRepository.findAllByItemId(itemId);

        return ItemMapper.toItemResponseDto(item, booking, comment);
    }

    public ItemDto update(int id, ItemDto dto, int userId) {
        User user = UserMapper.toUser(service.getById(userId));
        Item item = ItemMapper.toItem(dto, user);
        Item updateItem = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item не найден"));
        if (!updateItem.getOwner().getId().equals(userId)) {
            throw new NotFoundException("У пользователя не найдена вещь");
        } else {
            if (item.getAvailable() != null) {
                updateItem.setAvailable(item.getAvailable());
            }
            if (StringUtils.hasLength(item.getName())) {
                updateItem.setName(item.getName());
            }
            if (StringUtils.hasLength(item.getDescription())) {
                updateItem.setDescription(item.getDescription());
            }
            Item newItem = itemRepository.save(updateItem);
            return ItemMapper.toItemDto(newItem);
        }
    }

    public void delete(int id) {
        itemRepository.deleteById(id);
    }

    public List<ItemDto> search(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> itemList = itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(
                text, text, true);
        return itemList.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public Comment addComment(CommentDto dto, int userId, int itemId) {
        List<Booking> booking = bookingRepository.findAllByBookerIdAndItemIdAndStatusNotAndStartBefore(userId, itemId,
                Status.REJECTED, LocalDateTime.now());
        if (booking.isEmpty()) {
            throw new CannotBookItemException("Пользователь не бронировал вещь");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item не найден"));
        Comment comment = CommentMapper.toComment(dto, user, item);
        return commentRepository.save(comment);
    }
}