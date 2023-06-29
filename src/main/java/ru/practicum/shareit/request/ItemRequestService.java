package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    public ItemRequestResponseDto create(ItemRequestDto dto, Integer userId) {
        userService.getById(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(dto, userId);
        return ItemRequestMapper.toItemRequestResponseDto(requestRepository.save(itemRequest), new ArrayList<>());
    }

    public List<ItemRequestResponseDto> getForUser(Integer userId) {
        userService.getById(userId);
        List<ItemRequest> itemRequests = requestRepository.findAllByRequestorOrderByCreatedDesc(userId);
        return getItemRequestResponseDto(itemRequests);
    }

    public List<ItemRequestResponseDto> getOtherUsers(Integer userId, Integer from, Integer size) {
        userService.getById(userId);
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<ItemRequest> itemRequests = requestRepository.findAllByRequestorIsNotOrderByCreatedDesc(userId, page);
        return getItemRequestResponseDto(itemRequests);
    }

    private List<ItemRequestResponseDto> getItemRequestResponseDto(List<ItemRequest> itemRequests) {
        List<Item> items = itemRepository.findAllByRequestIdIn(
                itemRequests.stream()
                        .map(ItemRequest::getId)
                        .collect(Collectors.toList()));
        List<ItemDtoShort> itemDtoShorts = items.stream().map(ItemMapper::toItemDtoShort).collect(Collectors.toList());

        Map<Integer, List<ItemDtoShort>> itemsList = itemDtoShorts.stream().collect(Collectors.groupingBy(ItemDtoShort::getRequestId, Collectors.toList()));

        return itemRequests.stream()
                .map(itemRequest -> ItemRequestMapper.toItemRequestResponseDto(
                        itemRequest, itemsList.getOrDefault(itemRequest.getId(), Collections.emptyList()))).collect(Collectors.toList());
    }

    public ItemRequestResponseDto getRequestById(Integer userId, Integer requestId) {
        userService.getById(userId);
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));
        List<Item> items = itemRepository.findAllByRequestIdIn(List.of(itemRequest.getId()));
        List<ItemDtoShort> itemDtoShorts = items.stream().map(ItemMapper::toItemDtoShort).collect(Collectors.toList());

        return ItemRequestMapper.toItemRequestResponseDto(itemRequest, itemDtoShorts);
    }
}
