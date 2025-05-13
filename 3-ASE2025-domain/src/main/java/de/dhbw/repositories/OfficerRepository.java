package de.dhbw.repositories;

import de.dhbw.aggregates.Officer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for accessing Officer entities.
 * This abstraction hides the underlying persistence mechanism.
 */
public interface OfficerRepository {

    /**
     * Saves or updates an officer.
     * @param officer The officer to be persisted.
     */
    void save(Officer officer);

    /**
     * Finds an officer by their unique identifier.
     * @param id UUID of the officer.
     * @return An optional officer, if found.
     */
    Optional<Officer> findById(UUID id);

    /**
     * Returns all registered officers.
     * @return List of officers.
     */
    List<Officer> findAll();

    /**
     * Deletes an officer by ID.
     * @param id UUID of the officer to delete.
     */
    void deleteById(UUID id);
}

