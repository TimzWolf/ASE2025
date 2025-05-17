package de.dhbw;

import de.dhbw.cli.CliRunner;
import de.dhbw.repositories.*;
import de.dhbw.repositories.json.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main application entry point for the Police Management System.
 * Uses JSON repositories for persistent storage.
 */
public class PoliceManagementSystem {

    public static void main(String[] args) {
        System.out.println("Initializing Police Management System with JSON persistence...");

        try {
            // Ensure data directory exists first
            DataDirectoryManager.ensureDataDirectoryExists();
            System.out.println("Data will be stored in the '" + DataDirectoryManager.getDataDirectory() + "' directory.");

            // Create empty JSON files if they don't exist
            ensureValidJsonFilesExist();

            // Create the JSON repository factory - this will also initialize the repository registry
            JsonRepositoryFactory repositoryFactory = new JsonRepositoryFactory();

            // Now get all repositories
            RoomRepository roomRepository = repositoryFactory.getRoomRepository();
            OfficerRepository officerRepository = repositoryFactory.getOfficerRepository();
            DetaineeRepository detaineeRepository = repositoryFactory.getDetaineeRepository();
            InterrogationRepository interrogationRepository = repositoryFactory.getInterrogationRepository();
            MeetingRepository meetingRepository = repositoryFactory.getMeetingRepository();

            System.out.println("All repositories initialized successfully.");

            // Create and run the CLI
            CliRunner cli = new CliRunner(
                    roomRepository,
                    officerRepository,
                    detaineeRepository,
                    interrogationRepository,
                    meetingRepository);

            System.out.println("Police Management System started successfully.");

            // Start the application
            cli.run();
        } catch (Exception e) {
            System.err.println("Error starting Police Management System: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Ensures that all required JSON files exist and contain valid JSON.
     */
    private static void ensureValidJsonFilesExist() {
        try {
            String[] entityNames = {"rooms", "officers", "detainees", "interrogations", "meetings"};
            String dataDir = DataDirectoryManager.getDataDirectory();

            for (String entityName : entityNames) {
                Path filePath = Paths.get(dataDir, entityName + ".json");

                // Check if file exists
                if (!Files.exists(filePath)) {
                    // Create parent directories if needed
                    Files.createDirectories(filePath.getParent());

                    // Create empty array file
                    Files.writeString(filePath, "[]");
                    System.out.println("Created empty JSON file: " + filePath);
                } else {
                    // File exists, check if it's valid JSON
                    String content = Files.readString(filePath);
                    content = content.trim();

                    if (content.isEmpty() || content.equals("null")) {
                        // If file is empty or just contains "null", write an empty array
                        Files.writeString(filePath, "[]");
                        System.out.println("Replaced invalid content with empty array in: " + filePath);
                    } else if (!content.startsWith("[") || !content.endsWith("]")) {
                        // Not a valid JSON array, replace with empty array
                        System.out.println("Warning: Invalid JSON in " + filePath + ", replacing with empty array");
                        Files.writeString(filePath, "[]");
                    }

                    // Try to validate JSON by parsing it
                    try {
                        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                        mapper.readTree(filePath.toFile());
                    } catch (Exception e) {
                        System.out.println("Warning: Invalid JSON in " + filePath + ", replacing with empty array");
                        Files.writeString(filePath, "[]");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error ensuring valid JSON files: " + e.getMessage());
            e.printStackTrace();
        }
    }
}