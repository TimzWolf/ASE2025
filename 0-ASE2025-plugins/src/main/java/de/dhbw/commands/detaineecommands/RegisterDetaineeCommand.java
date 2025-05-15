package de.dhbw.commands.detaineecommands;

import de.dhbw.aggregates.Detainee;
import de.dhbw.commands.AbstractCommand;
import de.dhbw.usecases.RegisterDetaineeUseCase;

/**
 * Command to register a new detainee in the system.
 */
public class RegisterDetaineeCommand extends AbstractCommand {
    private final RegisterDetaineeUseCase registerDetaineeUseCase;

    public RegisterDetaineeCommand(RegisterDetaineeUseCase registerDetaineeUseCase) {
        super(
                "register-detainee",
                "Registers a new detainee in the system",
                "register-detainee <name> <crime>\n" +
                        "  Example: register-detainee \"John Doe\" \"Theft\""
        );
        this.registerDetaineeUseCase = registerDetaineeUseCase;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length != 2) {
            displayError("Invalid arguments. Usage: " + getUsage());
            return false;
        }

        try {
            String name = args[0];
            String crime = args[1];

            Detainee detainee = registerDetaineeUseCase.execute(name, crime);

            displaySuccess("Detainee registered with ID: " + detainee.getId());
            displayInfo(String.format("Name: %s, Crime: %s",
                    detainee.getName(), detainee.getCrime()));

            return true;
        } catch (Exception e) {
            displayError("Failed to register detainee: " + e.getMessage());
            return false;
        }
    }
}