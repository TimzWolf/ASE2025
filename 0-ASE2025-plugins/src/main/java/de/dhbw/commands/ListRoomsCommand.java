package de.dhbw.commands;

import de.dhbw.aggregates.Room;
import de.dhbw.usecases.GetAllRoomsUseCase;

import java.util.List;

/**
 * Command to list all rooms in the system.
 */
public class ListRoomsCommand extends AbstractCommand {
    private final GetAllRoomsUseCase getAllRoomsUseCase;

    public ListRoomsCommand(GetAllRoomsUseCase getAllRoomsUseCase) {
        super(
                "list-rooms",
                "Lists all rooms in the system",
                "list-rooms"
        );
        this.getAllRoomsUseCase = getAllRoomsUseCase;
    }

    @Override
    public boolean execute(String[] args) {
        try {
            List<Room> rooms = getAllRoomsUseCase.execute();

            if (rooms.isEmpty()) {
                displayInfo("No rooms found in the system.");
                return true;
            }

            displayInfo("Rooms in the system:");
            displayInfo("-------------------");

            for (Room room : rooms) {
                displayInfo(String.format("ID: %s | Type: %s | Available: %s",
                        room.getId(), room.getType(), room.isAvailable() ? "Yes" : "No"));
            }

            return true;
        } catch (Exception e) {
            displayError("Failed to list rooms: " + e.getMessage());
            return false;
        }
    }
}