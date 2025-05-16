package de.dhbw.repositories.json;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for managing the data directory and files.
 */
public class DataDirectoryManager {
    private static final String DEFAULT_DATA_DIRECTORY = "data";

    /**
     * Gets the data directory path.
     *
     * @return The data directory path
     */
    public static String getDataDirectory() {
        return DEFAULT_DATA_DIRECTORY;
    }

    /**
     * Ensures that the data directory exists, creating it if necessary.
     */
    public static void ensureDataDirectoryExists() {
        File dataDir = new File(getDataDirectory());
        if (!dataDir.exists()) {
            if (!dataDir.mkdirs()) {
                throw new RuntimeException("Failed to create data directory: " + dataDir.getAbsolutePath());
            }
        }
    }

    /**
     * Gets the file path for a specific entity type.
     *
     * @param entityName The name of the entity
     * @return The file path
     */
    public static String getFilePathForEntity(String entityName) {
        return Paths.get(getDataDirectory(), entityName + ".json").toString();
    }

    /**
     * Checks if a data file exists for the specified entity type.
     *
     * @param entityName The name of the entity
     * @return true if the file exists, false otherwise
     */
    public static boolean dataFileExists(String entityName) {
        Path filePath = Paths.get(getFilePathForEntity(entityName));
        return Files.exists(filePath);
    }

    /**
     * Clears all data files.
     * Useful for testing or resetting the application.
     */
    public static void clearAllDataFiles() {
        File dataDir = new File(getDataDirectory());
        if (dataDir.exists() && dataDir.isDirectory()) {
            File[] files = dataDir.listFiles((dir, name) -> name.endsWith(".json"));
            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        System.err.println("Failed to delete data file: " + file.getName());
                    }
                }
            }
        }
    }
}