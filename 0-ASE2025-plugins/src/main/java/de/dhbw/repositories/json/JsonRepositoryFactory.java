package de.dhbw.repositories.json;

import de.dhbw.repositories.*;
import de.dhbw.repositories.json.deserializers.*;

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
        // First, create basic repositories without complex dependencies
        createBasicRepositories();

        // Register these repositories in the registry for use during deserialization
        registerRepositories();

        // Now create repositories with dependencies after registry is set up
        createDependentRepositories();
    }

    /**
     * Creates basic repositories that don't depend on other repositories.
     */
    private void createBasicRepositories() {
        roomRepository = new JsonRoomRepository();
        officerRepository = new JsonOfficerRepository();
        detaineeRepository = new JsonDetaineeRepository();
    }

    /**
     * Registers repositories in the global registry.
     */
    private void registerRepositories() {
        // Initialize the temporary repositories in the registry
        RepositoryRegistry.getInstance().registerRepositories(
                roomRepository,
                officerRepository,
                detaineeRepository,
                null,  // Will be set later
                null   // Will be set later
        );
    }

    /**
     * Creates repositories that depend on other repositories.
     */
    private void createDependentRepositories() {
        // Now create repositories with dependencies
        interrogationRepository = new JsonInterrogationRepository(
                officerRepository, detaineeRepository, roomRepository);
        meetingRepository = new JsonMeetingRepository(
                officerRepository, roomRepository);

        // Update the registry with the complete set of repositories
        RepositoryRegistry.getInstance().registerRepositories(
                roomRepository,
                officerRepository,
                detaineeRepository,
                interrogationRepository,
                meetingRepository);
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