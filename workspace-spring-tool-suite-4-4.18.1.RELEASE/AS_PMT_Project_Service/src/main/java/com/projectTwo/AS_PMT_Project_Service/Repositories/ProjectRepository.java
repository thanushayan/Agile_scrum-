package com.projectTwo.AS_PMT_Project_Service.Repositories;

import com.projectTwo.AS_PMT_Project_Service.Entities.Project_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.UserRoles;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project_Entity, Long> {

	Project_Entity findByProjectId(String projectID);

	Project_Entity findActiveSprintByProjectId(String projectId);

	List<UserRoles> getUserRolesByProjectId(String projectId);

    // Custom query methods can be defined here if needed
	
}

