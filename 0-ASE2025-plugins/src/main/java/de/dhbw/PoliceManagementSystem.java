package de.dhbw;

import de.dhbw.cli.CliRunner;
import de.dhbw.repositories.*;
import de.dhbw.repositories.json.*;

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
            ensureEmptyJsonFilesExist();

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
     * Ensures that all required JSON files exist, creating empty ones if necessary.
     */
    private static void ensureEmptyJsonFilesExist() {
        try {
            String[] entityNames = {"rooms", "officers", "detainees", "interrogations", "meetings"};
            for (String entityName : entityNames) {
                java.nio.file.Path filePath = java.nio.file.Paths.get(
                        DataDirectoryManager.getDataDirectory(),
                        entityName + ".json");

                if (!java.nio.file.Files.exists(filePath)) {
                    // Create directory if it doesn't exist
                    java.nio.file.Files.createDirectories(filePath.getParent());

                    // Create empty array as initial content
                    java.nio.file.Files.writeString(filePath, "[]");
                    System.out.println("Created empty file: " + filePath);
                } else if (java.nio.file.Files.size(filePath) == 0) {
                    // If file exists but is empty, write empty array
                    java.nio.file.Files.writeString(filePath, "[]");
                    System.out.println("Updated empty file with valid JSON: " + filePath);
                }
            }
        } catch (Exception e) {
            System.err.println("Error creating empty JSON files: " + e.getMessage());
        }
    }
}