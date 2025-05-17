package de.dhbw.cli;

import de.dhbw.aggregates.Officer;
import de.dhbw.valueobjects.Rank;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Utility class to help reset and populate JSON files with initial data.
 * This can be useful when you want to start fresh with clean data files.
 */
public class JsonReset {

    /**
     * Main method to run the JSON reset utility
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("JSON Reset Utility");
        System.out.println("-----------------");

        try {
            // Ensure data directory exists
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
                System.out.println("Created data directory");
            }

            // Reset officers.json
            resetOfficersJson();

            // Reset other files (add as needed)
            resetEmptyJsonFile("rooms");
            resetEmptyJsonFile("detainees");
            resetEmptyJsonFile("interrogations");
            resetEmptyJsonFile("meetings");

            System.out.println("JSON Reset completed successfully!");

        } catch (Exception e) {
            System.err.println("Error during JSON reset: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Resets the officers.json file with sample data
     * @throws IOException If there's an error writing to the file
     */
    private static void resetOfficersJson() throws IOException {
        // Sample officer data
        String jsonContent = "[\n" +
                "  {\n" +
                "    \"id\": \"e22cbd23-69a6-4785-901f-b8584427ab8d\",\n" +
                "    \"name\": \"John Smith\",\n" +
                "    \"rank\": {\n" +
                "      \"name\": \"Inspector\",\n" +
                "      \"level\": 4\n" +
                "    }\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": \"" + UUID.randomUUID() + "\",\n" +
                "    \"name\": \"Jane Doe\",\n" +
                "    \"rank\": {\n" +
                "      \"name\": \"Sergeant\",\n" +
                "      \"level\": 3\n" +
                "    }\n" +
                "  }\n" +
                "]";

        Path path = Paths.get("data", "officers.json");
        Files.writeString(path, jsonContent);
        System.out.println("Reset officers.json with sample data");
    }

    /**
     * Resets a JSON file with an empty array
     * @param entityName The name of the entity (filename without .json)
     * @throws IOException If there's an error writing to the file
     */
    private static void resetEmptyJsonFile(String entityName) throws IOException {
        Path path = Paths.get("data", entityName + ".json");
        Files.writeString(path, "[]");
        System.out.println("Reset " + entityName + ".json with empty array");
    }
}