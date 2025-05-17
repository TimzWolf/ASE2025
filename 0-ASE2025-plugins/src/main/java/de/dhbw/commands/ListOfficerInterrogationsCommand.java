// 0-ASE2025-plugins/src/main/java/de/dhbw/commands/ListOfficerInterrogationsCommand.java
package de.dhbw.commands;

import de.dhbw.aggregates.Interrogation;
import de.dhbw.usecases.GetOfficerInterrogationsUseCase;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Command to list all interrogations scheduled for a specific officer.
 */
public class ListOfficerInterrogationsCommand extends AbstractCommand {
    private final GetOfficerInterrogationsUseCase getOfficerInterrogationsUseCase;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ListOfficerInterrogationsCommand(GetOfficerInterrogationsUseCase getOfficerInterrogationsUseCase) {
        super(
                "list-officer-interrogations",
                "Lists all interrogations scheduled for a specific officer",
                "list-officer-interrogations <officer-id> [--upcoming]\n" +
                        "  Example: list-officer-interrogations 550e8400-e29b-41d4-a716-446655440000 --upcoming"
        );
        this.getOfficerInterrogationsUseCase = getOfficerInterrogationsUseCase;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length == 0 || args.length > 2) {
            displayError("Invalid arguments. Usage: " + getUsage());
            return false;
        }

        try {
            UUID officerId = UUID.fromString(args[0]);
            boolean upcomingOnly = args.length == 2 && "--upcoming".equals(args[1]);

            List<Interrogation> interrogations;
            if (upcomingOnly) {
                interrogations = getOfficerInterrogationsUseCase.getUpcoming(officerId);
            } else {
                interrogations = getOfficerInterrogationsUseCase.execute(officerId);
            }

            if (interrogations.isEmpty()) {
                displayInfo("No interrogations found for the specified officer.");
                return true;
            }

            displayInfo("Interrogations for officer:");
            displayInfo("---------------------------");

            for (Interrogation interrogation : interrogations) {
                displayInfo(String.format("ID: %s | Detainee: %s | Crime: %s | Room: %s | Scheduled at: %s",
                        interrogation.getId(),
                        interrogation.getDetainee().getName(),
                        interrogation.getDetainee().getCrime(),
                        interrogation.getRoom().getId(),
                        interrogation.getScheduledAt().format(FORMATTER)));
            }

            return true;
        } catch (IllegalArgumentException e) {
            displayError(e.getMessage());
            return false;
        } catch (Exception e) {
            displayError("Failed to list officer interrogations: " + e.getMessage());
            return false;
        }
    }
}