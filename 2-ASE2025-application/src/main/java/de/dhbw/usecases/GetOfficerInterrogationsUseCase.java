package de.dhbw.usecases;

import de.dhbw.aggregates.Interrogation;
import de.dhbw.repositories.InterrogationRepository;
import de.dhbw.repositories.OfficerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Use case for retrieving all interrogations scheduled for a specific officer.
 */
public class GetOfficerInterrogationsUseCase {
    private final InterrogationRepository interrogationRepository;
    private final OfficerRepository officerRepository;

    public GetOfficerInterrogationsUseCase(
            InterrogationRepository interrogationRepository,
            OfficerRepository officerRepository) {
        this.interrogationRepository = interrogationRepository;
        this.officerRepository = officerRepository;
    }

    /**
     * Execute the use case to get all interrogations for an officer.
     *
     * @param officerId The ID of the officer
     * @return List of interrogations for the officer
     * @throws IllegalArgumentException If officer not found
     */
    public List<Interrogation> execute(UUID officerId) {
        // Verify that the officer exists
        if (!officerRepository.findById(officerId).isPresent()) {
            throw new IllegalArgumentException("Officer not found");
        }

        // Get all interrogations for the officer
        return interrogationRepository.findByOfficerId(officerId);
    }

    /**
     * Execute the use case to get upcoming interrogations for an officer.
     *
     * @param officerId The ID of the officer
     * @return List of upcoming interrogations for the officer
     * @throws IllegalArgumentException If officer not found
     */
    public List<Interrogation> getUpcoming(UUID officerId) {
        // Verify that the officer exists
        if (!officerRepository.findById(officerId).isPresent()) {
            throw new IllegalArgumentException("Officer not found");
        }

        // Get all interrogations for the officer and filter for upcoming ones
        LocalDateTime now = LocalDateTime.now();
        return interrogationRepository.findByOfficerId(officerId).stream()
                .filter(interrogation -> interrogation.getScheduledAt().isAfter(now))
                .collect(Collectors.toList());
    }
}