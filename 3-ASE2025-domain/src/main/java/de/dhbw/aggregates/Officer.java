package de.dhbw.aggregates;

import de.dhbw.valueobjects.Rank;

import java.util.Objects;
import java.util.UUID;

public class Officer {

    private final UUID id;
    private final String name;
    private Rank rank;

    public Officer(String name, Rank rank) {
        this.id = UUID.randomUUID();
        this.name = Objects.requireNonNull(name);
        this.rank = Objects.requireNonNull(rank);
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

    public void promoteTo(Rank newRank) {
        if (!newRank.isHigherThan(this.rank)) {
            throw new IllegalArgumentException("New rank must be higher.");
        }
        this.rank = newRank;
    }

    public void demoteTo(Rank newRank) {
        if (newRank.isHigherThan(this.rank)) {
            throw new IllegalArgumentException("New rank must be lower.");
        }
        this.rank = newRank;
    }
}
