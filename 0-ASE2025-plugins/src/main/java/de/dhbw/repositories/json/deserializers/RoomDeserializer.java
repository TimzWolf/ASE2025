package de.dhbw.repositories.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.dhbw.aggregates.Room;
import de.dhbw.valueobjects.RoomType;

import java.io.IOException;

/**
 * Custom deserializer for Room objects.
 */
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
        
        RoomType type = RoomType.valueOf(node.get("type").asText());
        Room room = new Room(type);
        
        // Set the room's availability based on the JSON value
        if (!node.get("available").asBoolean()) {
            room.book();
        }
        
        return room;
    }
}