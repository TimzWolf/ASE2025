package de.dhbw;

import de.dhbw.cli.CliRunner;
import de.dhbw.repositories.*;
import de.dhbw.repositories.inmemory.*;

/**
 * Main application entry point for the Police Management System.
 */
public class PoliceManagementSystem {

    public static void main(String[] args) {
        // Initialize in-memory repositories
        RoomRepository roomRepository = new InMemoryRoomRepository();
        OfficerRepository officerRepository = new InMemoryOfficerRepository();
        DetaineeRepository detaineeRepository = new InMemoryDetaineeRepository();
        InterrogationRepository interrogationRepository = new InMemoryInterrogationRepository();
        MeetingRepository meetingRepository = new InMemoryMeetingRepository();

        // Create and run the CLI
        CliRunner cli = new CliRunner(
                roomRepository,
                officerRepository,
                detaineeRepository,
                interrogationRepository,
                meetingRepository);

        // Start the application
        cli.run();
    }
}