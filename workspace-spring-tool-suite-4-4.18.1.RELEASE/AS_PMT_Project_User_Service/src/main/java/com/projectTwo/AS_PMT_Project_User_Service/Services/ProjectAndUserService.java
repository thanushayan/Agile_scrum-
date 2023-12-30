package com.projectTwo.AS_PMT_Project_User_Service.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectTwo.AS_PMT_Project_User_Service.Entities.ProjectAndUserEntity;
import com.projectTwo.AS_PMT_Project_User_Service.Repositories.ProjectAndUserRepository;
import com.projectTwo.AS_PMT_Project_User_Service.utils.DateTimeGetterUtil;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectAndUserService {

    private final ProjectAndUserRepository projectAndUserRepository;

    @Autowired
    public ProjectAndUserService(ProjectAndUserRepository projectAndUserRepository) {
        this.projectAndUserRepository = projectAndUserRepository;
    }

    public List<ProjectAndUserEntity> getAllProjectAndUsers() {
        return projectAndUserRepository.findAll();
    }

    public ProjectAndUserEntity createProjectAndUser(ProjectAndUserEntity projectAndUser) {
    	projectAndUser.setAddedDate(new DateTimeGetterUtil("ITC").getCurrent());
        return projectAndUserRepository.save(projectAndUser);
    }
    
    public List<ProjectAndUserEntity> getProjectsByUserId(String userId) {
        return projectAndUserRepository.findByUserId(userId);
    }
    
    public ProjectAndUserEntity getProjectAndUserByProjectIdAndUserId(String projectId, String userId) {
        
        return projectAndUserRepository.findByProjectIdAndUserId(projectId, userId);
    }
    
    
    public List<ProjectAndUserEntity> getUsersByProjectId(String projectId) {
        return projectAndUserRepository.findByProjectId(projectId);
    }


    
    public void deleteProjectAndUserByProjectId(String projectId) {
        List<ProjectAndUserEntity> projectAndUsers = projectAndUserRepository.findByProjectId(projectId);
        for (ProjectAndUserEntity projectAndUser : projectAndUsers) {
            projectAndUserRepository.delete(projectAndUser);
        }
    }

	
}
