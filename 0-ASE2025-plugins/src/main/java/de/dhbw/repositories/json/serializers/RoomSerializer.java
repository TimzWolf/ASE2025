package de.dhbw.repositories.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.dhbw.aggregates.Room;

import java.io.IOException;

/**
 * Custom serializer for Room objects.
 * Prevents cycles and properly handles the serialization of Room objects.
 */
public class RoomSerializer extends StdSerializer<Room> {
    
    public RoomSerializer() {
        this(null);
    }
    
    public RoomSerializer(Class<Room> t) {
        super(t);
    }
    
    @Override
    public void serialize(Room room, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", room.getId().toString());
        gen.writeStringField("type", room.getType().name());
        gen.writeBooleanField("available", room.isAvailable());
        gen.writeEndObject();
    }
}