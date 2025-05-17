package de.dhbw.repositories.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.dhbw.aggregates.Detainee;
import de.dhbw.aggregates.Interrogation;
import de.dhbw.aggregates.Officer;
import de.dhbw.aggregates.Room;
import de.dhbw.repositories.DetaineeRepository;
import de.dhbw.repositories.OfficerRepository;
import de.dhbw.repositories.RoomRepository;
import de.dhbw.repositories.json.RepositoryRegistry;
import de.dhbw.valueobjects.Rank;
import de.dhbw.valueobjects.RoomType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class InterrogationDeserializer extends StdDeserializer<Interrogation> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private OfficerRepository officerRepository;
    private DetaineeRepository detaineeRepository;
    private RoomRepository roomRepository;
    private boolean repositoriesInitialized = false;

    public InterrogationDeserializer(
            OfficerRepository officerRepository,
            DetaineeRepository detaineeRepository,
            RoomRepository roomRepository) {
        this(null, officerRepository, detaineeRepository, roomRepository);
    }

    public InterrogationDeserializer(
            Class<?> vc,
            OfficerRepository officerRepository,
            DetaineeRepository detaineeRepository,
            RoomRepository roomRepository) {
        super(vc);
        this.officerRepository = officerRepository;
        this.detaineeRepository = detaineeRepository;
        this.roomRepository = roomRepository;

        // Check if repositories are already provided
        this.repositoriesInitialized = (officerRepository != null &&
                detaineeRepository != null &&
                roomRepository != null);
    }

    /**
     * Ensures repositories are initialized before using them.
     *
     * @throws IllegalStateException if repositories cannot be initialized
     */
    private void ensureRepositoriesInitialized() {
        if (!repositoriesInitialized) {
            // Try to get repositories from registry
            RepositoryRegistry registry = RepositoryRegistry.getInstance();
            if (registry.isInitialized()) {
                this.officerRepository = registry.getOfficerRepository();
                this.detaineeRepository = registry.getDetaineeRepository();
                this.roomRepository = registry.getRoomRepository();
                this.repositoriesInitialized = true;
            } else {
                throw new IllegalStateException("Required repositories not provided or registered");
            }
        }
    }

    @Override
    public Interrogation deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        try {
            // Ensure repositories are available
            ensureRepositoriesInitialized();

            JsonNode node = jp.getCodec().readTree(jp);

            // Extract the UUID
            UUID id = UUID.fromString(node.get("id").asText());

            // Get the officer from the repository based on ID or create one
            JsonNode officerNode = node.get("officer");
            UUID officerId = UUID.fromString(officerNode.get("id").asText());
            Officer officer = officerRepository.findById(officerId)
                    .orElseGet(() -> {
                        String officerName = officerNode.get("name").asText();
                        JsonNode rankNode = officerNode.get("rank");
                        String rankName = rankNode.get("name").asText();
                        int rankLevel = rankNode.has("level") ? rankNode.get("level").asInt() : 1;
                        return new Officer(officerName, new Rank(rankName, rankLevel));
                    });

            // Get the detainee from the repository based on ID or create one
            JsonNode detaineeNode = node.get("detainee");
            UUID detaineeId = UUID.fromString(detaineeNode.get("id").asText());
            Detainee detainee = detaineeRepository.findById(detaineeId)
                    .orElseGet(() -> {
                        String detaineeName = detaineeNode.get("name").asText();
                        String crime = detaineeNode.get("crime").asText();
                        return new Detainee(detaineeName, crime);
                    });

            // Get the room from the repository based on ID or create one
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

            // Create a new Interrogation
            Interrogation interrogation = new Interrogation(officer, detainee, room, scheduledAt);

            // Use reflection to set the id field
            java.lang.reflect.Field idField = Interrogation.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(interrogation, id);

            return interrogation;
        } catch (Exception e) {
            System.err.println("Error during Interrogation deserialization: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Could not deserialize Interrogation", e);
        }
    }
}