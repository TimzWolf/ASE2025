package de.dhbw.services;

import de.dhbw.aggregates.Detainee;
import de.dhbw.repositories.DetaineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the DetaineeService.
 * Tests the service layer with a fake repository implementation.
 */
public class DetaineeServiceTest {

    private FakeDetaineeRepository detaineeRepository;
    private DetaineeService detaineeService;

    @BeforeEach
    public void setUp() {
        detaineeRepository = new FakeDetaineeRepository();
        detaineeService = new DetaineeService(detaineeRepository);
    }

    @Test
    public void registerDetaineeShouldSaveAndReturnDetainee() {
        // Arrange
        String name = "John Doe";
        String crime = "Theft";

        // Act
        Detainee result = detaineeService.registerDetainee(name, crime);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(crime, result.getCrime());
        assertTrue(detaineeRepository.wasSaveCalled());

        // Verify saved detainee
        Detainee savedDetainee = detaineeRepository.getLastSavedDetainee();
        assertEquals(name, savedDetainee.getName());
        assertEquals(crime, savedDetainee.getCrime());
    }

    @Test
    public void getDetaineeShouldReturnDetaineeWhenFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        Detainee detainee = new Detainee("Jane Smith", "Assault");
        detaineeRepository.addDetainee(id, detainee);

        // Act
        Detainee result = detaineeService.getDetainee(id);

        // Assert
        assertNotNull(result);
        assertEquals(detainee, result);
        assertEquals("Jane Smith", result.getName());
        assertEquals("Assault", result.getCrime());
    }

    @Test
    public void getDetaineeShouldThrowExceptionWhenNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        // Repository is empty - no detainee added

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            detaineeService.getDetainee(id);
        });

        assertEquals("Detainee not found", exception.getMessage());
    }

    @Test
    public void getAllDetaineesShouldReturnAllDetainees() {
        // Arrange
        Detainee detainee1 = new Detainee("John Doe", "Theft");
        Detainee detainee2 = new Detainee("Jane Smith", "Assault");

        detaineeRepository.addDetainee(UUID.randomUUID(), detainee1);
        detaineeRepository.addDetainee(UUID.randomUUID(), detainee2);

        // Act
        List<Detainee> result = detaineeService.getAllDetainees();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(detainee1));
        assertTrue(result.contains(detainee2));
    }

    /**
     * Fake implementation of DetaineeRepository for testing purposes.
     */
    private static class FakeDetaineeRepository implements DetaineeRepository {
        private final java.util.Map<UUID, Detainee> detainees = new java.util.HashMap<>();
        private boolean saveCalled = false;
        private Detainee lastSavedDetainee = null;

        @Override
        public void save(Detainee detainee) {
            saveCalled = true;
            lastSavedDetainee = detainee;
            detainees.put(detainee.getId(), detainee);
        }

        @Override
        public Optional<Detainee> findById(UUID id) {
            return Optional.ofNullable(detainees.get(id));
        }

        @Override
        public List<Detainee> findByNameContaining(String name) {
            return detainees.values().stream()
                    .filter(d -> d.getName().contains(name))
                    .collect(java.util.stream.Collectors.toList());
        }

        @Override
        public List<Detainee> findByCrime(String crime) {
            return detainees.values().stream()
                    .filter(d -> d.getCrime().equals(crime))
                    .collect(java.util.stream.Collectors.toList());
        }

        @Override
        public List<Detainee> findAll() {
            return new java.util.ArrayList<>(detainees.values());
        }

        @Override
        public void deleteById(UUID id) {
            detainees.remove(id);
        }

        // Helper methods for testing
        public boolean wasSaveCalled() {
            return saveCalled;
        }

        public Detainee getLastSavedDetainee() {
            return lastSavedDetainee;
        }

        public void addDetainee(UUID id, Detainee detainee) {
            detainees.put(id, detainee);
        }
    }
}