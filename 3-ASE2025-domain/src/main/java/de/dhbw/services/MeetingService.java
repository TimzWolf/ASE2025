package de.dhbw.services;

import de.dhbw.aggregates.*;
import de.dhbw.repositories.MeetingRepository;
import de.dhbw.repositories.OfficerRepository;
import de.dhbw.repositories.RoomRepository;
import de.dhbw.valueobjects.RoomType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Domain service handling operations related to meetings with informants or other personnel.
 * This is a simplified version assuming a Meeting entity exists in your domain.
 */
public class MeetingService {
    private final RoomRepository roomRepository;
    private final OfficerRepository officerRepository;

    // Assume you have or will create a Meeting entity and repository
    private final MeetingRepository meetingRepository;

    public MeetingService(
            RoomRepository roomRepository,
            OfficerRepository officerRepository,
            MeetingRepository meetingRepository) {
        this.roomRepository = roomRepository;
        this.officerRepository = officerRepository;
        this.meetingRepository = meetingRepository;
    }

    /**
     * Schedules a meeting with an informant.
     *
     * @param officerId The ID of the officer hosting the meeting
     * @param informantName The name of the informant
     * @param scheduledTime The time when the meeting should take place
     * @return The scheduled meeting
     * @throws IllegalArgumentException If officer not found
     * @throws IllegalStateException If no meeting rooms are available
     */
    public Meeting scheduleMeeting(
            UUID officerId,
            String informantName,
            LocalDateTime scheduledTime) {

        // Find the officer
        Officer officer = officerRepository.findById(officerId)
                .orElseThrow(() -> new IllegalArgumentException("Officer not found"));

        // Find an available meeting room
        List<Room> availableRooms = roomRepository.findAvailableByType(RoomType.MEETING);
        if (availableRooms.isEmpty()) {
            throw new IllegalStateException("No meeting rooms available");
        }

        // Check if the officer is already scheduled for another meeting
        List<Meeting> officerMeetings = meetingRepository.findByOfficerId(officerId);

        boolean isOfficerAvailable = officerMeetings.stream()
                .noneMatch(m -> m.getScheduledAt().equals(scheduledTime));

        if (!isOfficerAvailable) {
            throw new IllegalStateException("Officer is already scheduled for another meeting");
        }

        // Book the room and create the meeting
        Room room = availableRooms.get(0);
        room.book();
        roomRepository.save(room);

        Meeting meeting = new Meeting(officer, informantName, room, scheduledTime);
        meetingRepository.save(meeting);

        return meeting;
    }

    /**
     * Cancels a previously scheduled meeting.
     *
     * @param meetingId The ID of the meeting to cancel
     * @throws IllegalArgumentException If meeting not found
     */
    public void cancelMeeting(UUID meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found"));

        // Release the room
        Room room = meeting.getRoom();
        room.release();
        roomRepository.save(room);

        // Delete the meeting
        meetingRepository.deleteById(meetingId);
    }

    /**
     * Retrieves all upcoming meetings for a specific officer.
     *
     * @param officerId The ID of the officer
     * @return List of upcoming meetings for the officer
     */
    public List<Meeting> getUpcomingMeetingsForOfficer(UUID officerId) {
        return meetingRepository.findByOfficerId(officerId).stream()
                .filter(meeting -> meeting.getScheduledAt().isAfter(LocalDateTime.now()))
                .toList();
    }
}
