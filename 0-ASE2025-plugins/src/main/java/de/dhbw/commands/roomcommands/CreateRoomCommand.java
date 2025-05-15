package de.dhbw.commands.roomcommands;

import de.dhbw.aggregates.Room;
import de.dhbw.commands.AbstractCommand;
import de.dhbw.usecases.CreateRoomUseCase;
import de.dhbw.valueobjects.RoomType;

/**
 * Command to create a new room in the system.
 */
public class CreateRoomCommand extends AbstractCommand {
    private final CreateRoomUseCase createRoomUseCase;

    public CreateRoomCommand(CreateRoomUseCase createRoomUseCase) {
        super(
                "create-room",
                "Creates a new room in the system",
                "create-room <room-type>\n" +
                        "  room-type: INTERROGATION, MEETING, or PRODUCTION"
        );
        this.createRoomUseCase = createRoomUseCase;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length != 1) {
            displayError("Invalid arguments. Usage: " + getUsage());
            return false;
        }

        try {
            RoomType roomType = RoomType.valueOf(args[0].toUpperCase());
            Room room = createRoomUseCase.execute(roomType);
            displaySuccess("Room created with ID: " + room.getId());
            return true;
        } catch (IllegalArgumentException e) {
            displayError("Invalid room type. Valid types are: INTERROGATION, MEETING, PRODUCTION");
            return false;
        } catch (Exception e) {
            displayError("Failed to create room: " + e.getMessage());
            return false;
        }
    }
}