package de.dhbw.aggregates;

import java.util.Objects;
import java.util.UUID;

public class Detainee {

    private final UUID id;
    private final String name;
    private final String crime;

    public Detainee(String name, String crime) {
        this.id = UUID.randomUUID();
        this.name = Objects.requireNonNull(name);
        this.crime = Objects.requireNonNull(crime);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCrime() {
        return crime;
    }
}

