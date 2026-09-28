package com.college.roombooking.Controller;

import com.college.roombooking.Entity.Booking;
import com.college.roombooking.Entity.Room;
import com.college.roombooking.Service.BookingService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Create a booking
    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestParam Long roomId,
            @RequestParam Long guestId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkIn,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkOut) {

        Booking booking = bookingService.createBooking(
                roomId,
                guestId,
                checkIn,
                checkOut
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(booking);
    }

    // Get all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {

        return ResponseEntity.ok(
                bookingService.getAllBookings()
        );
    }

    // Get booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.getBookingById(id)
        );
    }

    // Cancel a booking
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                bookingService.cancelBooking(id)
        );
    }

    // Check room availability
    @GetMapping("/availability")
    public ResponseEntity<List<Room>> getAvailableRooms(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkIn,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate checkOut) {

        return ResponseEntity.ok(
                bookingService.getAvailableRooms(
                        checkIn,
                        checkOut
                )
        );
    }
}