package de.dhbw.commands;

import de.dhbw.aggregates.Meeting;
import de.dhbw.usecases.ScheduleMeetingUseCase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

/**
 * Command to schedule a meeting with an informant.
 */
public class ScheduleMeetingCommand extends AbstractCommand {
    private final ScheduleMeetingUseCase scheduleMeetingUseCase;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ScheduleMeetingCommand(ScheduleMeetingUseCase scheduleMeetingUseCase) {
        super(
                "schedule-meeting",
                "Schedules a meeting with an informant",
                "schedule-meeting <officer-id> <informant-name> <date-time>\n" +
                        "  Example: schedule-meeting 550e8400-e29b-41d4-a716-446655440000 \"John Smith\" \"2023-12-15 14:30\""
        );
        this.scheduleMeetingUseCase = scheduleMeetingUseCase;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length != 3) {
            displayError("Invalid arguments. Usage: " + getUsage());
            return false;
        }

        try {
            UUID officerId = UUID.fromString(args[0]);
            String informantName = args[1];
            LocalDateTime scheduledTime = LocalDateTime.parse(args[2], FORMATTER);

            Meeting meeting = scheduleMeetingUseCase.execute(officerId, informantName, scheduledTime);

            displaySuccess("Meeting scheduled successfully");
            displayInfo(String.format("Meeting ID: %s", meeting.getId()));
            displayInfo(String.format("Officer: %s", meeting.getOfficer().getName()));
            displayInfo(String.format("Informant: %s", meeting.getInformantName()));
            displayInfo(String.format("Room: %s", meeting.getRoom().getId()));
            displayInfo(String.format("Scheduled at: %s", meeting.getScheduledAt().format(FORMATTER)));

            return true;
        } catch (DateTimeParseException e) {
            displayError("Invalid date-time format. Use format: yyyy-MM-dd HH:mm (e.g., 2023-12-15 14:30)");
            return false;
        } catch (IllegalArgumentException e) {
            displayError(e.getMessage());
            return false;
        } catch (IllegalStateException e) {
            displayError(e.getMessage());
            return false;
        } catch (Exception e) {
            displayError("Failed to schedule meeting: " + e.getMessage());
            return false;
        }
    }
}