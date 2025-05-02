package de.dhbw.aggregates;

import de.dhbw.valueobjects.Rank;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a police officer within the domain.
 * Officers are identified by a UUID and have a name and a rank.
 */
public class Officer {

    private final UUID id;
    private final String name;
    private Rank rank;

    public Officer(UUID id, String name, Rank rank) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or empty.");
        }
        this.id = Objects.requireNonNull(id, "ID must not be null.");
        this.name = name;
        this.rank = Objects.requireNonNull(rank, "Rank must not be null.");
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Rank getRank() {
        return rank;
    }

    /**
     * Promotes the officer to the next rank, if available.
     */
    public void promote() {
        this.rank = this.rank.next();
    }

    /**
     * Demotes the officer to the previous rank, if available.
     */
    public void demote() {
        this.rank = this.rank.previous();
    }
}