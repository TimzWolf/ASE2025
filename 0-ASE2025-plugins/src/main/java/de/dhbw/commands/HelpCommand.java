package de.dhbw.commands;

import java.util.List;

/**
 * Command to display help information for all available commands.
 */
public class HelpCommand extends AbstractCommand {
    private final List<Command> availableCommands;

    public HelpCommand(List<Command> availableCommands) {
        super(
                "help",
                "Displays help information for all available commands",
                "help [command-name]"
        );
        this.availableCommands = availableCommands;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length == 0) {
            // Display help for all commands
            displayInfo("Available commands:");
            displayInfo("-------------------");

            for (Command command : availableCommands) {
                displayInfo(String.format("%-25s - %s", command.getName(), command.getDescription()));
            }

            displayInfo("\nType 'help <command-name>' for detailed information about a specific command.");
            return true;
        } else if (args.length == 1) {
            // Display help for a specific command
            String commandName = args[0];
            Command command = availableCommands.stream()
                    .filter(c -> c.getName().equals(commandName))
                    .findFirst()
                    .orElse(null);

            if (command == null) {
                displayError("Command not found: " + commandName);
                return false;
            }

            displayInfo("Command: " + command.getName());
            displayInfo("Description: " + command.getDescription());
            displayInfo("Usage: " + command.getUsage());
            return true;
        } else {
            displayError("Invalid arguments. Usage: " + getUsage());
            return false;
        }
    }
}