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