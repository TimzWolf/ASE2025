package de.dhbw.commands;

import de.dhbw.aggregates.Room;
import de.dhbw.usecases.BookRoomUseCase;

import java.util.UUID;

/**
 * Command to book a room, making it unavailable for other activities.
 */
public class BookRoomCommand extends AbstractCommand {
    private final BookRoomUseCase bookRoomUseCase;

    public BookRoomCommand(BookRoomUseCase bookRoomUseCase) {
        super(
                "book-room",
                "Books a room, making it unavailable for other activities",
                "book-room <room-id>"
        );
        this.bookRoomUseCase = bookRoomUseCase;
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length != 1) {
            displayError("Invalid arguments. Usage: " + getUsage());
            return false;
        }

        try {
            UUID roomId = UUID.fromString(args[0]);
            Room room = bookRoomUseCase.execute(roomId);
            displaySuccess("Room booked successfully. Room ID: " + room.getId());
            return true;
        } catch (IllegalArgumentException e) {
            displayError("Invalid room ID format");
            return false;
        } catch (IllegalStateException e) {
            displayError(e.getMessage());
            return false;
        } catch (Exception e) {
            displayError("Failed to book room: " + e.getMessage());
            return false;
        }
    }
}