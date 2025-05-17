package de.dhbw.repositories.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import de.dhbw.aggregates.Meeting;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Custom serializer for Meeting objects.
 * Enhanced with better error handling and detailed logging.
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
        try {
            System.out.println("Serializing meeting with ID: " + meeting.getId());

            gen.writeStartObject();

            // ID
            gen.writeStringField("id", meeting.getId().toString());

            // Officer
            if (meeting.getOfficer() != null) {
                gen.writeObjectFieldStart("officer");
                gen.writeStringField("id", meeting.getOfficer().getId().toString());
                gen.writeStringField("name", meeting.getOfficer().getName());

                // Rank
                if (meeting.getOfficer().getRank() != null) {
                    gen.writeObjectFieldStart("rank");
                    gen.writeStringField("name", meeting.getOfficer().getRank().getName());
                    gen.writeNumberField("level", meeting.getOfficer().getRank().getLevel());
                    gen.writeEndObject(); // End rank
                } else {
                    System.err.println("Warning: Rank is null in officer " + meeting.getOfficer().getId());
                    gen.writeNullField("rank");
                }

                gen.writeEndObject(); // End officer
            } else {
                System.err.println("Warning: Officer is null in meeting " + meeting.getId());
                gen.writeNullField("officer");
            }

            // InformantName
            if (meeting.getInformantName() != null) {
                gen.writeStringField("informantName", meeting.getInformantName());
            } else {
                System.err.println("Warning: InformantName is null in meeting " + meeting.getId());
                gen.writeNullField("informantName");
            }

            // Room
            if (meeting.getRoom() != null) {
                gen.writeObjectFieldStart("room");
                gen.writeStringField("id", meeting.getRoom().getId().toString());
                if (meeting.getRoom().getType() != null) {
                    gen.writeStringField("type", meeting.getRoom().getType().name());
                } else {
                    System.err.println("Warning: RoomType is null in meeting " + meeting.getId());
                    gen.writeNullField("type");
                }
                gen.writeBooleanField("available", meeting.getRoom().isAvailable());
                gen.writeEndObject(); // End room
            } else {
                System.err.println("Warning: Room is null in meeting " + meeting.getId());
                gen.writeNullField("room");
            }

            // ScheduledAt
            if (meeting.getScheduledAt() != null) {
                gen.writeStringField("scheduledAt", meeting.getScheduledAt().format(FORMATTER));
            } else {
                System.err.println("Warning: ScheduledAt is null in meeting " + meeting.getId());
                gen.writeNullField("scheduledAt");
            }

            gen.writeEndObject(); // End meeting

            System.out.println("Successfully serialized meeting " + meeting.getId());
        } catch (Exception e) {
            System.err.println("Error serializing Meeting: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Failed to serialize Meeting", e);
        }
    }
}