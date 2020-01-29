package com.example.workdist.common.data.repository;

import com.example.workdist.common.data.entity.SkillDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SkillRepository extends CrudRepository<SkillDao, Integer> {

    @Query("SELECT s FROM SkillDao s WHERE s.name = :name")
    SkillDao getSkillByName(@Param("name") String name);

}