package de.dhbw.usecases;

import de.dhbw.aggregates.Room;
import de.dhbw.services.RoomManagementService;

import java.util.List;

/**
 * Use case for retrieving all booked rooms in the system.
 */
public class GetAllBookedRoomsUseCase {
    private final RoomManagementService roomManagementService;

    public GetAllBookedRoomsUseCase(RoomManagementService roomManagementService) {
        this.roomManagementService = roomManagementService;
    }

    /**
     * Execute the use case to retrieve all booked rooms.
     *
     * @return List of all booked rooms in the system
     */
    public List<Room> execute() {
        return roomManagementService.getAllBookedRooms();
    }
}