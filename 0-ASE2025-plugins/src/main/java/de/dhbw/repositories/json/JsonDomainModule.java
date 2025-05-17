package de.dhbw.repositories.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.dhbw.aggregates.*;
import de.dhbw.repositories.json.deserializers.*;
import de.dhbw.repositories.json.serializers.*;
import de.dhbw.valueobjects.Rank;
import de.dhbw.valueobjects.RoomType;

/**
 * Configuration for Jackson JSON serialization of domain objects.
 * This class provides methods to configure the ObjectMapper for proper
 * serialization and deserialization of domain objects.
 */
public class JsonDomainModule {

    /**
     * Configures the ObjectMapper with custom serializers and deserializers for domain objects.
     *
     * @param objectMapper The ObjectMapper to configure
     */
    public static void configureObjectMapper(ObjectMapper objectMapper) {
        SimpleModule module = new SimpleModule("DomainModule");

        // Configure object mapper to be more lenient
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        // Register custom serializers
        module.addSerializer(Officer.class, new OfficerSerializer());
        module.addSerializer(Room.class, new RoomSerializer());
        module.addSerializer(Detainee.class, new DetaineeSerializer());
        module.addSerializer(Interrogation.class, new InterrogationSerializer());
        module.addSerializer(Meeting.class, new MeetingSerializer());

        // Only use this if you created a RankSerializer
        // module.addSerializer(Rank.class, new RankSerializer());

        // Register custom deserializers
        module.addDeserializer(Rank.class, new RankDeserializer());
        module.addDeserializer(Officer.class, new OfficerDeserializer());
        module.addDeserializer(Room.class, new RoomDeserializer());
        module.addDeserializer(Detainee.class, new DetaineeDeserializer());

        // These deserializers will use the repository registry to get the necessary repositories
        module.addDeserializer(Interrogation.class, new InterrogationDeserializer(null, null, null));
        module.addDeserializer(Meeting.class, new MeetingDeserializer(null, null));

        // Register the module with the ObjectMapper
        objectMapper.registerModule(module);
    }
}