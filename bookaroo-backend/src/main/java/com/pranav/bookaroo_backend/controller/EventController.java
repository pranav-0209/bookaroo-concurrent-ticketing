package com.pranav.bookaroo_backend.controller;

import com.pranav.bookaroo_backend.model.Event;
import com.pranav.bookaroo_backend.service.BookingService;
import com.pranav.bookaroo_backend.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService, BookingService bookingService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
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
}
