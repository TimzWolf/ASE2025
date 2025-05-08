package de.dhbw.aggregates;

import de.dhbw.valueobjects.RoomType;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Interrogation {

    private final UUID id;
    private final Officer officer;
    private final Detainee detainee;
    private final Room room;
    private final LocalDateTime scheduledAt;

    public Interrogation(Officer officer, Detainee detainee, Room room, LocalDateTime scheduledAt) {
        if (room.getType() != RoomType.INTERROGATION) {
            throw new IllegalArgumentException("Room must be of type INTERROGATION.");
        }

        this.id = UUID.randomUUID();
        this.officer = Objects.requireNonNull(officer);
        this.detainee = Objects.requireNonNull(detainee);
        this.room = Objects.requireNonNull(room);
        this.scheduledAt = Objects.requireNonNull(scheduledAt);
    }

    public UUID getId() {
        return id;
    }

    public Officer getOfficer() {
        return officer;
    }

    public Detainee getDetainee() {
        return detainee;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }
}

