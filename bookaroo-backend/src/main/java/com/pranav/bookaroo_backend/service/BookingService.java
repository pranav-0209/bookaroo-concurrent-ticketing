package com.pranav.bookaroo_backend.service;

import com.pranav.bookaroo_backend.model.Booking;
import com.pranav.bookaroo_backend.model.BookingStatus;
import com.pranav.bookaroo_backend.model.Event;
import com.pranav.bookaroo_backend.model.TicketInventory;
import com.pranav.bookaroo_backend.repository.BookingRepository;
import com.pranav.bookaroo_backend.repository.EventRepository;
import com.pranav.bookaroo_backend.repository.TicketInventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

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

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));

        try {
            TicketInventory inventory = ticketInventoryRepository.findById(eventId)
                    .orElseThrow(() -> new IllegalStateException("Inventory not initialized"));

            if (inventory.getAvailableTickets() < quantity) {
                throw new IllegalStateException("Not enough tickets available");
            }

            inventory.setAvailableTickets(inventory.getAvailableTickets() - quantity);

            ticketInventoryRepository.save(inventory);

            Booking booking = new Booking();
            booking.setEvent(event);
            booking.setUserEmail(userEmail);
            booking.setQuantity(quantity);
            booking.setStatue(BookingStatus.CONFIRMED);
            booking.setCreatedAt(LocalDateTime.now());
            return bookingRepository.save(booking);
        } catch (OptimisticLockingFailureException ex) {
            throw new IllegalStateException("Tickets are no longer available. Please retry.");
        }
    }

    public List<Booking> getBookingsByEvent(Long eventId) {
        return bookingRepository.findByEventId(eventId);
    }

    public List<Booking> getBookingsByUser(String userEmail) {
        return bookingRepository.findByUserEmail(userEmail);
    }
}
