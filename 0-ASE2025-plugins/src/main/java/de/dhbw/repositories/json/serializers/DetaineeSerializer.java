package de.dhbw.repositories.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.dhbw.aggregates.Detainee;

import java.io.IOException;

/**
 * Custom serializer for Detainee objects.
 * Ensures proper serialization of Detainee objects.
 */
public class DetaineeSerializer extends StdSerializer<Detainee> {
    
    public DetaineeSerializer() {
        this(null);
    }
    
    public DetaineeSerializer(Class<Detainee> t) {
        super(t);
    }
    
    @Override
    public void serialize(Detainee detainee, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", detainee.getId().toString());
        gen.writeStringField("name", detainee.getName());
        gen.writeStringField("crime", detainee.getCrime());
        gen.writeEndObject();
    }
}