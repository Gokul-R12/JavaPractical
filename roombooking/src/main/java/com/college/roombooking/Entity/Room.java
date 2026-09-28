package com.college.roombooking.Entity;
//this is for the JPA hibernate jakarta.
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @NotBlank(message = "Room number needed")
    @Column(nullable = false, unique = true)
    private String roomNumber;

    @NotBlank(message = "Room type needed")
    @Column(nullable = false)
    private String type;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be greater than zero")
    @Column(nullable = false)
    private Integer capacity;

    @NotNull(message = "Rate per night is required")
    @Positive(message = "Rate per night must be greater than zero")
    @Column(nullable = false)
    private Double ratePerNight;
    @JsonIgnore
    @OneToMany(mappedBy = "room")
    private List<Booking> bookings;

    public Room() {
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Double getRatePerNight() {
        return ratePerNight;
    }

    public void setRatePerNight(Double ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}