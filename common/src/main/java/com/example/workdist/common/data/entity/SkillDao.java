package com.example.workdist.common.data.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
@Table(name = "skills")
public class SkillDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @Column(length = 64)
    private String name;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
