package com.example.workdist.common.data.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Data
@Entity
@Table(name = "agents")
public class AgentDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @Column(length = 64)
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="agent_skill_map", joinColumns={@JoinColumn(name ="agent_id", referencedColumnName ="id")},
            inverseJoinColumns={@JoinColumn(name ="skill_id", referencedColumnName ="id")})
    private Set<SkillDao> skills;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
