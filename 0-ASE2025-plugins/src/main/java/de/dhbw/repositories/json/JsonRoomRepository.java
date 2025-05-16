package de.dhbw.repositories.json;

import de.dhbw.aggregates.Room;
import de.dhbw.repositories.RoomRepository;
import de.dhbw.valueobjects.RoomType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * JSON implementation of the RoomRepository interface.
 * Stores rooms in a JSON file.
 */
public class JsonRoomRepository extends JsonPersistenceBase<Room> implements RoomRepository {
    private Map<UUID, Room> rooms = new HashMap<>();

    public JsonRoomRepository() {
        super("rooms", Room.class);
        loadRooms();
    }

    /**
     * Loads rooms from the JSON file.
     */
    private void loadRooms() {
        List<Room> roomList = loadFromFile();
        rooms.clear();
        for (Room room : roomList) {
            rooms.put(room.getId(), room);
        }
    }

    /**
     * Saves rooms to the JSON file.
     */
    private void saveRooms() {
        saveToFile(new ArrayList<>(rooms.values()));
    }

    @Override
    public void save(Room room) {
        rooms.put(room.getId(), room);
        saveRooms();
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
        saveRooms();
    }
}