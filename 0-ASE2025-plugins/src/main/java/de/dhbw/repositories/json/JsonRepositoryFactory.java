package de.dhbw.repositories.json;

import de.dhbw.repositories.*;

/**
 * Factory for creating JSON repositories with proper dependencies.
 * This helps manage the circular dependencies between repositories.
 */
public class JsonRepositoryFactory {
    private JsonRoomRepository roomRepository;
    private JsonOfficerRepository officerRepository;
    private JsonDetaineeRepository detaineeRepository;
    private JsonInterrogationRepository interrogationRepository;
    private JsonMeetingRepository meetingRepository;

    /**
     * Creates all repositories with proper dependencies.
     */
    public JsonRepositoryFactory() {
        initializeRepositories();

        // Register repositories in the registry for use during deserialization
        RepositoryRegistry.getInstance().registerRepositories(
                roomRepository,
                officerRepository,
                detaineeRepository,
                interrogationRepository,
                meetingRepository);
    }

    /**
     * Initializes all repositories with proper dependencies.
     */
    private void initializeRepositories() {
        // First, create repositories without dependencies
        roomRepository = new JsonRoomRepository();
        officerRepository = new JsonOfficerRepository();
        detaineeRepository = new JsonDetaineeRepository();

        // Then create repositories with dependencies
        interrogationRepository = new JsonInterrogationRepository(
                officerRepository, detaineeRepository, roomRepository);
        meetingRepository = new JsonMeetingRepository(
                officerRepository, roomRepository);
    }

    /**
     * Gets the room repository.
     *
     * @return The room repository
     */
    public RoomRepository getRoomRepository() {
        return roomRepository;
    }

    /**
     * Gets the officer repository.
     *
     * @return The officer repository
     */
    public OfficerRepository getOfficerRepository() {
        return officerRepository;
    }

    /**
     * Gets the detainee repository.
     *
     * @return The detainee repository
     */
    public DetaineeRepository getDetaineeRepository() {
        return detaineeRepository;
    }

    /**
     * Gets the interrogation repository.
     *
     * @return The interrogation repository
     */
    public InterrogationRepository getInterrogationRepository() {
        return interrogationRepository;
    }

    /**
     * Gets the meeting repository.
     *
     * @return The meeting repository
     */
    public MeetingRepository getMeetingRepository() {
        return meetingRepository;
    }
}