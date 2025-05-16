package de.dhbw.repositories.json.mixins;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.dhbw.valueobjects.Rank;

import java.util.UUID;

/**
 * Jackson mixin class for Officer to control serialization/deserialization.
 */
public abstract class OfficerMixin {
    @JsonCreator
    public OfficerMixin(
            @JsonProperty("id") UUID id,
            @JsonProperty("name") String name,
            @JsonProperty("rank") Rank rank) {
    }
}