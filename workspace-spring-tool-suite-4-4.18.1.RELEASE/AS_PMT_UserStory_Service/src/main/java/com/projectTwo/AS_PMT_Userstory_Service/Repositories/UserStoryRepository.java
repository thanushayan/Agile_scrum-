package com.projectTwo.AS_PMT_Userstory_Service.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectTwo.AS_PMT_Userstory_Service.Entities.Userstory_Entity;

public interface UserStoryRepository extends JpaRepository<Userstory_Entity, Long> {
    List<Userstory_Entity> findByProjectId(String projectId);
    List<Userstory_Entity> findBySprintId(String sprintId);
    Userstory_Entity findByUserStoryId(String userStoryId);
}
