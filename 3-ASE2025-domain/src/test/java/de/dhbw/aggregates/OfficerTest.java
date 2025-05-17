package de.dhbw.aggregates;

import de.dhbw.valueobjects.Rank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Officer aggregate.
 * Tests the core functionality of the Officer class.
 */
public class OfficerTest {

    @Test
    public void officerShouldBeCreatedWithNameAndRank() {
        // Arrange
        String name = "John Doe";
        Rank rank = new Rank("Sergeant", 3);

        // Act
        Officer officer = new Officer(name, rank);

        // Assert
        assertEquals(name, officer.getName(), "Officer should have the correct name");
        assertEquals(rank, officer.getRank(), "Officer should have the correct rank");
        assertNotNull(officer.getId(), "Officer should have a non-null ID");
    }

    @Test
    public void officerPromotionShouldUpdateRank() {
        // Arrange
        Officer officer = new Officer("Jane Smith", new Rank("Officer", 1));
        Rank newRank = new Rank("Sergeant", 3);

        // Act
        officer.promoteTo(newRank);

        // Assert
        assertEquals(newRank, officer.getRank(), "Officer should have the new rank after promotion");
    }

    @Test
    public void promotingToLowerRankShouldThrowException() {
        // Arrange
        Officer officer = new Officer("James Bond", new Rank("Lieutenant", 4));
        Rank lowerRank = new Rank("Sergeant", 3);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            officer.promoteTo(lowerRank);
        });

        assertEquals("New rank must be higher.", exception.getMessage());
    }

    @Test
    public void demotingToHigherRankShouldThrowException() {
        // Arrange
        Officer officer = new Officer("Robert Smith", new Rank("Sergeant", 3));
        Rank higherRank = new Rank("Lieutenant", 4);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            officer.demoteTo(higherRank);
        });

        assertEquals("New rank must be lower.", exception.getMessage());
    }
}