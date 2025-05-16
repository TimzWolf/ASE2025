package de.dhbw.repositories.inmemory;

import de.dhbw.aggregates.Meeting;
import de.dhbw.repositories.MeetingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the MeetingRepository interface.
 * This is used for temporary storage in the CLI application.
 */
public class InMemoryMeetingRepository implements MeetingRepository {
    private final Map<UUID, Meeting> meetings = new HashMap<>();

    @Override
    public void save(Meeting meeting) {
        meetings.put(meeting.getId(), meeting);
    }

    @Override
    public Optional<Meeting> findById(UUID id) {
        return Optional.ofNullable(meetings.get(id));
    }

    @Override
    public List<Meeting> findAll() {
        return new ArrayList<>(meetings.values());
    }

    @Override
    public List<Meeting> findByOfficerId(UUID officerId) {
        return meetings.values().stream()
                .filter(meeting -> meeting.getOfficer().getId().equals(officerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meeting> findByRoomId(UUID roomId) {
        return meetings.values().stream()
                .filter(meeting -> meeting.getRoom().getId().equals(roomId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meeting> findByDate(LocalDate date) {
        return meetings.values().stream()
                .filter(meeting -> meeting.getScheduledAt().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meeting> findUpcoming() {
        LocalDateTime now = LocalDateTime.now();
        return meetings.values().stream()
                .filter(meeting -> meeting.getScheduledAt().isAfter(now))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        meetings.remove(id);
    }
}