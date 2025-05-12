package de.dhbw.aggregates;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for accessing Interrogation entities.
 * This abstraction hides the underlying persistence mechanism.
 */
public interface InterrogationRepository {

    /**
     * Saves or updates an interrogation.
     * @param interrogation The interrogation to be persisted.
     */
    void save(Interrogation interrogation);

    /**
     * Finds an interrogation by its unique identifier.
     * @param id UUID of the interrogation.
     * @return An optional interrogation, if found.
     */
    Optional<Interrogation> findById(UUID id);

    /**
     * Returns all scheduled interrogations.
     * @return List of interrogations.
     */
    List<Interrogation> findAll();

    /**
     * Finds all interrogations conducted by a specific officer.
     * @param officerId UUID of the officer.
     * @return List of interrogations conducted by the specified officer.
     */
    List<Interrogation> findByOfficerId(UUID officerId);

    /**
     * Finds all interrogations of a specific detainee.
     * @param detaineeId UUID of the detainee.
     * @return List of interrogations of the specified detainee.
     */
    List<Interrogation> findByDetaineeId(UUID detaineeId);

    /**
     * Finds all interrogations scheduled in a specific room.
     * @param roomId UUID of the room.
     * @return List of interrogations scheduled in the specified room.
     */
    List<Interrogation> findByRoomId(UUID roomId);

    /**
     * Finds all interrogations scheduled on a specific date.
     * @param date The date to search for.
     * @return List of interrogations scheduled on the specified date.
     */
    List<Interrogation> findByDate(LocalDate date);

    /**
     * Finds all upcoming interrogations (scheduled after current time).
     * @return List of upcoming interrogations.
     */
    List<Interrogation> findUpcoming();

    /**
     * Finds all interrogations scheduled within a time range.
     * @param start Start of the time range.
     * @param end End of the time range.
     * @return List of interrogations within the specified time range.
     */
    List<Interrogation> findByTimeRange(LocalDateTime start, LocalDateTime end);

    /**
     * Deletes an interrogation by ID.
     * @param id UUID of the interrogation to delete.
     */
    void deleteById(UUID id);
}