package com.example.workdist.common.model;

import com.example.workdist.common.enums.Priority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.ImmutableSet;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.util.Optional;

@Data
@Builder(builderClassName = "WorkItemBuilder", builderMethodName = "hiddenBuilder")
@JsonDeserialize(builder = WorkItem.WorkItemBuilder.class)
public class WorkItem {

    private static WorkItemBuilder hiddenBuilder() {
        return new WorkItemBuilder();
    }

    public static WorkItemBuilder builder() {
        return hiddenBuilder()
                .id(Optional.empty())
                .assignedAgent(Optional.empty())
                .createdAt(Instant.now())
                .startedAt(Optional.empty())
                .completedAt(Optional.empty())
                .updatedAt(Optional.empty());
    }

    @NonNull
    private final Optional<Integer> id;

    @NonNull
    private final String title;

    @NonNull
    private final Priority priority;

    @NonNull
    private final ImmutableSet<Skill> requiredSkills;

    @NonNull
    private final Optional<Agent> assignedAgent;

    @NonNull
    private final Optional<Instant> startedAt;

    @NonNull
    private final Optional<Instant> completedAt;

    @NonNull
    @JsonIgnore
    private final Instant createdAt;

    @NonNull
    @JsonIgnore
    private final Optional<Instant> updatedAt;

    @JsonPOJOBuilder(withPrefix = "")
    public static class WorkItemBuilder {

    }
}
