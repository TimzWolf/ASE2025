package de.dhbw.repositories.inmemory;

import de.dhbw.aggregates.Detainee;
import de.dhbw.repositories.DetaineeRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the DetaineeRepository interface.
 * This is used for temporary storage in the CLI application.
 */
public class InMemoryDetaineeRepository implements DetaineeRepository {
    private final Map<UUID, Detainee> detainees = new HashMap<>();

    @Override
    public void save(Detainee detainee) {
        detainees.put(detainee.getId(), detainee);
    }

    @Override
    public Optional<Detainee> findById(UUID id) {
        return Optional.ofNullable(detainees.get(id));
    }

    @Override
    public List<Detainee> findByNameContaining(String name) {
        String lowerCaseName = name.toLowerCase();
        return detainees.values().stream()
                .filter(detainee -> detainee.getName().toLowerCase().contains(lowerCaseName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Detainee> findByCrime(String crime) {
        String lowerCaseCrime = crime.toLowerCase();
        return detainees.values().stream()
                .filter(detainee -> detainee.getCrime().toLowerCase().equals(lowerCaseCrime))
                .collect(Collectors.toList());
    }

    @Override
    public List<Detainee> findAll() {
        return new ArrayList<>(detainees.values());
    }

    @Override
    public void deleteById(UUID id) {
        detainees.remove(id);
    }
}