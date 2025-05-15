package de.dhbw.usecases;

import de.dhbw.services.DetaineeService;

import java.util.UUID;

/**
 * Use case for releasing a detainee (unregistering from the system).
 */
public class ReleaseDetaineeUseCase {
    private final DetaineeService detaineeService;

    public ReleaseDetaineeUseCase(DetaineeService detaineeService) {
        this.detaineeService = detaineeService;
    }

    /**
     * Execute the use case to release a detainee.
     *
     * @param detaineeId The ID of the detainee to release
     * @throws IllegalArgumentException If detainee not found
     */
    public void execute(UUID detaineeId) {
        detaineeService.releaseDetainee(detaineeId);
    }
}