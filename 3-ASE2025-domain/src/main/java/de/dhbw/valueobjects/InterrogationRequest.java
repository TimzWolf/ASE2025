package de.dhbw.valueobjects;

import de.dhbw.aggregates.Detainee;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable value object representing parameters for scheduling an interrogation.
 */
public final class InterrogationRequest {
    private final UUID officerId;
    private final Detainee detainee;
    private final LocalDateTime scheduledTime;

    public InterrogationRequest(UUID officerId, Detainee detainee, LocalDateTime scheduledTime) {
        this.officerId = Objects.requireNonNull(officerId, "Officer ID must not be null");
        this.detainee = Objects.requireNonNull(detainee, "Detainee must not be null");
        this.scheduledTime = Objects.requireNonNull(scheduledTime, "Scheduled time must not be null");
    }

    public UUID getOfficerId() {
        return officerId;
    }

    public Detainee getDetainee() {
        return detainee;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }
}