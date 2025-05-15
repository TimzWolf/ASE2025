package de.dhbw.commands;

import de.dhbw.aggregates.Interrogation;
import de.dhbw.usecases.InterrogateDetaineeUseCase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

/**
 * Command to schedule an interrogation with a detainee.
 */
public class ScheduleInterrogationCommand extends AbstractCommand {
    private final InterrogateDetaineeUseCase interrogateDetaineeUseCase;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ScheduleInterrogationCommand(InterrogateDetaineeUseCase interrogateDetaineeUseCase) {
        super(
                "schedule-interrogation",
                "Schedules an interrogation with a detainee",
                "schedule-interrogation <officer-id> <detainee-id> <date-time>\n" +
                        "  Example: schedule-interrogation 550e8400-e29b-41d4-a716-446655440000 662e8400-e29b-41d4-a716-446655440000 \"2023-12-15 14:30\""
        );
        this.interrogateDetaineeUseCase = interrogateDetaineeUseCase;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length != 3) {
            displayError("Invalid arguments. Usage: " + getUsage());
            return false;
        }

        try {
            UUID officerId = UUID.fromString(args[0]);
            UUID detaineeId = UUID.fromString(args[1]);
            LocalDateTime scheduledTime = LocalDateTime.parse(args[2], FORMATTER);

            Interrogation interrogation = interrogateDetaineeUseCase.execute(officerId, detaineeId, scheduledTime);

            displaySuccess("Interrogation scheduled successfully");
            displayInfo(String.format("Interrogation ID: %s", interrogation.getId()));
            displayInfo(String.format("Officer: %s", interrogation.getOfficer().getName()));
            displayInfo(String.format("Detainee: %s", interrogation.getDetainee().getName()));
            displayInfo(String.format("Room: %s", interrogation.getRoom().getId()));
            displayInfo(String.format("Scheduled at: %s", interrogation.getScheduledAt().format(FORMATTER)));

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
            displayError("Failed to schedule interrogation: " + e.getMessage());
            return false;
        }
    }
}