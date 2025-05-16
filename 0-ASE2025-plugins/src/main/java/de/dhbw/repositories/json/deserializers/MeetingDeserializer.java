package de.dhbw.repositories.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.dhbw.aggregates.Meeting;
import de.dhbw.aggregates.Officer;
import de.dhbw.aggregates.Room;
import de.dhbw.repositories.OfficerRepository;
import de.dhbw.repositories.RoomRepository;
import de.dhbw.repositories.json.RepositoryRegistry;
import de.dhbw.valueobjects.Rank;
import de.dhbw.valueobjects.RoomType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Custom deserializer for Meeting objects.
 * This is a more complex deserializer that requires repositories to look up referenced objects.
 */
public class MeetingDeserializer extends StdDeserializer<Meeting> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final OfficerRepository officerRepository;
    private final RoomRepository roomRepository;

    public MeetingDeserializer(
            OfficerRepository officerRepository,
            RoomRepository roomRepository) {
        this(null, officerRepository, roomRepository);
    }

    public MeetingDeserializer(
            Class<?> vc,
            OfficerRepository officerRepository,
            RoomRepository roomRepository) {
        super(vc);
        this.officerRepository = officerRepository != null ? officerRepository :
                (RepositoryRegistry.getInstance() != null ? RepositoryRegistry.getInstance().getOfficerRepository() : null);
        this.roomRepository = roomRepository != null ? roomRepository :
                (RepositoryRegistry.getInstance() != null ? RepositoryRegistry.getInstance().getRoomRepository() : null);

        if (this.officerRepository == null || this.roomRepository == null) {
            throw new IllegalStateException("Required repositories not provided or registered");
        }
    }

    @Override
    public Meeting deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        // Get the officer and room from the repositories based on their IDs
        JsonNode officerNode = node.get("officer");
        UUID officerId = UUID.fromString(officerNode.get("id").asText());
        Officer officer = officerRepository.findById(officerId)
                .orElseGet(() -> {
                    String name = officerNode.get("name").asText();
                    JsonNode rankNode = officerNode.get("rank");
                    String rankName = rankNode.get("name").asText();
                    int rankLevel = rankNode.get("level").asInt();
                    return new Officer(name, new Rank(rankName, rankLevel));
                });

        String informantName = node.get("informantName").asText();

        JsonNode roomNode = node.get("room");
        UUID roomId = UUID.fromString(roomNode.get("id").asText());
        Room room = roomRepository.findById(roomId)
                .orElseGet(() -> {
                    RoomType type = RoomType.valueOf(roomNode.get("type").asText());
                    Room newRoom = new Room(type);
                    if (!roomNode.get("available").asBoolean()) {
                        newRoom.book();
                    }
                    return newRoom;
                });

        // Parse the scheduled time
        LocalDateTime scheduledAt = LocalDateTime.parse(node.get("scheduledAt").asText(), FORMATTER);

        return new Meeting(officer, informantName, room, scheduledAt);
    }
}