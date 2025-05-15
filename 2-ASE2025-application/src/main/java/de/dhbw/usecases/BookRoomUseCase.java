package de.dhbw.usecases;

import de.dhbw.aggregates.Room;
import de.dhbw.services.RoomManagementService;

import java.util.UUID;

/**
 * Use case for booking a room, making it unavailable for other activities.
 */
public class BookRoomUseCase {
    private final RoomManagementService roomManagementService;

    public BookRoomUseCase(RoomManagementService roomManagementService) {
        this.roomManagementService = roomManagementService;
    }

    /**
     * Execute the use case to book a specific room.
     *
     * @param roomId The ID of the room to book
     * @return The booked room
     * @throws IllegalArgumentException If room not found
     * @throws IllegalStateException If room is already booked
     */
    public Room execute(UUID roomId) {
        return roomManagementService.bookRoom(roomId);
    }
}