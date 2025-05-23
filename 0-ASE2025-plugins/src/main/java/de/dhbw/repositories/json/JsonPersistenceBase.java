package de.dhbw.repositories.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * Base class for JSON persistence implementations.
 * Provides common functionality for saving and loading objects to/from JSON files.
 */
public abstract class JsonPersistenceBase<T> {
    protected final ObjectMapper objectMapper;
    protected final String dataDirectory;
    protected final String entityName;
    protected final Class<T> entityClass;

    /**
     * Creates a new JSON persistence base.
     *
     * @param entityName The name of the entity (used for file naming)
     * @param entityClass The class of the entity
     */
    public JsonPersistenceBase(String entityName, Class<T> entityClass) {
        this.entityName = entityName;
        this.entityClass = entityClass;
        this.dataDirectory = "data";

        // Initialize Jackson ObjectMapper with proper configuration
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // For handling Java 8 date/time types
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print JSON

        // Configure object mapper to use fields rather than getters/setters
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        // Configure to handle missing values gracefully
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

        // Configure the JSON domain module
        JsonDomainModule.configureObjectMapper(objectMapper);

        // Ensure data directory exists
        createDataDirectoryIfNotExists();
    }

    /**
     * Creates the data directory if it doesn't exist.
     */
    private void createDataDirectoryIfNotExists() {
        File directory = new File(dataDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * Gets the file path for the entity.
     *
     * @return The file path
     */
    protected String getFilePath() {
        return Paths.get(dataDirectory, entityName + ".json").toString();
    }

    /**
     * Saves a list of entities to the JSON file.
     *
     * @param entities The entities to save
     */
    protected void saveToFile(List<T> entities) {
        try {
            objectMapper.writeValue(new File(getFilePath()), entities);
        } catch (IOException e) {
            throw new RuntimeException("Error saving " + entityName + " to JSON file", e);
        }
    }

    /**
     * Loads a list of entities from the JSON file.
     *
     * @return The loaded entities
     */
    protected List<T> loadFromFile() {
        Path filePath = Paths.get(getFilePath());
        if (!Files.exists(filePath)) {
            return Collections.emptyList();
        }

        try {
            // Check if file is empty
            if (Files.size(filePath) == 0) {
                return Collections.emptyList();
            }

            String content = Files.readString(filePath);
            if (content.trim().isEmpty() || content.trim().equals("[]")) {
                return Collections.emptyList();
            }

            return objectMapper.readValue(filePath.toFile(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, entityClass));
        } catch (IOException e) {
            System.err.println("Error loading " + entityName + " from JSON file: " + e.getMessage());
            // Return empty list instead of throwing exception to allow application to start
            return Collections.emptyList();
        }
    }
}