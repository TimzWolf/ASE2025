package de.dhbw.repositories.json;

import de.dhbw.repositories.*;

/**
 * Registry for repositories that can be used during deserialization.
 * This helps with handling circular dependencies between domain objects.
 */
public class RepositoryRegistry {
    private static RepositoryRegistry instance;

    private RoomRepository roomRepository;
    private OfficerRepository officerRepository;
    private DetaineeRepository detaineeRepository;
    private InterrogationRepository interrogationRepository;
    private MeetingRepository meetingRepository;
    private boolean isInitialized = false;

    /**
     * Gets the singleton instance of the registry.
     *
     * @return The registry instance
     */
    public static synchronized RepositoryRegistry getInstance() {
        if (instance == null) {
            instance = new RepositoryRegistry();
        }
        return instance;
    }

    /**
     * Private constructor to prevent direct instantiation.
     */
    private RepositoryRegistry() {
        // Private constructor to enforce singleton pattern
    }

    /**
     * Registers all repositories.
     *
     * @param roomRepository The room repository
     * @param officerRepository The officer repository
     * @param detaineeRepository The detainee repository
     * @param interrogationRepository The interrogation repository
     * @param meetingRepository The meeting repository
     */
    public void registerRepositories(
            RoomRepository roomRepository,
            OfficerRepository officerRepository,
            DetaineeRepository detaineeRepository,
            InterrogationRepository interrogationRepository,
            MeetingRepository meetingRepository) {
        this.roomRepository = roomRepository;
        this.officerRepository = officerRepository;
        this.detaineeRepository = detaineeRepository;
        this.interrogationRepository = interrogationRepository;
        this.meetingRepository = meetingRepository;
        this.isInitialized = true;
    }

    /**
     * Checks if the registry is initialized with repositories.
     *
     * @return true if repositories are registered, false otherwise
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Gets the room repository.
     *
     * @return The room repository
     * @throws IllegalStateException if repository is not registered
     */
    public RoomRepository getRoomRepository() {
        checkInitialized();
        return roomRepository;
    }

    /**
     * Gets the officer repository.
     *
     * @return The officer repository
     * @throws IllegalStateException if repository is not registered
     */
    public OfficerRepository getOfficerRepository() {
        checkInitialized();
        return officerRepository;
    }

    /**
     * Gets the detainee repository.
     *
     * @return The detainee repository
     * @throws IllegalStateException if repository is not registered
     */
    public DetaineeRepository getDetaineeRepository() {
        checkInitialized();
        return detaineeRepository;
    }

    /**
     * Gets the interrogation repository.
     *
     * @return The interrogation repository
     * @throws IllegalStateException if repository is not registered
     */
    public InterrogationRepository getInterrogationRepository() {
        checkInitialized();
        return interrogationRepository;
    }

    /**
     * Gets the meeting repository.
     *
     * @return The meeting repository
     * @throws IllegalStateException if repository is not registered
     */
    public MeetingRepository getMeetingRepository() {
        checkInitialized();
        return meetingRepository;
    }

    /**
     * Checks if the registry is initialized and throws an exception if not.
     *
     * @throws IllegalStateException if repositories are not registered
     */
    private void checkInitialized() {
        if (!isInitialized) {
            throw new IllegalStateException("Required repositories not provided or registered");
        }
    }
}