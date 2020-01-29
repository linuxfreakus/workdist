package com.example.workdist.common.data.repository;

import com.example.workdist.common.data.entity.AgentDao;
import com.example.workdist.common.data.entity.WorkItemDao;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface AgentRepository extends CrudRepository<AgentDao, Integer> {

    @Query(value = "SELECT * FROM agents_having_skills(:skillIds)", nativeQuery = true)
    Set<AgentDao> getAgentsHavingSkills(@Param("skillIds") String skillIds);

    @Query(value = "SELECT * FROM available_agents_having_skills_for_priority(:skillIds, :priority) LIMIT 1", nativeQuery = true)
    AgentDao getAvailableAgentHavingSkillsForPriority(
            @Param("skillIds") String skillIds,
            @Param("priority") Integer priority);

}