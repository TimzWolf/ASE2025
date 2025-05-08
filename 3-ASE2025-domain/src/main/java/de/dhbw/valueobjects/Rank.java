package de.dhbw.valueobjects;

import java.util.Objects;

/**
 * Represents the hierarchical rank of an officer.
 * Used as an immutable value object.
 */
public final class Rank implements Comparable<Rank> {

    private final String name;
    private final int level; // e.g. Officer = 1, Inspector = 2, Chief = 3

    public Rank(String name, int level) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Rank name must not be empty.");
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public boolean isHigherThan(Rank other) {
        return this.level > other.level;
    }

    @Override
    public int compareTo(Rank other) {
        return Integer.compare(this.level, other.level);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rank)) return false;
        Rank rank = (Rank) o;
        return level == rank.level && name.equals(rank.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, level);
    }

    @Override
    public String toString() {
        return name;
    }
}
