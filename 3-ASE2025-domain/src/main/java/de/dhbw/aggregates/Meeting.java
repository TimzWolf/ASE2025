package de.dhbw.aggregates;

import de.dhbw.valueobjects.RoomType;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a meeting with an informant or other personnel.
 */
public class Meeting {
    private final UUID id;
    private final Officer officer;
    private final String informantName;
    private final Room room;
    private final LocalDateTime scheduledAt;

    public Meeting(Officer officer, String informantName, Room room, LocalDateTime scheduledAt) {
        if (room.getType() != RoomType.MEETING) {
            throw new IllegalArgumentException("Room must be of type MEETING.");
        }

        this.id = UUID.randomUUID();
        this.officer = Objects.requireNonNull(officer);
        this.informantName = Objects.requireNonNull(informantName);
        this.room = Objects.requireNonNull(room);
        this.scheduledAt = Objects.requireNonNull(scheduledAt);
    }

    public UUID getId() {
        return id;
    }

    public Officer getOfficer() {
        return officer;
    }

    public String getInformantName() {
        return informantName;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }
}