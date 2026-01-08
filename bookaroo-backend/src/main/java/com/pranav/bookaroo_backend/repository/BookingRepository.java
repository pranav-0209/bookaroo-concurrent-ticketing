package com.pranav.bookaroo_backend.repository;

import com.pranav.bookaroo_backend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByEventId(Long eventId);

    List<Booking> findByUserEmail(String email);

}
