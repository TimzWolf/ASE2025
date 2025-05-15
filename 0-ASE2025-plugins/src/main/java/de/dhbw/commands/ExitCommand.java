package de.dhbw.commands;

/**
 * Command to exit the application.
 */
public class ExitCommand extends AbstractCommand {

    public ExitCommand() {
        super(
                "exit",
                "Exits the application",
                "exit"
        );
    }

    @Override
    public boolean execute(String[] args) {
        displayInfo("Exiting application. Goodbye!");
        System.exit(0);
        return true; // This will not be reached, but is required by the interface
    }
}