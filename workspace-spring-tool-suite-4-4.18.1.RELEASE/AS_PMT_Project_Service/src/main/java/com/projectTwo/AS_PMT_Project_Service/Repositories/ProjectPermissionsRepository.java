package com.projectTwo.AS_PMT_Project_Service.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectTwo.AS_PMT_Project_Service.Entities.ProjectPermissions;
import com.projectTwo.AS_PMT_Project_Service.Entities.UserRoles;

@Repository
public interface ProjectPermissionsRepository extends JpaRepository<ProjectPermissions, Long> {

	ProjectPermissions findByProjectId(String projectId);
    // You can add custom queries or methods related to ProjectPermissions here if needed

}

