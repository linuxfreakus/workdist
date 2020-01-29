package com.example.workdist.common.service;

import com.example.workdist.common.data.entity.EntityUtil;
import com.example.workdist.common.data.entity.WorkItemDao;
import com.example.workdist.common.data.repository.AgentRepository;
import com.example.workdist.common.data.repository.SkillRepository;
import com.example.workdist.common.data.repository.WorkItemRepository;
import com.example.workdist.common.model.Agent;
import com.example.workdist.common.model.Skill;
import com.example.workdist.common.model.WorkItem;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.security.InvalidParameterException;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WorkAssignmentService {
    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private WorkItemRepository workItemRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    EntityManager entityManager;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ImmutableSet<Agent> getAllAgentsWithInProgressWorkItems() {
        return Streams.stream(agentRepository.findAll())
                .map(EntityUtil::toModel)
                .map(this::loadInProgressWorkItems)
                .collect(ImmutableSet.toImmutableSet());
    }

    public ImmutableSet<Agent> getAgentsHavingSkills(final ImmutableSet<Skill> skills) {
        return getAgentsHavingSkillIds(skills.stream()
                .map(Skill::getId).collect(ImmutableSet.toImmutableSet()));
    }

    public ImmutableSet<Agent> getAgentsHavingSkillIds(final ImmutableSet<Integer> skillIds) {
        return agentRepository.getAgentsHavingSkills(Joiner.on(",").join(skillIds)).stream()
                .map(EntityUtil::toModel)
                .collect(ImmutableSet.toImmutableSet());
    }

    public Optional<Skill> tryGetSkillByName(final String name) {
        return Optional
                .ofNullable(skillRepository.getSkillByName(name))
                .map(EntityUtil::toModel);
    }

    public ImmutableSet<WorkItem> getInProgressWorkItems() {
        return workItemRepository.findAllInProgressWorkItems().stream()
                .map(EntityUtil::toModel)
                .collect(ImmutableSet.toImmutableSet());
    }

    @Transactional(timeout = 5)
    public Optional<WorkItem> tryAssignWorkItem(final WorkItem item) {
        return tryGetAgentForWorkItem(item).map(agent -> assignWorkItem(agent, item));
    }

    public WorkItem setWorkItemCompleted(final Integer workItemId) throws NotFoundException, InvalidParameterException {
        final Optional<WorkItemDao> item = workItemRepository.findById(workItemId);
        item.map(i -> {
            if (i.getCompletedAt() != null) {
                throw new InvalidParameterException("work item already completed");
            }
            return i;
        }).orElseThrow(() -> new NotFoundException("work item was not found"));
        final Integer result = workItemRepository.setWorkItemCompleted(workItemId);
        return workItemRepository
                .findById(workItemId)
                .map(EntityUtil::toModel)
                .get();
    }

    private Optional<Agent> tryGetAgentForWorkItem(final WorkItem item) {

        final String skillIds = Joiner.on(",").join(item.getRequiredSkills().stream()
                .map(Skill::getId).iterator());

        return Optional.ofNullable(agentRepository
                .getAvailableAgentHavingSkillsForPriority(skillIds, item.getPriority().ordinal()))
                .map(EntityUtil::toModel);
    }

    private WorkItem assignWorkItem(final Agent agent, final WorkItem item) {
        final Instant now = Instant.now();
        final WorkItemDao dao = EntityUtil.toDao(item);
        dao.setAssignedAgent(EntityUtil.toDao(agent));
        dao.setCreatedAt(now);
        dao.setStartedAt(now);
        return EntityUtil.toModel(workItemRepository.save(dao));
    }

    private Agent loadInProgressWorkItems(final Agent agent) {

        final ImmutableSet<WorkItem> items  = workItemRepository
                .findInProgressWorkItemsByAgentId(agent.getId()).stream()
                .map(WorkItemDao::fromRaw)
                .map(w -> workItemRepository.findById(w.getId()).get())
                .map(EntityUtil::toModel)
                .collect(ImmutableSet.toImmutableSet());

        return agent.toBuilder().inProgressWorkItems(Optional.of(items)).build();
    }
}
