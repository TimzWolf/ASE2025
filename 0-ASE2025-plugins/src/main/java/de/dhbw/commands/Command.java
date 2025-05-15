package de.dhbw.commands;

/**
 * Base Command interface for all commands in the system.
 * Commands are used to execute specific use cases through the CLI.
 */
public interface Command {
    /**
     * Executes the command with the given arguments.
     *
     * @param args Arguments for command execution
     * @return true if command executed successfully, false otherwise
     */
    boolean execute(String[] args);

    /**
     * Gets the name of the command. This is what users will type to invoke the command.
     *
     * @return The command name
     */
    String getName();

    /**
     * Gets the description of the command for help information.
     *
     * @return The command description
     */
    String getDescription();

    /**
     * Gets the usage instructions for the command.
     *
     * @return Usage instructions
     */
    String getUsage();
}