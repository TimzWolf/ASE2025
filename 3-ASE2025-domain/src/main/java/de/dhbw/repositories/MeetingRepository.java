package de.dhbw.repositories;

import de.dhbw.aggregates.Meeting;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for accessing Meeting entities.
 */
public interface MeetingRepository {
    /**
     * Saves or updates a meeting.
     * @param meeting The meeting to be persisted.
     */
    void save(Meeting meeting);

    /**
     * Finds a meeting by its unique identifier.
     * @param id UUID of the meeting.
     * @return An optional meeting, if found.
     */
    Optional<Meeting> findById(UUID id);

    /**
     * Returns all scheduled meetings.
     * @return List of meetings.
     */
    List<Meeting> findAll();

    /**
     * Finds all meetings conducted by a specific officer.
     * @param officerId UUID of the officer.
     * @return List of meetings conducted by the specified officer.
     */
    List<Meeting> findByOfficerId(UUID officerId);

    /**
     * Finds all meetings scheduled in a specific room.
     * @param roomId UUID of the room.
     * @return List of meetings scheduled in the specified room.
     */
    List<Meeting> findByRoomId(UUID roomId);

    /**
     * Finds all meetings scheduled on a specific date.
     * @param date The date to search for.
     * @return List of meetings scheduled on the specified date.
     */
    List<Meeting> findByDate(LocalDate date);

    /**
     * Finds all upcoming meetings (scheduled after current time).
     * @return List of upcoming meetings.
     */
    List<Meeting> findUpcoming();

    /**
     * Deletes a meeting by ID.
     * @param id UUID of the meeting to delete.
     */
    void deleteById(UUID id);
}