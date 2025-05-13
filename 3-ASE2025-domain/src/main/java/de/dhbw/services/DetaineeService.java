package de.dhbw.services;

import de.dhbw.aggregates.Detainee;
import de.dhbw.repositories.DetaineeRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Domain service handling operations related to detainees.
 */
public class DetaineeService {
    private final DetaineeRepository detaineeRepository;

    public DetaineeService(DetaineeRepository detaineeRepository) {
        this.detaineeRepository = detaineeRepository;
    }

    /**
     * Registers a new detainee in the system.
     *
     * @param name The name of the detainee
     * @param crime The crime the detainee is charged with
     * @return The newly registered detainee
     */
    public Detainee registerDetainee(String name, String crime) {
        Detainee detainee = new Detainee(name, crime);
        detaineeRepository.save(detainee);
        return detainee;
    }

    /**
     * Searches for detainees by name.
     *
     * @param nameQuery The name or part of the name to search for
     * @return List of detainees matching the search criteria
     */
    public List<Detainee> searchByName(String nameQuery) {
        return detaineeRepository.findByNameContaining(nameQuery);
    }

    /**
     * Finds all detainees charged with a specific crime.
     *
     * @param crime The crime to search for
     * @return List of detainees charged with the specified crime
     */
    public List<Detainee> findByCrime(String crime) {
        return detaineeRepository.findByCrime(crime);
    }

    /**
     * Retrieves a detainee by their ID.
     *
     * @param detaineeId The ID of the detainee to retrieve
     * @return The detainee
     * @throws IllegalArgumentException If detainee not found
     */
    public Detainee getDetainee(UUID detaineeId) {
        return detaineeRepository.findById(detaineeId)
                .orElseThrow(() -> new IllegalArgumentException("Detainee not found"));
    }
}