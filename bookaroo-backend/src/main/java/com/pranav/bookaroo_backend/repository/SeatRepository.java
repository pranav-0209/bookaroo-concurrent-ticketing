package com.pranav.bookaroo_backend.repository;

import com.pranav.bookaroo_backend.model.Seat;
import com.pranav.bookaroo_backend.model.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByEventId(Long eventId);

    List<Seat> findByEventIdAndSeatNumberIn(Long eventId, List<String> seats);

    List<Seat> findByStatusAndHoldUntilBefore(SeatStatus status, LocalDateTime time);
}
