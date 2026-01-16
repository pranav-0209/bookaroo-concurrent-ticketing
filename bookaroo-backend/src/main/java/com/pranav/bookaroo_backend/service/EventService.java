package com.pranav.bookaroo_backend.service;

import com.pranav.bookaroo_backend.model.Event;
import com.pranav.bookaroo_backend.model.TicketInventory;
import com.pranav.bookaroo_backend.repository.EventRepository;
import com.pranav.bookaroo_backend.repository.TicketInventoryRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final TicketInventoryRepository ticketInventoryRepository;

    public EventService(EventRepository eventRepository, TicketInventoryRepository ticketInventoryRepository) {
        this.eventRepository = eventRepository;
        this.ticketInventoryRepository = ticketInventoryRepository;
    }

    @Transactional
    public Event createEvent(Event e) {
        Event SavedEvent = eventRepository.save(e);
        log.info("Event created: name={}, totalTickets={}", SavedEvent.getName(), SavedEvent.getTotalTickets());

        TicketInventory ticketInventory = new TicketInventory();
        ticketInventory.setEvent(SavedEvent);
        ticketInventory.setAvailableTickets(SavedEvent.getTotalTickets());

        ticketInventoryRepository.save(ticketInventory);
        log.info("Inventory initialized for eventId={} with {} tickets",
                SavedEvent.getId(), SavedEvent.getTotalTickets());

        return SavedEvent;
    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public int getAvailableTickets(Long eventId) {
        TicketInventory inventory = ticketInventoryRepository.findByEventId(eventId).
                orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
        return inventory.getAvailableTickets();
    }

}
