package de.dhbw.repositories.inmemory;

import de.dhbw.aggregates.Officer;
import de.dhbw.repositories.OfficerRepository;

import java.util.*;

/**
 * In-memory implementation of the OfficerRepository interface.
 * This is used for temporary storage in the CLI application.
 */
public class InMemoryOfficerRepository implements OfficerRepository {
    private final Map<UUID, Officer> officers = new HashMap<>();

    @Override
    public void save(Officer officer) {
        officers.put(officer.getId(), officer);
    }

    @Override
    public Optional<Officer> findById(UUID id) {
        return Optional.ofNullable(officers.get(id));
    }

    @Override
    public List<Officer> findAll() {
        return new ArrayList<>(officers.values());
    }

    @Override
    public void deleteById(UUID id) {
        officers.remove(id);
    }
}