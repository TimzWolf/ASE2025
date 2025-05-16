package de.dhbw.repositories.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.dhbw.aggregates.Room;
import de.dhbw.valueobjects.RoomType;

import java.io.IOException;
import java.util.UUID;

public class RoomDeserializer extends StdDeserializer<Room> {

    public RoomDeserializer() {
        this(null);
    }

    public RoomDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Room deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        // Extract id and type
        UUID id = UUID.fromString(node.get("id").asText());
        RoomType type = RoomType.valueOf(node.get("type").asText());
        boolean available = node.get("available").asBoolean();

        try {
            // Create a new Room
            Room room = new Room(type);

            // Use reflection to set the id field
            java.lang.reflect.Field idField = Room.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(room, id);

            // Set the availability if needed
            if (!available) {
                room.book();
            }

            return room;
        } catch (Exception e) {
            throw new IOException("Could not deserialize Room", e);
        }
    }
}