package de.dhbw.aggregates;

import de.dhbw.valueobjects.RoomType;

import java.util.Objects;
import java.util.UUID;

public class Room {

    private final UUID id;
    private final RoomType type;
    private boolean available = true;

    public Room(RoomType type) {
        this.id = UUID.randomUUID();
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public RoomType getType() {
        return type;
    }

    public boolean isAvailable() {
        return available;
    }

    public void book() {
        if (!available) throw new IllegalStateException("Room is already booked.");
        this.available = false;
    }

    public void release() {
        this.available = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return id.equals(room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

