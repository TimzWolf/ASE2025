package de.dhbw.aggregates;

import de.dhbw.valueobjects.RoomType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for accessing Room entities.
 * This abstraction hides the underlying persistence mechanism.
 */
public interface RoomRepository {

    /**
     * Saves or updates a room.
     * @param room The room to be persisted.
     */
    void save(Room room);

    /**
     * Finds a room by its unique identifier.
     * @param id UUID of the room.
     * @return An optional room, if found.
     */
    Optional<Room> findById(UUID id);

    /**
     * Returns all registered rooms.
     * @return List of rooms.
     */
    List<Room> findAll();

    /**
     * Finds all rooms of a specific type.
     * @param type The room type to search for.
     * @return List of rooms matching the given type.
     */
    List<Room> findByType(RoomType type);

    /**
     * Finds all available rooms.
     * @return List of available rooms.
     */
    List<Room> findAvailable();

    /**
     * Finds all available rooms of a specific type.
     * @param type The room type to search for.
     * @return List of available rooms matching the given type.
     */
    List<Room> findAvailableByType(RoomType type);

    /**
     * Deletes a room by ID.
     * @param id UUID of the room to delete.
     */
    void deleteById(UUID id);
}
