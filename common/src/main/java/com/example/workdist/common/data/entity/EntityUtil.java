package com.example.workdist.common.data.entity;

import com.example.workdist.common.model.Agent;
import com.example.workdist.common.model.Skill;
import com.example.workdist.common.model.WorkItem;
import com.google.common.collect.ImmutableSet;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

public class EntityUtil {

    public static Instant fromNullableRaw(final Date date) {
        return Optional.ofNullable(date).map(d -> d.toInstant()).orElse(null);
    }

    public static Agent toModel(final AgentDao dao) {
        return toModel(dao, false);
    }

    public static Agent toModel(final AgentDao dao, final boolean fetchSkills) {
        return Agent.builder()
                .id(dao.getId())
                .name(dao.getName())
                .skills(fetchSkills ? Optional.of(EntityUtil.toSkillModels(dao.getSkills())) : Optional.empty())
                .build();
    }

    public static Skill toModel(final SkillDao dao) {
        return Skill.builder()
                .id(dao.getId())
                .name(dao.getName())
                .build();
    }

    public static WorkItem toModel(final WorkItemDao dao) {
        return WorkItem.builder()
                .id(Optional.ofNullable(dao.getId()))
                .title(dao.getTitle())
                .priority(dao.getPriority())
                .requiredSkills(EntityUtil.toSkillModels(dao.getRequiredSkills()))
                .assignedAgent(Optional.ofNullable(dao.getAssignedAgent()).map(EntityUtil::toModel))
                .startedAt(Optional.ofNullable(dao.getStartedAt()))
                .completedAt(Optional.ofNullable(dao.getCompletedAt()))
                .createdAt(dao.getCreatedAt())
                .updatedAt(Optional.ofNullable(dao.getUpdatedAt()))
                .build();
    }

    public static ImmutableSet<Skill> toSkillModels(final Set<SkillDao> daoSet) {
        return daoSet.stream()
                .map(EntityUtil::toModel)
                .collect(ImmutableSet.toImmutableSet());
    }

    public static ImmutableSet<WorkItem> toWorkItemModels(final Set<WorkItemDao> daoSet) {
        return daoSet.stream()
                .map(EntityUtil::toModel)
                .collect(ImmutableSet.toImmutableSet());
    }

    public static AgentDao toDao(final Agent model) {
        final AgentDao dao = new AgentDao();
        dao.setId(model.getId());
        dao.setName(model.getName());
        model.getSkills()
                .map(EntityUtil::toDaos)
                .ifPresent(dao::setSkills);
        return dao;
    }

    public static SkillDao toDao(final Skill model) {
        final SkillDao dao = new SkillDao();
        dao.setId(model.getId());
        dao.setName(model.getName());
        return dao;
    }

    public static WorkItemDao toDao(final WorkItem model) {
        final WorkItemDao dao = new WorkItemDao();
        dao.setTitle(model.getTitle());
        dao.setPriority(model.getPriority());
        dao.setRequiredSkills(toDaos(model.getRequiredSkills()));
        return dao;
    }

    public static ImmutableSet<SkillDao> toDaos(final Set<Skill> modelSet) {
        return modelSet.stream()
                .map(EntityUtil::toDao)
                .collect(ImmutableSet.toImmutableSet());
    }

}
