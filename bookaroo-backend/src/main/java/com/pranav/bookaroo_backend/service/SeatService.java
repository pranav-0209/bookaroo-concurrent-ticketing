package com.pranav.bookaroo_backend.service;

import com.pranav.bookaroo_backend.model.Seat;
import com.pranav.bookaroo_backend.model.SeatStatus;
import com.pranav.bookaroo_backend.repository.SeatRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SeatService {

    private static final Logger log = LoggerFactory.getLogger(SeatService.class);

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getSeatsForEvent(Long eventId) {
        return seatRepository.findByEventId(eventId);
    }

    @Transactional
    public List<Seat> holdSeats(Long eventId, List<String> seatNumbers) {

        int maxAttempts = 3;
        int attempt = 0;

        while (attempt < maxAttempts) {
            try {
                attempt++;
                log.info("Hold attempt {} for eventId={}, seats={}", attempt, eventId, seatNumbers);

                List<Seat> seats = seatRepository.findByEventIdAndSeatNumberIn(eventId, seatNumbers);

                if (seats.size() != seatNumbers.size()) {
                    throw new IllegalArgumentException("Some seats does not exist");
                }

                for (Seat seat : seats) {
                    if (seat.getStatus() != SeatStatus.AVAILABLE) {
                        log.warn("Seat not available: {}", seat.getSeatNumber());
                        throw new IllegalStateException("One or more seats are not available");
                    }
                }

                LocalDateTime holdUntil = LocalDateTime.now().plusMinutes(5);

                for (Seat seat : seats) {
                    seat.setStatus(SeatStatus.HELD);
                    seat.setHoldUntil(holdUntil);
                }

                List<Seat> saved = seatRepository.saveAll(seats);
                log.info("Seats held successfully: {}", seatNumbers);
                return saved;
            } catch (OptimisticLockingFailureException ex) {
                log.warn("Version conflict while holding seats on attempt {}", attempt);
                if (attempt == maxAttempts) {
                    throw new IllegalStateException("High demand. Please try again.");
                }
            }
        }

        throw new IllegalStateException("Seat hold failed unexpectedly");
    }

}
