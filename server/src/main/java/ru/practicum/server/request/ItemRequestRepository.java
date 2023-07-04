package ru.practicum.server.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {
    List<ItemRequest> findAllByRequestorOrderByCreatedDesc(Integer userId);

    List<ItemRequest> findAllByRequestorIsNotOrderByCreatedDesc(Integer requestor, Pageable page);
}
