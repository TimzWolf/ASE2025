package de.dhbw.usecases;

import de.dhbw.aggregates.Detainee;
import de.dhbw.services.DetaineeService;

/**
 * Use case for registering a new detainee in the system.
 */
public class RegisterDetaineeUseCase {
    private final DetaineeService detaineeService;

    public RegisterDetaineeUseCase(DetaineeService detaineeService) {
        this.detaineeService = detaineeService;
    }

    /**
     * Execute the use case to register a new detainee.
     *
     * @param name The name of the detainee
     * @param crime The crime the detainee is charged with
     * @return The newly registered detainee
     */
    public Detainee execute(String name, String crime) {
        return detaineeService.registerDetainee(name, crime);
    }
}