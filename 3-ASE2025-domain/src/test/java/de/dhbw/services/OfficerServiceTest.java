package de.dhbw.services;

import de.dhbw.aggregates.Officer;
import de.dhbw.repositories.OfficerRepository;
import de.dhbw.valueobjects.Rank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the OfficerService.
 * Tests the service layer with a fake repository implementation.
 */
public class OfficerServiceTest {

    private FakeOfficerRepository officerRepository;
    private OfficerService officerService;

    @BeforeEach
    public void setUp() {
        officerRepository = new FakeOfficerRepository();
        officerService = new OfficerService(officerRepository);
    }

    @Test
    public void registerOfficerShouldSaveAndReturnOfficer() {
        // Arrange
        String name = "John Smith";
        Rank rank = new Rank("Sergeant", 3);

        // Act
        Officer result = officerService.registerOfficer(name, rank);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(rank, result.getRank());
        assertTrue(officerRepository.wasSaveCalled());

        // Verify saved officer
        Officer savedOfficer = officerRepository.getLastSavedOfficer();
        assertEquals(name, savedOfficer.getName());
        assertEquals(rank, savedOfficer.getRank());
    }

    @Test
    public void promoteOfficerShouldUpdateAndReturnOfficerWithHigherRank() {
        // Arrange
        UUID id = UUID.randomUUID();
        Rank currentRank = new Rank("Officer", 1);
        Rank newRank = new Rank("Sergeant", 3);
        Officer officer = new Officer("James Bond", currentRank);

        // Add the officer to the repository
        officerRepository.addOfficer(id, officer);

        // Act
        Officer result = officerService.promoteOfficer(id, newRank);

        // Assert
        assertNotNull(result);
        assertEquals(newRank, result.getRank());
        assertTrue(officerRepository.wasSaveCalled());

        // Verify saved officer
        Officer savedOfficer = officerRepository.getLastSavedOfficer();
        assertEquals(newRank, savedOfficer.getRank());
    }

    @Test
    public void promoteOfficerShouldThrowExceptionWhenOfficerNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        Rank newRank = new Rank("Sergeant", 3);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            officerService.promoteOfficer(id, newRank);
        });

        assertEquals("Officer not found", exception.getMessage());
        assertFalse(officerRepository.wasSaveCalled());
    }

    @Test
    public void demoteOfficerShouldUpdateAndReturnOfficerWithLowerRank() {
        // Arrange
        UUID id = UUID.randomUUID();
        Rank higherRank = new Rank("Lieutenant", 4);
        Rank lowerRank = new Rank("Sergeant", 3);
        Officer officer = new Officer("James Bond", higherRank);

        // Add the officer to the repository
        officerRepository.addOfficer(id, officer);

        // Act
        Officer result = officerService.demoteOfficer(id, lowerRank);

        // Assert
        assertNotNull(result);
        assertEquals(lowerRank, result.getRank());
        assertTrue(officerRepository.wasSaveCalled());

        // Verify saved officer
        Officer savedOfficer = officerRepository.getLastSavedOfficer();
        assertEquals(lowerRank, savedOfficer.getRank());
    }

    @Test
    public void findOfficersWithMinimumRankShouldReturnFilteredList() {
        // Arrange
        Rank lowRank = new Rank("Officer", 1);
        Rank midRank = new Rank("Sergeant", 3);
        Rank highRank = new Rank("Lieutenant", 4);

        Officer officer1 = new Officer("Officer Low", lowRank);
        Officer officer2 = new Officer("Officer Mid", midRank);
        Officer officer3 = new Officer("Officer High", highRank);

        officerRepository.addOfficer(UUID.randomUUID(), officer1);
        officerRepository.addOfficer(UUID.randomUUID(), officer2);
        officerRepository.addOfficer(UUID.randomUUID(), officer3);

        // Act - find officers with rank >= Sergeant
        List<Officer> result = officerService.findOfficersWithMinimumRank(midRank);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(officer2)); // Sergeant
        assertTrue(result.contains(officer3)); // Lieutenant
        assertFalse(result.contains(officer1)); // Officer - lower than minimum
    }

    /**
     * Fake implementation of OfficerRepository for testing purposes.
     */
    private static class FakeOfficerRepository implements OfficerRepository {
        private final Map<UUID, Officer> officers = new HashMap<>();
        private boolean saveCalled = false;
        private Officer lastSavedOfficer = null;

        @Override
        public void save(Officer officer) {
            saveCalled = true;
            lastSavedOfficer = officer;
            officers.put(officer.getId(), officer);
        }

        @Override
        public Optional<Officer> findById(UUID id) {
            return Optional.ofNullable(officers.get(id));
        }

        @Override
        public List<Officer> findAll() {
            return new ArrayList<>(officers.values());
        }

        @Override
        public void deleteById(UUID id) {
            officers.remove(id);
        }

        // Helper methods for testing
        public boolean wasSaveCalled() {
            return saveCalled;
        }

        public Officer getLastSavedOfficer() {
            return lastSavedOfficer;
        }

        public void addOfficer(UUID id, Officer officer) {
            officers.put(id, officer);
        }
    }
}