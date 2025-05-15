package de.dhbw.commands;

/**
 * Abstract base class for all commands that implements common behavior.
 */
public abstract class AbstractCommand implements Command {
    private final String name;
    private final String description;
    private final String usage;

    /**
     * Creates a new command with the given name, description, and usage.
     *
     * @param name The command name
     * @param description The command description
     * @param usage Usage instructions
     */
    protected AbstractCommand(String name, String description, String usage) {
        this.name = name;
        this.description = description;
        this.usage = usage;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getUsage() {
        return usage;
    }

    /**
     * Displays error message to the console.
     *
     * @param message The error message
     */
    protected void displayError(String message) {
        System.err.println("Error: " + message);
    }

    /**
     * Displays success message to the console.
     *
     * @param message The success message
     */
    protected void displaySuccess(String message) {
        System.out.println("Success: " + message);
    }

    /**
     * Displays information to the console.
     *
     * @param message The information message
     */
    protected void displayInfo(String message) {
        System.out.println(message);
    }
}