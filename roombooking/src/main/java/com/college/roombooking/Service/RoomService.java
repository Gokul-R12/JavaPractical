package com.college.roombooking.Service;

import com.college.roombooking.Entity.Room;
import com.college.roombooking.Exception.ResourceNotFoundException;
import com.college.roombooking.Repository.RoomRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // Add a new room
    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }

    // Get all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Get one room by ID
    public Room getRoomById(Long id) {

        return roomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Room not found with ID: " + id)
                );
    }

    // Update an existing room
    public Room updateRoom(Long id, Room updatedRoom) {

        Room existingRoom = getRoomById(id);

        existingRoom.setRoomNumber(updatedRoom.getRoomNumber());
        existingRoom.setType(updatedRoom.getType());
        existingRoom.setCapacity(updatedRoom.getCapacity());
        existingRoom.setRatePerNight(updatedRoom.getRatePerNight());

        return roomRepository.save(existingRoom);
    }

    // Delete a room
    public void deleteRoom(Long id) {

        Room room = getRoomById(id);

        roomRepository.delete(room);
    }
}