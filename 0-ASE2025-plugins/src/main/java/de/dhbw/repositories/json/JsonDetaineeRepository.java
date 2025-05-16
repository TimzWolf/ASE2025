package de.dhbw.repositories.json;

import de.dhbw.aggregates.Detainee;
import de.dhbw.repositories.DetaineeRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * JSON implementation of the DetaineeRepository interface.
 * Stores detainees in a JSON file.
 */
public class JsonDetaineeRepository extends JsonPersistenceBase<Detainee> implements DetaineeRepository {
    private Map<UUID, Detainee> detainees = new HashMap<>();

    public JsonDetaineeRepository() {
        super("detainees", Detainee.class);
        loadDetainees();
    }

    /**
     * Loads detainees from the JSON file.
     */
    private void loadDetainees() {
        List<Detainee> detaineeList = loadFromFile();
        detainees.clear();
        for (Detainee detainee : detaineeList) {
            detainees.put(detainee.getId(), detainee);
        }
    }

    /**
     * Saves detainees to the JSON file.
     */
    private void saveDetainees() {
        saveToFile(new ArrayList<>(detainees.values()));
    }

    @Override
    public void save(Detainee detainee) {
        detainees.put(detainee.getId(), detainee);
        saveDetainees();
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
        saveDetainees();
    }
}