package de.dhbw.repositories.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.dhbw.aggregates.Meeting;
import de.dhbw.repositories.MeetingRepository;
import de.dhbw.repositories.OfficerRepository;
import de.dhbw.repositories.RoomRepository;
import de.dhbw.repositories.json.deserializers.MeetingDeserializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JSON implementation of the MeetingRepository interface.
 * Stores meetings in a JSON file.
 */
public class JsonMeetingRepository extends JsonPersistenceBase<Meeting> implements MeetingRepository {
    private Map<UUID, Meeting> meetings = new HashMap<>();
    private final OfficerRepository officerRepository;
    private final RoomRepository roomRepository;

    public JsonMeetingRepository(
            OfficerRepository officerRepository,
            RoomRepository roomRepository) {
        super("meetings", Meeting.class);
        this.officerRepository = officerRepository;
        this.roomRepository = roomRepository;

        // Register the custom deserializer
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Meeting.class, new MeetingDeserializer(
                officerRepository, roomRepository));
        objectMapper.registerModule(module);

        loadMeetings();
    }

    /**
     * Loads meetings from the JSON file.
     */
    private void loadMeetings() {
        List<Meeting> meetingList = loadFromFile();
        meetings.clear();
        for (Meeting meeting : meetingList) {
            meetings.put(meeting.getId(), meeting);
        }
    }

    /**
     * Saves meetings to the JSON file.
     */
    private void saveMeetings() {
        saveToFile(new ArrayList<>(meetings.values()));
    }

    @Override
    public void save(Meeting meeting) {
        meetings.put(meeting.getId(), meeting);
        saveMeetings();
    }

    @Override
    public Optional<Meeting> findById(UUID id) {
        return Optional.ofNullable(meetings.get(id));
    }

    @Override
    public List<Meeting> findAll() {
        return new ArrayList<>(meetings.values());
    }

    @Override
    public List<Meeting> findByOfficerId(UUID officerId) {
        return meetings.values().stream()
                .filter(meeting -> meeting.getOfficer().getId().equals(officerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meeting> findByRoomId(UUID roomId) {
        return meetings.values().stream()
                .filter(meeting -> meeting.getRoom().getId().equals(roomId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meeting> findByDate(LocalDate date) {
        return meetings.values().stream()
                .filter(meeting -> meeting.getScheduledAt().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meeting> findUpcoming() {
        LocalDateTime now = LocalDateTime.now();
        return meetings.values().stream()
                .filter(meeting -> meeting.getScheduledAt().isAfter(now))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        meetings.remove(id);
        saveMeetings();
    }
}