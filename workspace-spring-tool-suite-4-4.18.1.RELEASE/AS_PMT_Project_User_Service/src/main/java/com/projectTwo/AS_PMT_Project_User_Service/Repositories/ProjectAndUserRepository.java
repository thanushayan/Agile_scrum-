package com.projectTwo.AS_PMT_Project_User_Service.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectTwo.AS_PMT_Project_User_Service.Entities.ProjectAndUserEntity;

@Repository
public interface ProjectAndUserRepository extends JpaRepository<ProjectAndUserEntity, Long> {
	List<ProjectAndUserEntity> findByUserId(String userId);

	List<ProjectAndUserEntity> findByProjectId(String projectId);

	ProjectAndUserEntity findByProjectIdAndUserId(String projectId, String userId);
}
