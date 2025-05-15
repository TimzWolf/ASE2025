package de.dhbw.commands.detaineecommands;

import de.dhbw.aggregates.Detainee;
import de.dhbw.commands.AbstractCommand;
import de.dhbw.usecases.GetAllDetaineesUseCase;

import java.util.List;

/**
 * Command to list all detainees in the system.
 */
public class ListDetaineesCommand extends AbstractCommand {
    private final GetAllDetaineesUseCase getAllDetaineesUseCase;

    public ListDetaineesCommand(GetAllDetaineesUseCase getAllDetaineesUseCase) {
        super(
                "list-detainees",
                "Lists all detainees in the system",
                "list-detainees"
        );
        this.getAllDetaineesUseCase = getAllDetaineesUseCase;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            List<Detainee> detainees = getAllDetaineesUseCase.execute();

            if (detainees.isEmpty()) {
                displayInfo("No detainees found in the system.");
                return true;
            }

            displayInfo("Detainees in the system:");
            displayInfo("------------------------");

            for (Detainee detainee : detainees) {
                displayInfo(String.format("ID: %s | Name: %s | Crime: %s",
                        detainee.getId(), detainee.getName(), detainee.getCrime()));
            }

            return true;
        } catch (Exception e) {
            displayError("Failed to list detainees: " + e.getMessage());
            return false;
        }
    }
}