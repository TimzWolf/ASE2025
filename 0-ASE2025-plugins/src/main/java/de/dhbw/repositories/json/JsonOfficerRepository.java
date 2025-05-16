package de.dhbw.repositories.json;

import de.dhbw.aggregates.Officer;
import de.dhbw.repositories.OfficerRepository;
import de.dhbw.valueobjects.Rank;

import java.util.*;
import java.util.stream.Collectors;

/**
 * JSON implementation of the OfficerRepository interface.
 * Stores officers in a JSON file.
 */
public class JsonOfficerRepository extends JsonPersistenceBase<Officer> implements OfficerRepository {
    private Map<UUID, Officer> officers = new HashMap<>();

    public JsonOfficerRepository() {
        super("officers", Officer.class);
        loadOfficers();
    }

    /**
     * Loads officers from the JSON file.
     */
    private void loadOfficers() {
        List<Officer> officerList = loadFromFile();
        officers.clear();
        for (Officer officer : officerList) {
            officers.put(officer.getId(), officer);
        }
    }

    /**
     * Saves officers to the JSON file.
     */
    private void saveOfficers() {
        saveToFile(new ArrayList<>(officers.values()));
    }

    @Override
    public void save(Officer officer) {
        officers.put(officer.getId(), officer);
        saveOfficers();
    }

    @Override
    public Optional<Officer> findById(UUID id) {
        return Optional.ofNullable(officers.get(id));
    }

    @Override
    public List<Officer> findAll() {
        return new ArrayList<>(officers.values());
    }

    @Override
    public void deleteById(UUID id) {
        officers.remove(id);
        saveOfficers();
    }
}