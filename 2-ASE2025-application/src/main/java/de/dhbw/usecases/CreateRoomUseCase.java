package de.dhbw.usecases;

import de.dhbw.aggregates.Room;
import de.dhbw.repositories.RoomRepository;
import de.dhbw.services.RoomManagementService;
import de.dhbw.valueobjects.RoomType;

/**
 * Use case for creating a new room in the system.
 */
public class CreateRoomUseCase {
    private final RoomManagementService roomManagementService;

    public CreateRoomUseCase(RoomManagementService roomManagementService) {
        this.roomManagementService = roomManagementService;
    }

    /**
     * Execute the use case to create a new room of the specified type.
     *
     * @param roomType The type of room to create
     * @return The newly created room
     */
    public Room execute(RoomType roomType) {
        return roomManagementService.createRoom(roomType);
    }
}