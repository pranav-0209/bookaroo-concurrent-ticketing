package com.pranav.bookaroo_backend.controller;

import com.pranav.bookaroo_backend.dto.CreateEventRequest;
import com.pranav.bookaroo_backend.model.Event;
import com.pranav.bookaroo_backend.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody CreateEventRequest request) {
        Event event = new Event();
        event.setName(request.getName());
        event.setVenue(request.getVenue());
        event.setTotalTickets(request.getTotalTickets());
        event.setEventDateTime(request.getEventDateTime());

        Event createdEvent = eventService.createEvent(event);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}/availability")
    public ResponseEntity<Integer> getAvailableTickets(@PathVariable Long eventId) {
        int availableTickets = eventService.getAvailableTickets(eventId);
        return ResponseEntity.ok(availableTickets);
    }
}
