package com.college.roombooking.Service;

import com.college.roombooking.Entity.Guest;
import com.college.roombooking.Exception.ResourceNotFoundException;
import com.college.roombooking.Repository.GuestRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    // Add a new guest
    public Guest addGuest(Guest guest) {
        return guestRepository.save(guest);
    }

    // Get all guests
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    // Get one guest by ID
    public Guest getGuestById(Long id) {

        return guestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Guest not found with ID: " + id
                        )
                );
    }

    // Update an existing guest
    public Guest updateGuest(Long id, Guest updatedGuest) {

        Guest existingGuest = getGuestById(id);

        existingGuest.setName(updatedGuest.getName());
        existingGuest.setPhone(updatedGuest.getPhone());
        existingGuest.setEmail(updatedGuest.getEmail());

        return guestRepository.save(existingGuest);
    }

    // Delete a guest
    public void deleteGuest(Long id) {

        Guest guest = getGuestById(id);

        guestRepository.delete(guest);
    }
}