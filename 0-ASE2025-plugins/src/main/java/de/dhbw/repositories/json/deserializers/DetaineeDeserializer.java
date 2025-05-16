package de.dhbw.repositories.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.dhbw.aggregates.Detainee;

import java.io.IOException;
import java.util.UUID;

public class DetaineeDeserializer extends StdDeserializer<Detainee> {

    public DetaineeDeserializer() {
        this(null);
    }

    public DetaineeDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Detainee deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        // Extract the UUID
        UUID id = UUID.fromString(node.get("id").asText());
        String name = node.get("name").asText();
        String crime = node.get("crime").asText();

        // Create a new Detainee
        try {
            Detainee detainee = new Detainee(name, crime);

            // Use reflection to set the id field
            java.lang.reflect.Field idField = Detainee.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(detainee, id);

            return detainee;
        } catch (Exception e) {
            throw new IOException("Could not deserialize Detainee", e);
        }
    }
}