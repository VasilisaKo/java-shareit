package ru.practicum.server.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("select b " +
            "from Booking as b " +
            "JOIN b.item AS i " +
            "WHERE b.id = ?1 " +
            "AND i.owner.id = ?2")
    Booking findBookingOwner(Integer bookingId, Integer ownerId);

    @Query("select b " +
            "from Booking as b " +
            "JOIN b.item AS i " +
            "WHERE b.id = ?1 " +
            "AND (i.owner.id = ?2 OR b.booker.id = ?2)")
    Booking findBookingOwnerOrBooker(Integer bookingId, Integer ownerId);

    List<Booking> findAllByBookerIdOrderByStartDesc(Integer bookerId, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Integer bookerId, LocalDateTime time,
                                                                             LocalDateTime time2, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Integer bookerId, LocalDateTime time, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Integer bookerId, LocalDateTime time, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Integer bookerId, Status status, Pageable pageable);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "WHERE i.owner.id = ?1 " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllByOwnerIdOrderByStartDesc(Integer bookerId, Pageable pageable);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "WHERE i.owner.id = ?1 " +
            "AND b.start < ?2 AND b.end > ?2 " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllByOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Integer bookerId, LocalDateTime time,
                                                                            LocalDateTime time2, Pageable pageable);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "WHERE i.owner.id = ?1 " +
            "AND b.start > ?2 " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllByOwnerIdAndStartAfterOrderByStartDesc(Integer bookerId, LocalDateTime time, Pageable pageable);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "WHERE i.owner.id = ?1 " +
            "AND b.end < ?2 " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllByOwnerIdAndEndBeforeOrderByStartDesc(Integer bookerId, LocalDateTime time, Pageable pageable);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "WHERE i.owner.id = ?1 " +
            "AND b.status = ?2 " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllByOwnerIdAndStatusOrderByStartDesc(Integer bookerId, Status status, Pageable pageable);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "WHERE i.id = ?1 " +
            "AND i.owner.id = ?2 " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllByItemIdAndOwnerId(Integer itemId, Integer ownerId);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "WHERE  i.owner.id = ?1 " +
            "AND i.id IN (?2) ")
    List<Booking> findAllByOwnerIdAndItemIn(Integer ownerId, List<Integer> items);

    List<Booking> findAllByBookerIdAndItemIdAndStatusNotAndStartBefore(int userId, int itemId, Status rejected,
                                                                              LocalDateTime now);

}
