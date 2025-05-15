package de.dhbw.usecases;

import de.dhbw.aggregates.Detainee;
import de.dhbw.services.DetaineeService;

import java.util.List;

/**
 * Use case for retrieving all registered detainees in the system.
 */
public class GetAllDetaineesUseCase {
    private final DetaineeService detaineeService;

    public GetAllDetaineesUseCase(DetaineeService detaineeService) {
        this.detaineeService = detaineeService;
    }

    /**
     * Execute the use case to retrieve all detainees.
     *
     * @return List of all detainees in the system
     */
    public List<Detainee> execute() {
        return detaineeService.getAllDetainees();
    }
}