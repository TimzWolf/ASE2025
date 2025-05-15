package de.dhbw.usecases;

import de.dhbw.aggregates.Meeting;
import de.dhbw.services.MeetingService;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Use case for scheduling a meeting with an informant.
 */
public class ScheduleMeetingUseCase {
    private final MeetingService meetingService;

    public ScheduleMeetingUseCase(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    /**
     * Execute the use case to schedule a meeting.
     *
     * @param officerId The ID of the officer hosting the meeting
     * @param informantName The name of the informant
     * @param scheduledTime The time when the meeting should take place
     * @return The scheduled meeting
     * @throws IllegalArgumentException If officer not found
     * @throws IllegalStateException If no meeting rooms are available
     */
    public Meeting execute(UUID officerId, String informantName, LocalDateTime scheduledTime) {
        return meetingService.scheduleMeeting(officerId, informantName, scheduledTime);
    }
}