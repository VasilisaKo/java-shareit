package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/*public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByOwnerOrderById(User user);

    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(
            String name, String description, Boolean available);
    List<Item> findAllByRequestId(List<Integer> requestsId);
}*/
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByOwnerOrderById(User user);

    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(
            String name, String description, Boolean available);

    List<Item> findAllByRequestIdIn(List<Integer> requestsId);
}