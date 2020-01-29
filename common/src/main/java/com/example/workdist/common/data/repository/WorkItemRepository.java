package com.example.workdist.common.data.repository;

import com.example.workdist.common.data.entity.AgentDao;
import com.example.workdist.common.data.entity.WorkItemDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface WorkItemRepository extends CrudRepository<WorkItemDao, Integer> {

    @Query("SELECT w FROM WorkItemDao w WHERE w.completedAt IS NULL")
    Set<WorkItemDao> findAllInProgressWorkItems();

    @Query(value = "SELECT id, title, priority, started_at, completed_at, created_at, updated_at FROM in_progress_work_items_for_agent(:agentId)", nativeQuery = true)
    List<Object[]> findInProgressWorkItemsByAgentId(@Param("agentId") Integer agentId);

    @Modifying
    @Transactional(timeout = 5)
    @Query("UPDATE WorkItemDao w SET w.completedAt = CURRENT_TIMESTAMP WHERE w.id = :workItemId")
    Integer setWorkItemCompleted(@Param("workItemId") Integer workItemId);
}