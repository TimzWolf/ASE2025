package de.dhbw.commands.officercommands;

import de.dhbw.aggregates.Officer;
import de.dhbw.commands.AbstractCommand;
import de.dhbw.repositories.OfficerRepository;

import java.util.List;

/**
 * Command to list all officers in the system.
 */
public class ListOfficersCommand extends AbstractCommand {
    private final OfficerRepository officerRepository;

    public ListOfficersCommand(OfficerRepository officerRepository) {
        super(
                "list-officers",
                "Lists all officers in the system",
                "list-officers"
        );
        this.officerRepository = officerRepository;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            List<Officer> officers = officerRepository.findAll();

            if (officers.isEmpty()) {
                displayInfo("No officers found in the system.");
                return true;
            }

            displayInfo("Officers in the system:");
            displayInfo("-----------------------");

            for (Officer officer : officers) {
                displayInfo(String.format("ID: %s | Name: %s | Rank: %s (Level %d)",
                        officer.getId(),
                        officer.getName(),
                        officer.getRank().getName(),
                        officer.getRank().getLevel()));  // Use getLevel() instead of comparing
            }

            return true;
        } catch (Exception e) {
            displayError("Failed to list officers: " + e.getMessage());
            return false;
        }
    }
}