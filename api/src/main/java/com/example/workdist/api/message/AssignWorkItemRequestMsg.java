package com.example.workdist.api.message;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.ImmutableSet;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder(builderClassName = "AssignWorkItemRequestMsgBuilder")
@JsonDeserialize(builder = AssignWorkItemRequestMsg.AssignWorkItemRequestMsgBuilder.class)
public class AssignWorkItemRequestMsg {

    @NonNull
    private final String title;

    @NonNull
    private final String priorityName;

    @NonNull
    private final ImmutableSet<String> requiredSkillNames;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AssignWorkItemRequestMsgBuilder {

    }
}
