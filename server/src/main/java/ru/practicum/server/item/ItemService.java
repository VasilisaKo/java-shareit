package ru.practicum.server.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.server.booking.Booking;
import ru.practicum.server.booking.BookingRepository;
import ru.practicum.server.booking.Status;
import ru.practicum.server.exception.CannotBookItemException;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.item.dto.CommentDto;
import ru.practicum.server.item.dto.CommentResponseDto;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.dto.ItemResponseDto;
import ru.practicum.server.item.model.Comment;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.user.UserMapper;
import ru.practicum.server.user.UserRepository;
import ru.practicum.server.user.UserService;
import ru.practicum.server.user.model.User;

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
    public ItemResponseDto create(ItemDto dto, int userId) {
        User user = UserMapper.toUser(service.getById(userId));
        Item item = ItemMapper.toItem(dto, user);
        Item newItem = itemRepository.save(item);
        return ItemMapper.toItemResponseDto(newItem, new ArrayList<>(), new ArrayList<>());
    }

    public List<ItemResponseDto> getAll(int userId) {
        User user = UserMapper.toUser(service.getById(userId));
        List<Item> itemList = itemRepository.findAllByOwnerOrderById(user);
        List<Integer> itemIdList = itemList.stream().map(Item::getId).collect(Collectors.toList());

        List<Booking> booking = bookingRepository.findAllByOwnerIdAndItemIn(userId, itemIdList);
        List<CommentResponseDto> commentResponseDto = commentRepository.findAllByAndAuthorName(user.getName())
                .stream()
                .map(CommentMapper::toCommentResponseDto).collect(Collectors.toList());
        return itemList.stream()
                .map(item -> ItemMapper.toItemResponseDto(item, booking, commentResponseDto)).collect(Collectors.toList());
    }

    public ItemResponseDto getById(int itemId, int userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item не найден"));
        List<Booking> booking = bookingRepository.findAllByItemIdAndOwnerId(itemId, userId);
        List<CommentResponseDto> commentResponseDto = commentRepository.findAllByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentResponseDto).collect(Collectors.toList());
        return ItemMapper.toItemResponseDto(item, booking, commentResponseDto);
    }

    public ItemResponseDto update(int id, ItemDto dto, int userId) {
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
            return ItemMapper.toItemResponseDto(newItem, new ArrayList<>(), new ArrayList<>());
        }
    }

    public void delete(int id) {
        itemRepository.deleteById(id);
    }

    public List<ItemResponseDto> search(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> itemList = itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(
                text, text, true);
        return itemList.stream().map(item -> ItemMapper.toItemResponseDto(item, new ArrayList<>(), new ArrayList<>()))
                .collect(Collectors.toList());
    }

    public CommentResponseDto createComment(CommentDto dto, Integer userId, Integer itemId) {
        List<Booking> booking = bookingRepository.findAllByBookerIdAndItemIdAndStatusNotAndStartBefore(userId, itemId,
                Status.REJECTED, LocalDateTime.now());
        if (booking.isEmpty()) {
            throw new CannotBookItemException("Вы не можете оставить отзыв, т.к. не бронировали вещь");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User не найден"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item не найден"));
        Comment comment = CommentMapper.toComment(dto, user, item);

        return CommentMapper.toCommentResponseDto(commentRepository.save(comment));
    }
}