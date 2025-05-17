package de.dhbw.services;

import de.dhbw.aggregates.*;
import de.dhbw.repositories.InterrogationRepository;
import de.dhbw.repositories.OfficerRepository;
import de.dhbw.repositories.RoomRepository;
import de.dhbw.valueobjects.Rank;
import de.dhbw.valueobjects.RoomType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InterrogationService {
    private static final Rank MINIMUM_RANK_FOR_INTERROGATION = new Rank("Sergeant", 3);
    private final RoomRepository roomRepository;
    private final OfficerRepository officerRepository;
    private final InterrogationRepository interrogationRepository;

    public InterrogationService(
            RoomRepository roomRepository,
            OfficerRepository officerRepository,
            InterrogationRepository interrogationRepository) {
        this.roomRepository = roomRepository;
        this.officerRepository = officerRepository;
        this.interrogationRepository = interrogationRepository;
    }

    /**
     * Schedules an interrogation if all conditions are met.
     * Conditions:
     * - Officer must be available
     * - Room must be of type INTERROGATION and available
     * - No other interrogation in the same room at the same time
     */
    public Interrogation scheduleInterrogation(
            UUID officerId,
            Detainee detainee,
            LocalDateTime scheduledTime) {

        Officer officer = officerRepository.findById(officerId)
                .orElseThrow(() -> new IllegalArgumentException("Officer not found"));

        // Verwendung der symbolischen Konstante
        if (officer.getRank().compareTo(MINIMUM_RANK_FOR_INTERROGATION) < 0) {
            throw new IllegalStateException("Officer does not have sufficient rank for interrogation");
        }

        // Find an available interrogation room
        List<Room> availableRooms = roomRepository.findAvailableByType(RoomType.INTERROGATION);
        if (availableRooms.isEmpty()) {
            throw new IllegalStateException("No interrogation rooms available");
        }

        // Check if the officer is already scheduled for another interrogation
        List<Interrogation> officerInterrogations =
                interrogationRepository.findByOfficerId(officerId);

        boolean isOfficerAvailable = officerInterrogations.stream()
                .noneMatch(i -> i.getScheduledAt().equals(scheduledTime));

        if (!isOfficerAvailable) {
            throw new IllegalStateException("Officer is already scheduled for another interrogation");
        }

        // Book the room and create the interrogation
        Room room = availableRooms.get(0);
        room.book();
        roomRepository.save(room);

        Interrogation interrogation = new Interrogation(officer, detainee, room, scheduledTime);
        interrogationRepository.save(interrogation);

        return interrogation;
    }
}
