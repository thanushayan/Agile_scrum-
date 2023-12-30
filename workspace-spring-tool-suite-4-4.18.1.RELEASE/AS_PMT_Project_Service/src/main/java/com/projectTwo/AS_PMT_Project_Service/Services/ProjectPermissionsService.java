package com.projectTwo.AS_PMT_Project_Service.Services;

import com.projectTwo.AS_PMT_Project_Service.Entities.ProjectPermissions;
import com.projectTwo.AS_PMT_Project_Service.Entities.UserRoles;
import com.projectTwo.AS_PMT_Project_Service.Repositories.ProjectPermissionsRepository;

import jakarta.persistence.PersistenceException;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ProjectPermissionsService {

    private final ProjectPermissionsRepository projectPermissionsRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProjectPermissionsService.class);
    @Autowired
    public ProjectPermissionsService(ProjectPermissionsRepository projectPermissionsRepository) {
        this.projectPermissionsRepository = projectPermissionsRepository;
    }

    public ProjectPermissions getProjectPermissionsByProjectId(String projectId) {
        return projectPermissionsRepository.findByProjectId(projectId);
    }

    public String updateProjectPermissions(ProjectPermissions permissions) {
        try {
            // Save the updated permissions using the repository's save method
            projectPermissionsRepository.save(permissions);
            return "true"; // Return true to indicate successful update
        } catch (Exception e) {
            // Log or handle the exception
            e.printStackTrace();
            return "false"; // Return false in case of any exception or error
        }
    }


    public String createProjectPermissions(ProjectPermissions newProjectPermissions) {
        try
        {
            projectPermissionsRepository.save(newProjectPermissions);
            return "true";
        }
        catch (Exception e)
        {
        	return e.getMessage();
        }
    }


    
    public List<UserRoles> getProjectManagementPermissions(String projectId)
    {
    	return projectPermissionsRepository.findByProjectId(projectId).getProjectManagementPermissions();
    	 
    }
}
