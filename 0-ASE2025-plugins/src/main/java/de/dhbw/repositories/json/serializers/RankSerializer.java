package de.dhbw.repositories.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.dhbw.valueobjects.Rank;

import java.io.IOException;

/**
 * Custom serializer for Rank value objects.
 * Ensures proper serialization of Rank objects.
 */
public class RankSerializer extends StdSerializer<Rank> {

    public RankSerializer() {
        this(null);
    }

    public RankSerializer(Class<Rank> t) {
        super(t);
    }

    @Override
    public void serialize(Rank rank, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("name", rank.getName());
        gen.writeNumberField("level", rank.getLevel());
        gen.writeEndObject();
    }
}