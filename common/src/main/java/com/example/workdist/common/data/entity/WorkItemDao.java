package com.example.workdist.common.data.entity;

import com.example.workdist.common.enums.Priority;
import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "work_items")
public class WorkItemDao implements Serializable {
    private static final long serialVersionUID = 1L;
    public static WorkItemDao fromRaw(final Object[] raw) {
        WorkItemDao dao = new WorkItemDao();
        dao.setId((Integer) raw[0]);
        dao.setTitle((String) raw[1]);
        dao.setPriority(Priority.values()[(Integer) raw[2]]);
        dao.setStartedAt(EntityUtil.fromNullableRaw((Date) raw[3]));
        dao.setCompletedAt(EntityUtil.fromNullableRaw((Date) raw[4]));
        dao.setCreatedAt(EntityUtil.fromNullableRaw((Date) raw[5]));
        dao.setUpdatedAt(EntityUtil.fromNullableRaw((Date) raw[6]));
        return dao;
    }

    @Id
    @SequenceGenerator(name="work_items_sequence",sequenceName="work_items_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="work_items_sequence")
    @Generated(GenerationTime.INSERT)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title", length = 64)
    private String title;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "priority")
    private Priority priority;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="work_item_skill_map", joinColumns={@JoinColumn(name ="work_item_id", referencedColumnName ="id")},
            inverseJoinColumns={@JoinColumn(name ="skill_id", referencedColumnName ="id")})
    private Set<SkillDao> requiredSkills;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name="work_item_agent_map", joinColumns={@JoinColumn(name ="work_item_id", referencedColumnName ="id")},
            inverseJoinColumns={@JoinColumn(name ="agent_id", referencedColumnName ="id")})
    private AgentDao assignedAgent;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
