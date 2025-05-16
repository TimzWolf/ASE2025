package de.dhbw.repositories.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.dhbw.valueobjects.Rank;

import java.io.IOException;

public class RankDeserializer extends StdDeserializer<Rank> {

    public RankDeserializer() {
        this(null);
    }

    public RankDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Rank deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String name = node.get("name").asText();
        int level = node.get("level").asInt();

        return new Rank(name, level);
    }
}