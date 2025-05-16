package de.dhbw.repositories.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.dhbw.aggregates.Officer;
import de.dhbw.valueobjects.Rank;

import java.io.IOException;

/**
 * Custom deserializer for Officer objects.
 */
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
        
        String name = node.get("name").asText();
        
        // Extract rank information
        JsonNode rankNode = node.get("rank");
        String rankName = rankNode.get("name").asText();
        int rankLevel = rankNode.get("level").asInt();
        
        Rank rank = new Rank(rankName, rankLevel);
        
        return new Officer(name, rank);
    }
}