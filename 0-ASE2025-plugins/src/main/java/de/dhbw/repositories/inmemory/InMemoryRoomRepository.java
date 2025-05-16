package de.dhbw.repositories.inmemory;

import de.dhbw.aggregates.Room;
import de.dhbw.repositories.RoomRepository;
import de.dhbw.valueobjects.RoomType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the RoomRepository interface.
 * This is used for temporary storage in the CLI application.
 */
public class InMemoryRoomRepository implements RoomRepository {
    private final Map<UUID, Room> rooms = new HashMap<>();

    @Override
    public void save(Room room) {
        rooms.put(room.getId(), room);
    }

    @Override
    public Optional<Room> findById(UUID id) {
        return Optional.ofNullable(rooms.get(id));
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }

    @Override
    public List<Room> findByType(RoomType type) {
        return rooms.values().stream()
                .filter(room -> room.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findAvailable() {
        return rooms.values().stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findAvailableByType(RoomType type) {
        return rooms.values().stream()
                .filter(room -> room.getType() == type && room.isAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        rooms.remove(id);
    }
}