package de.dhbw.repositories.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.dhbw.aggregates.Officer;
import de.dhbw.repositories.OfficerRepository;
import de.dhbw.repositories.json.deserializers.OfficerDeserializer;
import de.dhbw.repositories.json.serializers.OfficerSerializer;

import java.util.*;

/**
 * JSON implementation of the OfficerRepository interface.
 * Stores officers in a JSON file.
 */
public class JsonOfficerRepository extends JsonPersistenceBase<Officer> implements OfficerRepository {
    private Map<UUID, Officer> officers = new HashMap<>();

    public JsonOfficerRepository() {
        super("officers", Officer.class);

        // Register the custom serializer and deserializer
        SimpleModule module = new SimpleModule();
        module.addSerializer(Officer.class, new OfficerSerializer());
        module.addDeserializer(Officer.class, new OfficerDeserializer());
        objectMapper.registerModule(module);

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
        System.out.println("Loaded " + officers.size() + " officers from file");
    }

    /**
     * Saves officers to the JSON file.
     */
    private void saveOfficers() {
        List<Officer> officerList = new ArrayList<>(officers.values());
        saveToFile(officerList);
        System.out.println("Saved " + officers.size() + " officers to file");
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