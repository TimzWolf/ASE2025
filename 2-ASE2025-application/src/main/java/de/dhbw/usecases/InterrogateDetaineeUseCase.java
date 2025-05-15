package de.dhbw.usecases;

import de.dhbw.aggregates.Detainee;
import de.dhbw.aggregates.Interrogation;
import de.dhbw.services.DetaineeService;
import de.dhbw.services.InterrogationService;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Use case for interrogating a detainee in an interrogation room.
 */
public class InterrogateDetaineeUseCase {
    private final InterrogationService interrogationService;
    private final DetaineeService detaineeService;

    public InterrogateDetaineeUseCase(
            InterrogationService interrogationService,
            DetaineeService detaineeService) {
        this.interrogationService = interrogationService;
        this.detaineeService = detaineeService;
    }

    /**
     * Execute the use case to schedule an interrogation of a detainee.
     *
     * @param officerId The ID of the officer conducting the interrogation
     * @param detaineeId The ID of the detainee to interrogate
     * @param scheduledTime The time when the interrogation should take place
     * @return The scheduled interrogation
     * @throws IllegalArgumentException If officer or detainee not found
     * @throws IllegalStateException If no interrogation rooms are available
     */
    public Interrogation execute(UUID officerId, UUID detaineeId, LocalDateTime scheduledTime) {
        // First, retrieve the detainee
        Detainee detainee = detaineeService.getDetainee(detaineeId);

        // Then schedule the interrogation
        return interrogationService.scheduleInterrogation(officerId, detainee, scheduledTime);
    }
}