package de.dhbw.usecases;

import de.dhbw.aggregates.Officer;
import de.dhbw.services.OfficerService;
import de.dhbw.valueobjects.Rank;

import java.util.UUID;

/**
 * Use case for changing the rank of an officer (promotion or demotion).
 */
public class ChangeRankUseCase {
    private final OfficerService officerService;

    public ChangeRankUseCase(OfficerService officerService) {
        this.officerService = officerService;
    }

    /**
     * Execute the use case to promote an officer to a higher rank.
     *
     * @param officerId The ID of the officer to promote
     * @param newRank The new rank for the officer
     * @return The updated officer
     * @throws IllegalArgumentException If officer not found or promotion invalid
     */
    public Officer promote(UUID officerId, Rank newRank) {
        return officerService.promoteOfficer(officerId, newRank);
    }

    /**
     * Execute the use case to demote an officer to a lower rank.
     *
     * @param officerId The ID of the officer to demote
     * @param newRank The new rank for the officer
     * @return The updated officer
     * @throws IllegalArgumentException If officer not found or demotion invalid
     */
    public Officer demote(UUID officerId, Rank newRank) {
        return officerService.demoteOfficer(officerId, newRank);
    }
}