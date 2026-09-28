package com.college.roombooking.Controller;

import com.college.roombooking.Entity.Guest;
import com.college.roombooking.Service.GuestService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    // Add a new guest
    @PostMapping
    public ResponseEntity<Guest> addGuest(
            @Valid @RequestBody Guest guest) {

        Guest savedGuest = guestService.addGuest(guest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedGuest);
    }

    // Get all guests
    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {

        return ResponseEntity.ok(
                guestService.getAllGuests()
        );
    }

    // Get one guest
    @GetMapping("/{id}")
    public ResponseEntity<Guest> getGuestById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                guestService.getGuestById(id)
        );
    }

    // Update guest
    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(
            @PathVariable Long id,
            @Valid @RequestBody Guest guest) {

        return ResponseEntity.ok(
                guestService.updateGuest(id, guest)
        );
    }

    // Delete guest
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(
            @PathVariable Long id) {

        guestService.deleteGuest(id);

        return ResponseEntity.noContent().build();
    }
}