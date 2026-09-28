package com.college.roombooking.Repository;

import com.college.roombooking.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRoomRoomIdAndStatusAndCheckInLessThanAndCheckOutGreaterThan(
            Long roomId,
            String status,
            LocalDate checkOut,
            LocalDate checkIn
    );
}