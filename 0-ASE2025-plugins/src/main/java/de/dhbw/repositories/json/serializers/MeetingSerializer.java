package de.dhbw.repositories.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.dhbw.aggregates.Meeting;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Custom serializer for Meeting objects.
 * Prevents cycles and properly handles the serialization of Meeting objects.
 */
public class MeetingSerializer extends StdSerializer<Meeting> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    public MeetingSerializer() {
        this(null);
    }
    
    public MeetingSerializer(Class<Meeting> t) {
        super(t);
    }
    
    @Override
    public void serialize(Meeting meeting, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        
        gen.writeStringField("id", meeting.getId().toString());
        
        // Write officer
        gen.writeObjectFieldStart("officer");
        gen.writeStringField("id", meeting.getOfficer().getId().toString());
        gen.writeStringField("name", meeting.getOfficer().getName());
        
        // Write rank
        gen.writeObjectFieldStart("rank");
        gen.writeStringField("name", meeting.getOfficer().getRank().getName());
        gen.writeNumberField("level", meeting.getOfficer().getRank().compareTo(new de.dhbw.valueobjects.Rank("", 0)));
        gen.writeEndObject(); // End rank
        
        gen.writeEndObject(); // End officer
        
        gen.writeStringField("informantName", meeting.getInformantName());
        
        // Write room
        gen.writeObjectFieldStart("room");
        gen.writeStringField("id", meeting.getRoom().getId().toString());
        gen.writeStringField("type", meeting.getRoom().getType().name());
        gen.writeBooleanField("available", meeting.getRoom().isAvailable());
        gen.writeEndObject(); // End room
        
        gen.writeStringField("scheduledAt", meeting.getScheduledAt().format(FORMATTER));
        
        gen.writeEndObject(); // End meeting
    }
}