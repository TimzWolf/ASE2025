package de.dhbw.commands;

import de.dhbw.aggregates.Officer;
import de.dhbw.usecases.ChangeRankUseCase;
import de.dhbw.valueobjects.Rank;

import java.util.UUID;

/**
 * Command to change the rank of an officer (promotion or demotion).
 */
public class ChangeRankCommand extends AbstractCommand {
    private final ChangeRankUseCase changeRankUseCase;

    public ChangeRankCommand(ChangeRankUseCase changeRankUseCase) {
        super(
                "change-rank",
                "Changes the rank of an officer (promotion or demotion)",
                "change-rank <promote|demote> <officer-id> <new-rank-name> <new-rank-level>\n" +
                        "  Example: change-rank promote 550e8400-e29b-41d4-a716-446655440000 \"Inspector\" 2"
        );
        this.changeRankUseCase = changeRankUseCase;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length != 4) {
            displayError("Invalid arguments. Usage: " + getUsage());
            return false;
        }

        try {
            String action = args[0].toLowerCase();
            UUID officerId = UUID.fromString(args[1]);
            String rankName = args[2];
            int rankLevel = Integer.parseInt(args[3]);

            Rank newRank = new Rank(rankName, rankLevel);
            Officer officer;

            if ("promote".equals(action)) {
                officer = changeRankUseCase.promote(officerId, newRank);
                displaySuccess("Officer promoted successfully");
            } else if ("demote".equals(action)) {
                officer = changeRankUseCase.demote(officerId, newRank);
                displaySuccess("Officer demoted successfully");
            } else {
                displayError("Invalid action. Use 'promote' or 'demote'");
                return false;
            }

            displayInfo(String.format("Officer ID: %s, Name: %s, New Rank: %s (Level %d)",
                    officer.getId(), officer.getName(), newRank.getName(), rankLevel));

            return true;
        } catch (NumberFormatException e) {
            displayError("Rank level must be a number");
            return false;
        } catch (IllegalArgumentException e) {
            displayError(e.getMessage());
            return false;
        } catch (Exception e) {
            displayError("Failed to change officer rank: " + e.getMessage());
            return false;
        }
    }
}