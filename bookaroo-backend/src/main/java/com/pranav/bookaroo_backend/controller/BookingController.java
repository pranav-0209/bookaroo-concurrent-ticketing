package com.pranav.bookaroo_backend.controller;

import com.pranav.bookaroo_backend.model.Booking;
import com.pranav.bookaroo_backend.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> bookTickets(
            @RequestParam Long eventId,
            @RequestParam int quantity,
            @RequestParam String userEmail) {

        Booking booking = bookingService.bookTickets(eventId, quantity, userEmail);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Booking>> getBookingsByEvent(@PathVariable Long eventId) {
        List<Booking> bookings = bookingService.getBookingsByEvent(eventId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Booking>> getBookingsByUserEmail(@RequestParam String userEmail) {
        List<Booking> bookings = bookingService.getBookingsByUser(userEmail);
        return ResponseEntity.ok(bookings);
    }
}
