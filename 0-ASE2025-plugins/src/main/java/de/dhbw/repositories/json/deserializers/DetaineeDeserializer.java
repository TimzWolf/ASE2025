package de.dhbw.repositories.json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.dhbw.aggregates.Detainee;

import java.io.IOException;

/**
 * Custom deserializer for Detainee objects.
 */
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
        
        String name = node.get("name").asText();
        String crime = node.get("crime").asText();
        
        return new Detainee(name, crime);
    }
}