package de.dhbw.services;

import de.dhbw.aggregates.Room;
import de.dhbw.repositories.RoomRepository;
import de.dhbw.valueobjects.RoomType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Domain service handling operations related to room management.
 */
public class RoomManagementService {
    private final RoomRepository roomRepository;

    public RoomManagementService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Creates a new room in the system.
     *
     * @param type The type of room to create
     * @return The newly created room
     */
    public Room createRoom(RoomType type) {
        Room room = new Room(type);
        roomRepository.save(room);
        return room;
    }

    /**
     * Books a room for use, making it unavailable.
     *
     * @param roomId The ID of the room to book
     * @return The booked room
     * @throws IllegalArgumentException If room not found
     * @throws IllegalStateException If room is already booked
     */
    public Room bookRoom(UUID roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (!room.isAvailable()) {
            throw new IllegalStateException("Room is already booked");
        }

        room.book();
        roomRepository.save(room);
        return room;
    }

    /**
     * Releases a room, making it available for booking.
     *
     * @param roomId The ID of the room to release
     * @return The released room
     * @throws IllegalArgumentException If room not found
     */
    public Room releaseRoom(UUID roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        room.release();
        roomRepository.save(room);
        return room;
    }

    /**
     * Finds all available rooms of a specific type.
     *
     * @param type The type of room to search for
     * @return List of available rooms of the specified type
     */
    public List<Room> findAvailableRoomsByType(RoomType type) {
        return roomRepository.findAvailableByType(type);
    }

    /**
     * Gets the availability status of a specific room.
     *
     * @param roomId The ID of the room to check
     * @return true if the room is available, false otherwise
     * @throws IllegalArgumentException If room not found
     */
    public boolean isRoomAvailable(UUID roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        return room.isAvailable();
    }
    /**
     * Retrieves all rooms registered in the system.
     *
     * @return List of all rooms
     */
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    /**
     * Retrieves all booked (unavailable) rooms in the system.
     *
     * @return List of all booked rooms
     */
    public List<Room> getAllBookedRooms() {
        // Filter all rooms to find only those that are not available
        return roomRepository.findAll().stream()
                .filter(room -> !room.isAvailable())
                .collect(java.util.stream.Collectors.toList());
    }
}