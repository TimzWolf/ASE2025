package de.dhbw.repositories.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.dhbw.aggregates.Officer;
import de.dhbw.valueobjects.Rank;

import java.io.IOException;

/**
 * Custom serializer for Officer objects.
 * Prevents cycles and properly handles the serialization of Officer objects.
 */
public class OfficerSerializer extends StdSerializer<Officer> {

    public OfficerSerializer() {
        this(null);
    }

    public OfficerSerializer(Class<Officer> t) {
        super(t);
    }

    @Override
    public void serialize(Officer officer, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            gen.writeStartObject();
            gen.writeStringField("id", officer.getId().toString());
            gen.writeStringField("name", officer.getName());

            // Serialize the rank
            Rank rank = officer.getRank();
            gen.writeObjectFieldStart("rank");
            gen.writeStringField("name", rank.getName());
            gen.writeNumberField("level", rank.getLevel());
            gen.writeEndObject();

            gen.writeEndObject();
        } catch (Exception e) {
            System.err.println("Error serializing Officer: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}