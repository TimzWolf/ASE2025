package de.dhbw.services;

import de.dhbw.aggregates.Officer;
import de.dhbw.repositories.OfficerRepository;
import de.dhbw.valueobjects.Rank;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Domain service handling operations related to officers.
 */
public class OfficerService {
    private final OfficerRepository officerRepository;

    public OfficerService(OfficerRepository officerRepository) {
        this.officerRepository = officerRepository;
    }

    /**
     * Registers a new officer in the system.
     *
     * @param name The name of the officer
     * @param rank The initial rank of the officer
     * @return The newly created officer
     */
    public Officer registerOfficer(String name, Rank rank) {
        Officer officer = new Officer(name, rank);
        officerRepository.save(officer);
        return officer;
    }

    /**
     * Promotes an officer to a higher rank.
     *
     * @param officerId The ID of the officer to promote
     * @param newRank The new rank for the officer
     * @return The updated officer
     * @throws IllegalArgumentException If officer not found or promotion invalid
     */
    public Officer promoteOfficer(UUID officerId, Rank newRank) {
        Officer officer = officerRepository.findById(officerId)
                .orElseThrow(() -> new IllegalArgumentException("Officer not found"));

        if (!newRank.isHigherThan(officer.getRank())) {
            throw new IllegalArgumentException("New rank must be higher than current rank");
        }

        officer.promoteTo(newRank);
        officerRepository.save(officer);
        return officer;
    }

    /**
     * Demotes an officer to a lower rank.
     *
     * @param officerId The ID of the officer to demote
     * @param newRank The new rank for the officer
     * @return The updated officer
     * @throws IllegalArgumentException If officer not found or demotion invalid
     */
    public Officer demoteOfficer(UUID officerId, Rank newRank) {
        Officer officer = officerRepository.findById(officerId)
                .orElseThrow(() -> new IllegalArgumentException("Officer not found"));

        if (newRank.isHigherThan(officer.getRank())) {
            throw new IllegalArgumentException("New rank must be lower than current rank");
        }

        officer.demoteTo(newRank);
        officerRepository.save(officer);
        return officer;
    }

    /**
     * Retrieves all officers with a rank equal to or higher than the specified rank.
     *
     * @param minimumRank The minimum rank to filter by
     * @return List of officers with the specified minimum rank
     */
    public List<Officer> findOfficersWithMinimumRank(Rank minimumRank) {
        return officerRepository.findAll().stream()
                .filter(officer -> officer.getRank().compareTo(minimumRank) >= 0)
                .toList();
    }
}