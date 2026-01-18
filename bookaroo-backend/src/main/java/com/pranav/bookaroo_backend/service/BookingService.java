package com.pranav.bookaroo_backend.service;

import com.pranav.bookaroo_backend.model.Booking;
import com.pranav.bookaroo_backend.model.BookingStatus;
import com.pranav.bookaroo_backend.model.Event;
import com.pranav.bookaroo_backend.model.TicketInventory;
import com.pranav.bookaroo_backend.repository.BookingRepository;
import com.pranav.bookaroo_backend.repository.EventRepository;
import com.pranav.bookaroo_backend.repository.TicketInventoryRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final TicketInventoryRepository ticketInventoryRepository;

    public BookingService(BookingRepository bookingRepository, EventRepository eventRepository, TicketInventoryRepository ticketInventoryRepository) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
        this.ticketInventoryRepository = ticketInventoryRepository;
    }

    @Transactional
    public Booking bookTickets(Long eventId, int quantity, String userEmail) {

        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                attempt++;
                log.info("Booking attempt: eventId={}, qty={}, user={}", eventId, quantity, userEmail);

                Event event = eventRepository.findById(eventId)
                        .orElseThrow(() -> new IllegalArgumentException("Event not found"));

                TicketInventory inventory = ticketInventoryRepository.findByEventId(eventId)
                        .orElseThrow(() -> new IllegalStateException("Inventory not initialized"));
                log.debug("Inventory before update: available={}", inventory.getAvailableTickets());

                if (inventory.getAvailableTickets() < quantity) {
                    log.warn("Booking Failed (Tickets Not Available): eventId={}, requested={}", eventId, quantity);
                    throw new IllegalStateException("Not enough tickets available");
                }

                log.debug("Updating inventory: eventId={}, from {} to {}",
                        eventId,
                        inventory.getAvailableTickets(),
                        inventory.getAvailableTickets() - quantity);
                inventory.setAvailableTickets(inventory.getAvailableTickets() - quantity);

                ticketInventoryRepository.save(inventory);
                Booking booking = new Booking();
                booking.setEvent(event);
                booking.setUserEmail(userEmail);
                booking.setQuantity(quantity);
                booking.setStatus(BookingStatus.CONFIRMED);
                booking.setCreatedAt(LocalDateTime.now());
                Booking savedBooking = bookingRepository.save(booking);
                log.info("Booking Success: eventId={}, user={}, quantity={}", eventId, userEmail, quantity);
                return savedBooking;
            } catch (OptimisticLockingFailureException ex) {
                log.warn("Version conflict on attempt {} for eventId={}", attempt, eventId);

                if (attempt == maxRetries) {
                    throw new IllegalStateException("High Demand. Please try again in a moment");
                }
            }
        }
        throw new IllegalStateException("Booking failed after retries");
    }

    public List<Booking> getBookingsByEvent(Long eventId) {
        return bookingRepository.findByEventId(eventId);
    }

    public List<Booking> getBookingsByUser(String userEmail) {
        return bookingRepository.findByUserEmail(userEmail);
    }
}
