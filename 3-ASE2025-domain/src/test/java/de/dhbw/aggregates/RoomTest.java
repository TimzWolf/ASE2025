package de.dhbw.aggregates;

import de.dhbw.valueobjects.RoomType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Room aggregate.
 * Tests the core functionality of the Room class.
 */
public class RoomTest {

    @Test
    public void newRoomShouldBeAvailable() {
        // Arrange
        Room room = new Room(RoomType.INTERROGATION);

        // Act & Assert
        assertTrue(room.isAvailable(), "A newly created room should be available");
    }

    @Test
    public void bookingRoomShouldMakeItUnavailable() {
        // Arrange
        Room room = new Room(RoomType.MEETING);

        // Act
        room.book();

        // Assert
        assertFalse(room.isAvailable(), "A booked room should not be available");
    }

    @Test
    public void bookingAlreadyBookedRoomShouldThrowException() {
        // Arrange
        Room room = new Room(RoomType.PRODUCTION);
        room.book();

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            room.book();
        });

        assertEquals("Room is already booked.", exception.getMessage());
    }

    @Test
    public void releasingRoomShouldMakeItAvailable() {
        // Arrange
        Room room = new Room(RoomType.INTERROGATION);
        room.book();
        assertFalse(room.isAvailable(), "Room should be unavailable after booking");

        // Act
        room.release();

        // Assert
        assertTrue(room.isAvailable(), "Room should be available after release");
    }
}