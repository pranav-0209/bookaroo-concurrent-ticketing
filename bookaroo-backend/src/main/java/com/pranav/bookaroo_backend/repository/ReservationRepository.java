package com.pranav.bookaroo_backend.repository;

import com.pranav.bookaroo_backend.model.Reservation;
import com.pranav.bookaroo_backend.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByReservationCode(String reservationCode);

    List<Reservation> findByStatusAndHoldUntilBefore(ReservationStatus status, LocalDateTime time);
}
