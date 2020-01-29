package com.example.workdist.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.ImmutableSet;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Data
@Builder(builderClassName = "SkillBuilder")
@JsonDeserialize(builder = Skill.SkillBuilder.class)
public class Skill {
    @NonNull
    private final Integer id;

    @NonNull
    private final String name;

    @JsonPOJOBuilder(withPrefix = "")
    public static class SkillBuilder {
        // Lombok will add constructor, setters, build method
    }
}
