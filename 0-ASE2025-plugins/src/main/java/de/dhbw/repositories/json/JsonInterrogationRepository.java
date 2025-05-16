package de.dhbw.repositories.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.dhbw.aggregates.Interrogation;
import de.dhbw.repositories.DetaineeRepository;
import de.dhbw.repositories.InterrogationRepository;
import de.dhbw.repositories.OfficerRepository;
import de.dhbw.repositories.RoomRepository;
import de.dhbw.repositories.json.deserializers.InterrogationDeserializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JSON implementation of the InterrogationRepository interface.
 * Stores interrogations in a JSON file.
 */
public class JsonInterrogationRepository extends JsonPersistenceBase<Interrogation> implements InterrogationRepository {
    private Map<UUID, Interrogation> interrogations = new HashMap<>();
    private final OfficerRepository officerRepository;
    private final DetaineeRepository detaineeRepository;
    private final RoomRepository roomRepository;

    public JsonInterrogationRepository(
            OfficerRepository officerRepository,
            DetaineeRepository detaineeRepository,
            RoomRepository roomRepository) {
        super("interrogations", Interrogation.class);
        this.officerRepository = officerRepository;
        this.detaineeRepository = detaineeRepository;
        this.roomRepository = roomRepository;

        // Register the custom deserializer that needs access to the repositories
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Interrogation.class, new InterrogationDeserializer(
                officerRepository, detaineeRepository, roomRepository));
        objectMapper.registerModule(module);

        loadInterrogations();
    }

    /**
     * Loads interrogations from the JSON file.
     */
    private void loadInterrogations() {
        List<Interrogation> interrogationList = loadFromFile();
        interrogations.clear();
        for (Interrogation interrogation : interrogationList) {
            interrogations.put(interrogation.getId(), interrogation);
        }
    }

    /**
     * Saves interrogations to the JSON file.
     */
    private void saveInterrogations() {
        saveToFile(new ArrayList<>(interrogations.values()));
    }

    @Override
    public void save(Interrogation interrogation) {
        interrogations.put(interrogation.getId(), interrogation);
        saveInterrogations();
    }

    @Override
    public Optional<Interrogation> findById(UUID id) {
        return Optional.ofNullable(interrogations.get(id));
    }

    @Override
    public List<Interrogation> findAll() {
        return new ArrayList<>(interrogations.values());
    }

    @Override
    public List<Interrogation> findByOfficerId(UUID officerId) {
        return interrogations.values().stream()
                .filter(interrogation -> interrogation.getOfficer().getId().equals(officerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Interrogation> findByDetaineeId(UUID detaineeId) {
        return interrogations.values().stream()
                .filter(interrogation -> interrogation.getDetainee().getId().equals(detaineeId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Interrogation> findByRoomId(UUID roomId) {
        return interrogations.values().stream()
                .filter(interrogation -> interrogation.getRoom().getId().equals(roomId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Interrogation> findByDate(LocalDate date) {
        return interrogations.values().stream()
                .filter(interrogation -> interrogation.getScheduledAt().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Interrogation> findUpcoming() {
        LocalDateTime now = LocalDateTime.now();
        return interrogations.values().stream()
                .filter(interrogation -> interrogation.getScheduledAt().isAfter(now))
                .collect(Collectors.toList());
    }

    @Override
    public List<Interrogation> findByTimeRange(LocalDateTime start, LocalDateTime end) {
        return interrogations.values().stream()
                .filter(interrogation -> {
                    LocalDateTime scheduledTime = interrogation.getScheduledAt();
                    return scheduledTime.isAfter(start) && scheduledTime.isBefore(end);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        interrogations.remove(id);
        saveInterrogations();
    }
}