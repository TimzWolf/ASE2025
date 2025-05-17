package de.dhbw.repositories.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.dhbw.aggregates.Interrogation;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Custom serializer for Interrogation objects.
 * Enhanced with better error handling and detailed logging.
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
        try {
            System.out.println("Serializing interrogation with ID: " + interrogation.getId());

            gen.writeStartObject();

            // ID
            gen.writeStringField("id", interrogation.getId().toString());

            // Officer
            if (interrogation.getOfficer() != null) {
                gen.writeObjectFieldStart("officer");
                gen.writeStringField("id", interrogation.getOfficer().getId().toString());
                gen.writeStringField("name", interrogation.getOfficer().getName());

                // Rank
                gen.writeObjectFieldStart("rank");
                gen.writeStringField("name", interrogation.getOfficer().getRank().getName());
                gen.writeNumberField("level", interrogation.getOfficer().getRank().getLevel());
                gen.writeEndObject(); // End rank

                gen.writeEndObject(); // End officer
            } else {
                System.err.println("Warning: Officer is null in interrogation " + interrogation.getId());
                gen.writeNullField("officer");
            }

            // Detainee
            if (interrogation.getDetainee() != null) {
                gen.writeObjectFieldStart("detainee");
                gen.writeStringField("id", interrogation.getDetainee().getId().toString());
                gen.writeStringField("name", interrogation.getDetainee().getName());
                gen.writeStringField("crime", interrogation.getDetainee().getCrime());
                gen.writeEndObject(); // End detainee
            } else {
                System.err.println("Warning: Detainee is null in interrogation " + interrogation.getId());
                gen.writeNullField("detainee");
            }

            // Room
            if (interrogation.getRoom() != null) {
                gen.writeObjectFieldStart("room");
                gen.writeStringField("id", interrogation.getRoom().getId().toString());
                gen.writeStringField("type", interrogation.getRoom().getType().name());
                gen.writeBooleanField("available", interrogation.getRoom().isAvailable());
                gen.writeEndObject(); // End room
            } else {
                System.err.println("Warning: Room is null in interrogation " + interrogation.getId());
                gen.writeNullField("room");
            }

            // ScheduledAt
            if (interrogation.getScheduledAt() != null) {
                gen.writeStringField("scheduledAt", interrogation.getScheduledAt().format(FORMATTER));
            } else {
                System.err.println("Warning: ScheduledAt is null in interrogation " + interrogation.getId());
                gen.writeNullField("scheduledAt");
            }

            gen.writeEndObject(); // End interrogation

            System.out.println("Successfully serialized interrogation " + interrogation.getId());
        } catch (Exception e) {
            System.err.println("Error serializing Interrogation: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Failed to serialize Interrogation", e);
        }
    }
}