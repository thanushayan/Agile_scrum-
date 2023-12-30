package com.projectTwo.AS_PMT_Project_Service.Repositories;

import com.projectTwo.AS_PMT_Project_Service.Entities.RequestPending_Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestPendingRepository extends JpaRepository<RequestPending_Entity, Long> {

    List<RequestPending_Entity> findByUserName(String userName);

    Optional<RequestPending_Entity> deleteByProjectIdAndUserName(String projectId, String userName);
    
    List<RequestPending_Entity> findByProjectId(String projectId);

	Optional<RequestPending_Entity> findByProjectIdAndUserName(String projectId, String userName);
}
