package com.college.roombooking.Controller;

import com.college.roombooking.Entity.Room;
import com.college.roombooking.Service.RoomService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Add a new room
    @PostMapping
    public ResponseEntity<Room> addRoom(
            @Valid @RequestBody Room room) {

        Room savedRoom = roomService.addRoom(room);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedRoom);
    }

    // Get all rooms
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {

        return ResponseEntity.ok(
                roomService.getAllRooms()
        );
    }

    // Get one room
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                roomService.getRoomById(id)
        );
    }

    // Update a room
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody Room room) {

        return ResponseEntity.ok(
                roomService.updateRoom(id, room)
        );
    }

    // Delete a room
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long id) {

        roomService.deleteRoom(id);

        return ResponseEntity.noContent().build();
    }
}