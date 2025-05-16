package de.dhbw.repositories.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.dhbw.aggregates.Officer;
import de.dhbw.valueobjects.Rank;

import java.io.IOException;
import java.util.UUID;

public class OfficerDeserializer extends StdDeserializer<Officer> {

    public OfficerDeserializer() {
        this(null);
    }

    public OfficerDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Officer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        // Extract the UUID
        UUID id = UUID.fromString(node.get("id").asText());
        String name = node.get("name").asText();

        // Extract rank information
        JsonNode rankNode = node.get("rank");
        String rankName = rankNode.get("name").asText();
        int rankLevel = rankNode.get("level").asInt();

        Rank rank = new Rank(rankName, rankLevel);

        // Create a new Officer with reflection to bypass constructor limitations
        try {
            Officer officer = new Officer(name, rank);

            // Use reflection to set the id field
            java.lang.reflect.Field idField = Officer.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(officer, id);

            return officer;
        } catch (Exception e) {
            throw new IOException("Could not deserialize Officer", e);
        }
    }
}