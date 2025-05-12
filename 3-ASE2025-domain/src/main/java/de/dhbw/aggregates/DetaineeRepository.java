package de.dhbw.aggregates;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for accessing Detainee entities.
 * This abstraction hides the underlying persistence mechanism.
 */
public interface DetaineeRepository {

    /**
     * Saves or updates a detainee.
     * @param detainee The detainee to be persisted.
     */
    void save(Detainee detainee);

    /**
     * Finds a detainee by their unique identifier.
     * @param id UUID of the detainee.
     * @return An optional detainee, if found.
     */
    Optional<Detainee> findById(UUID id);

    /**
     * Find detainees by name (partial match).
     * @param name The name or part of the name to search for.
     * @return List of detainees matching the name criteria.
     */
    List<Detainee> findByNameContaining(String name);

    /**
     * Find detainees by crime type.
     * @param crime The crime to search for.
     * @return List of detainees charged with the specified crime.
     */
    List<Detainee> findByCrime(String crime);

    /**
     * Returns all registered detainees.
     * @return List of detainees.
     */
    List<Detainee> findAll();

    /**
     * Deletes a detainee by ID.
     * @param id UUID of the detainee to delete.
     */
    void deleteById(UUID id);
}