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
@Builder(builderClassName = "AgentBuilder", toBuilder = true)
@JsonDeserialize(builder = Agent.AgentBuilder.class)
public class Agent {
    private static Agent.AgentBuilder hiddenBuilder() {
        return new Agent.AgentBuilder();
    }

    public static Agent.AgentBuilder builder() {
        return hiddenBuilder()
                .skills(Optional.empty())
                .inProgressWorkItems(Optional.empty());
    }


    @NonNull
    private final Integer id;

    @NonNull
    private final String name;

    @NonNull
    private final Optional<ImmutableSet<Skill>> skills;

    @NonNull
    private final Optional<ImmutableSet<WorkItem>> inProgressWorkItems;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AgentBuilder {
        // Lombok will add constructor, setters, build method
    }
}
