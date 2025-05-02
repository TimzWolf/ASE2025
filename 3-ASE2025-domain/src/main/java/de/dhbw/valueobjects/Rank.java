package de.dhbw.valueobjects;

/**
 * Represents the hierarchical rank of an officer.
 * Used as an immutable value object.
 */
public enum Rank {
    TRAINEE,
    PATROL_OFFICER,
    DETECTIVE,
    INSPECTOR;

    /**
     * Returns the next rank, or the current one if already at the top.
     */
    public Rank next() {
        int ordinal = this.ordinal();
        return ordinal < values().length - 1 ? values()[ordinal + 1] : this;
    }

    /**
     * Returns the previous rank, or the current one if already at the lowest.
     */
    public Rank previous() {
        int ordinal = this.ordinal();
        return ordinal > 0 ? values()[ordinal - 1] : this;
    }
}