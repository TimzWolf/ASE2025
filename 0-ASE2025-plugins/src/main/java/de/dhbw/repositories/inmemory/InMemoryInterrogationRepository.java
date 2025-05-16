package de.dhbw.repositories.inmemory;

import de.dhbw.aggregates.Interrogation;
import de.dhbw.repositories.InterrogationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the InterrogationRepository interface.
 * This is used for temporary storage in the CLI application.
 */
public class InMemoryInterrogationRepository implements InterrogationRepository {
    private final Map<UUID, Interrogation> interrogations = new HashMap<>();

    @Override
    public void save(Interrogation interrogation) {
        interrogations.put(interrogation.getId(), interrogation);
    }

    @Override
    public Optional<Interrogation> findById(UUID id) {
        return Optional.ofNullable(interrogations.get(id));
    }

    @Override
    public List<Interrogation> findAll() {
        return new ArrayList<>(interrogations.values());
    }

    @Override
    public List<Interrogation> findByOfficerId(UUID officerId) {
        return interrogations.values().stream()
                .filter(interrogation -> interrogation.getOfficer().getId().equals(officerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Interrogation> findByDetaineeId(UUID detaineeId) {
        return interrogations.values().stream()
                .filter(interrogation -> interrogation.getDetainee().getId().equals(detaineeId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Interrogation> findByRoomId(UUID roomId) {
        return interrogations.values().stream()
                .filter(interrogation -> interrogation.getRoom().getId().equals(roomId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Interrogation> findByDate(LocalDate date) {
        return interrogations.values().stream()
                .filter(interrogation -> interrogation.getScheduledAt().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Interrogation> findUpcoming() {
        LocalDateTime now = LocalDateTime.now();
        return interrogations.values().stream()
                .filter(interrogation -> interrogation.getScheduledAt().isAfter(now))
                .collect(Collectors.toList());
    }

    @Override
    public List<Interrogation> findByTimeRange(LocalDateTime start, LocalDateTime end) {
        return interrogations.values().stream()
                .filter(interrogation -> {
                    LocalDateTime scheduledTime = interrogation.getScheduledAt();
                    return scheduledTime.isAfter(start) && scheduledTime.isBefore(end);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        interrogations.remove(id);
    }
}