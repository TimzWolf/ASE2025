package de.dhbw.usecases;

import de.dhbw.aggregates.Room;
import de.dhbw.services.RoomManagementService;

import java.util.List;

/**
 * Use case for retrieving all rooms registered in the system.
 */
public class GetAllRoomsUseCase {
    private final RoomManagementService roomManagementService;

    public GetAllRoomsUseCase(RoomManagementService roomManagementService) {
        this.roomManagementService = roomManagementService;
    }

    /**
     * Execute the use case to retrieve all rooms.
     *
     * @return List of all rooms in the system
     */
    public List<Room> execute() {
        return roomManagementService.getAllRooms();
    }
}