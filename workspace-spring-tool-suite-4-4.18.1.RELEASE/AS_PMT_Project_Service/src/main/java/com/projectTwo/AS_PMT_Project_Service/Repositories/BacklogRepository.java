package com.projectTwo.AS_PMT_Project_Service.Repositories;

import com.projectTwo.AS_PMT_Project_Service.Entities.Backlog_Entity;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends JpaRepository<Backlog_Entity, Long> {
    Backlog_Entity findByProjectId(String projectId);
}
