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
        System.out.println("Data will be stored in the '" + DataDirectoryManager.getDataDirectory() + "' directory.");

        try {
            // Create the JSON repository factory
            JsonRepositoryFactory repositoryFactory = new JsonRepositoryFactory();

            // Get all repositories
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
}