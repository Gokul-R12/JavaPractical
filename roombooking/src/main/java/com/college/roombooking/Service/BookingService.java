package com.college.roombooking.Service;

import com.college.roombooking.Entity.Booking;
import com.college.roombooking.Entity.Guest;
import com.college.roombooking.Entity.Room;
import com.college.roombooking.Exception.BookingConflictException;
import com.college.roombooking.Exception.ResourceNotFoundException;
import com.college.roombooking.Repository.BookingRepository;
import com.college.roombooking.Repository.GuestRepository;
import com.college.roombooking.Repository.RoomRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
//import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;

    public BookingService(
            BookingRepository bookingRepository,
            RoomRepository roomRepository,
            GuestRepository guestRepository) {

        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
    }

    // Create a new booking
    public Booking createBooking(
            Long roomId,
            Long guestId,
            LocalDate checkIn,
            LocalDate checkOut) {

        // 1. Validate the dates
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException(
                    "Check-out date must be after check-in date"
            );
        }

        // 2. Find the room
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room not found with ID: " + roomId
                        )
                );

        // 3. Find the guest
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Guest not found with ID: " + guestId
                        )
                );

        // 4. Check whether the room is already booked
        List<Booking> conflictingBookings =
                bookingRepository
                        .findByRoomRoomIdAndStatusAndCheckInLessThanAndCheckOutGreaterThan(
                                roomId,
                                "CONFIRMED",
                                checkOut,
                                checkIn
                        );

        // 5. Reject the booking if there is a conflict
        if (!conflictingBookings.isEmpty()) {
            throw new BookingConflictException(
                    "Room " + room.getRoomNumber()
                            + " is already booked for the selected dates"
            );
        }

        // 6. Calculate the number of nights
        long numberOfNights =
                ChronoUnit.DAYS.between(checkIn, checkOut);

        // 7. Calculate total cost
        double totalCost =
                numberOfNights * room.getRatePerNight();

        // 8. Create the booking
        Booking booking = new Booking();

        booking.setRoom(room);
        booking.setGuest(guest);
        booking.setCheckIn(checkIn);
        booking.setCheckOut(checkOut);
        booking.setTotalCost(totalCost);
        booking.setStatus("CONFIRMED");

        // 9. Save the booking
        return bookingRepository.save(booking);
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Get one booking
    public Booking getBookingById(Long id) {

        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with ID: " + id
                        )
                );
    }

    // Cancel a booking
    public Booking cancelBooking(Long id) {

        Booking booking = getBookingById(id);

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new IllegalArgumentException(
                    "Booking is already cancelled"
            );
        }

        booking.setStatus("CANCELLED");

        return bookingRepository.save(booking);
    }

    // Check available rooms
    public List<Room> getAvailableRooms(
            LocalDate checkIn,
            LocalDate checkOut) {

        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException(
                    "Check-out date must be after check-in date"
            );
        }

        List<Room> allRooms = roomRepository.findAll();

        return allRooms.stream()
                .filter(room -> {

                    List<Booking> conflicts =
                            bookingRepository
                                    .findByRoomRoomIdAndStatusAndCheckInLessThanAndCheckOutGreaterThan(
                                            room.getRoomId(),
                                            "CONFIRMED",
                                            checkOut,
                                            checkIn
                                    );

                    return conflicts.isEmpty();
                })
                .toList();
    }
}