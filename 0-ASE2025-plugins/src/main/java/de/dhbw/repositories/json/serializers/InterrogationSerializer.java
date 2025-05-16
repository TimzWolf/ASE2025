package de.dhbw.repositories.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.dhbw.aggregates.Interrogation;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Custom serializer for Interrogation objects.
 * Prevents cycles and properly handles the serialization of Interrogation objects.
 */
public class InterrogationSerializer extends StdSerializer<Interrogation> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    public InterrogationSerializer() {
        this(null);
    }
    
    public InterrogationSerializer(Class<Interrogation> t) {
        super(t);
    }
    
    @Override
    public void serialize(Interrogation interrogation, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        
        gen.writeStringField("id", interrogation.getId().toString());
        
        // Write officer
        gen.writeObjectFieldStart("officer");
        gen.writeStringField("id", interrogation.getOfficer().getId().toString());
        gen.writeStringField("name", interrogation.getOfficer().getName());
        
        // Write rank
        gen.writeObjectFieldStart("rank");
        gen.writeStringField("name", interrogation.getOfficer().getRank().getName());
        gen.writeNumberField("level", interrogation.getOfficer().getRank().compareTo(new de.dhbw.valueobjects.Rank("", 0)));
        gen.writeEndObject(); // End rank
        
        gen.writeEndObject(); // End officer
        
        // Write detainee
        gen.writeObjectFieldStart("detainee");
        gen.writeStringField("id", interrogation.getDetainee().getId().toString());
        gen.writeStringField("name", interrogation.getDetainee().getName());
        gen.writeStringField("crime", interrogation.getDetainee().getCrime());
        gen.writeEndObject(); // End detainee
        
        // Write room
        gen.writeObjectFieldStart("room");
        gen.writeStringField("id", interrogation.getRoom().getId().toString());
        gen.writeStringField("type", interrogation.getRoom().getType().name());
        gen.writeBooleanField("available", interrogation.getRoom().isAvailable());
        gen.writeEndObject(); // End room
        
        gen.writeStringField("scheduledAt", interrogation.getScheduledAt().format(FORMATTER));
        
        gen.writeEndObject(); // End interrogation
    }
}