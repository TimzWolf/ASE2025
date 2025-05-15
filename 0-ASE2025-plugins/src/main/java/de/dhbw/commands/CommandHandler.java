package de.dhbw.cli;

import de.dhbw.commands.Command;

import java.util.*;

/**
 * Manages the registry of available commands and processes user input.
 */
public class CommandHandler {
    private final Map<String, Command> commandRegistry = new HashMap<>();
    private final List<Command> availableCommands = new ArrayList<>();

    /**
     * Registers a command to be available for execution.
     *
     * @param command The command to register
     */
    public void registerCommand(Command command) {
        commandRegistry.put(command.getName().toLowerCase(), command);
        availableCommands.add(command);
    }

    /**
     * Gets all available commands.
     *
     * @return List of all registered commands
     */
    public List<Command> getAvailableCommands() {
        return Collections.unmodifiableList(availableCommands);
    }

    /**
     * Processes a command line input and executes the corresponding command.
     *
     * @param input The command line input from the user
     * @return true if the command was executed successfully, false otherwise
     */
    public boolean processCommand(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        // Parse the input into command name and arguments
        String[] parts = parseCommandLine(input);
        if (parts.length == 0) {
            return false;
        }

        String commandName = parts[0].toLowerCase();
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        // Find the command
        Command command = commandRegistry.get(commandName);
        if (command == null) {
            System.err.println("Unknown command: " + commandName);
            System.err.println("Type 'help' for a list of available commands.");
            return false;
        }

        // Execute the command
        return command.execute(args);
    }

    /**
     * Parses a command line string into an array of arguments, respecting quoted strings.
     *
     * @param commandLine The command line to parse
     * @return Array of arguments, where the first element is the command name
     */
    private String[] parseCommandLine(String commandLine) {
        List<String> arguments = new ArrayList<>();
        StringBuilder currentArg = new StringBuilder();
        boolean inQuotes = false;

        for (char c : commandLine.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ' ' && !inQuotes) {
                if (!currentArg.isEmpty()) {
                    arguments.add(currentArg.toString());
                    currentArg = new StringBuilder();
                }
            } else {
                currentArg.append(c);
            }
        }

        if (!currentArg.isEmpty()) {
            arguments.add(currentArg.toString());
        }

        return arguments.toArray(new String[0]);
    }
}