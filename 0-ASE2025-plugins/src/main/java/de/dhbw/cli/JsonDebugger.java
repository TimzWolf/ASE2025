package de.dhbw.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.dhbw.aggregates.Officer;
import de.dhbw.repositories.json.deserializers.OfficerDeserializer;
import de.dhbw.repositories.json.serializers.OfficerSerializer;
import de.dhbw.valueobjects.Rank;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Utility class to debug JSON serialization/deserialization issues
 * This can be used for testing purposes
 */
public class JsonDebugger {

    /**
     * Tests reading and writing officers to a JSON file
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Create a data directory if it doesn't exist
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }

            // Create a test file path
            File testFile = new File("data/officers_test.json");

            // Initialize mapper with proper serializers
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(Officer.class, new OfficerSerializer());
            module.addDeserializer(Officer.class, new OfficerDeserializer());
            mapper.registerModule(module);

            // Create sample officers
            List<Officer> officers = new ArrayList<>();
            officers.add(new Officer("John Smith", new Rank("Inspector", 4)));
            officers.add(new Officer("Jane Doe", new Rank("Sergeant", 3)));

            // Write to file
            System.out.println("Writing officers to file...");
            mapper.writerWithDefaultPrettyPrinter().writeValue(testFile, officers);
            System.out.println("Officers written successfully.");

            // Read from file
            System.out.println("Reading officers from file...");
            List<Officer> readOfficers = mapper.readValue(testFile,
                    mapper.getTypeFactory().constructCollectionType(List.class, Officer.class));

            // Print results
            System.out.println("Read " + readOfficers.size() + " officers:");
            for (Officer officer : readOfficers) {
                System.out.println("  - ID: " + officer.getId());
                System.out.println("    Name: " + officer.getName());
                System.out.println("    Rank: " + officer.getRank().getName() + " (Level " + officer.getRank().getLevel() + ")");
            }

            System.out.println("Test completed successfully!");

        } catch (Exception e) {
            System.err.println("Error in JSON test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}