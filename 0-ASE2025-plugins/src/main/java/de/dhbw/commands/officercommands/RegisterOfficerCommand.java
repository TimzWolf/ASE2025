package de.dhbw.commands.officercommands;

import de.dhbw.aggregates.Officer;
import de.dhbw.commands.AbstractCommand;
import de.dhbw.services.OfficerService;
import de.dhbw.valueobjects.Rank;

/**
 * Command to register a new officer in the system.
 */
public class RegisterOfficerCommand extends AbstractCommand {
    private final OfficerService officerService;

    public RegisterOfficerCommand(OfficerService officerService) {
        super(
                "register-officer",
                "Registers a new officer in the system",
                "register-officer <name> <rank-name> <rank-level>\n" +
                        "  Example: register-officer \"John Doe\" Officer 1"
        );
        this.officerService = officerService;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length != 3) {
            displayError("Invalid arguments. Usage: " + getUsage());
            return false;
        }

        try {
            String name = args[0];
            String rankName = args[1];
            int rankLevel = Integer.parseInt(args[2]);

            Rank rank = new Rank(rankName, rankLevel);
            Officer officer = officerService.registerOfficer(name, rank);

            displaySuccess("Officer registered with ID: " + officer.getId());
            displayInfo(String.format("Name: %s, Rank: %s (Level %d)",
                    officer.getName(), rank.getName(), rankLevel));

            return true;
        } catch (NumberFormatException e) {
            displayError("Rank level must be a number");
            return false;
        } catch (Exception e) {
            displayError("Failed to register officer: " + e.getMessage());
            return false;
        }
    }
}