package de.dhbw.commands.detaineecommands;

import de.dhbw.commands.AbstractCommand;
import de.dhbw.usecases.ReleaseDetaineeUseCase;

import java.util.UUID;

/**
 * Command to release a detainee from the system.
 */
public class ReleaseDetaineeCommand extends AbstractCommand {
    private final ReleaseDetaineeUseCase releaseDetaineeUseCase;

    public ReleaseDetaineeCommand(ReleaseDetaineeUseCase releaseDetaineeUseCase) {
        super(
                "release-detainee",
                "Releases a detainee from the system",
                "release-detainee <detainee-id>"
        );
        this.releaseDetaineeUseCase = releaseDetaineeUseCase;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length != 1) {
            displayError("Invalid arguments. Usage: " + getUsage());
            return false;
        }

        try {
            UUID detaineeId = UUID.fromString(args[0]);
            releaseDetaineeUseCase.execute(detaineeId);

            displaySuccess("Detainee released successfully. ID: " + detaineeId);
            return true;
        } catch (IllegalArgumentException e) {
            displayError(e.getMessage());
            return false;
        } catch (Exception e) {
            displayError("Failed to release detainee: " + e.getMessage());
            return false;
        }
    }
}