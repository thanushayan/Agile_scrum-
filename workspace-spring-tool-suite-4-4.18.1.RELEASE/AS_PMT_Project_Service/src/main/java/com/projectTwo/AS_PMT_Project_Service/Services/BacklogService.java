package com.projectTwo.AS_PMT_Project_Service.Services;

import com.projectTwo.AS_PMT_Project_Service.Entities.Backlog_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.Issue_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.Project_Entity;
import com.projectTwo.AS_PMT_Project_Service.Repositories.BacklogRepository;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BacklogService {
    private final BacklogRepository backlogRepository;

    @Autowired
    public BacklogService(BacklogRepository backlogRepository) {
        this.backlogRepository = backlogRepository;
    }
    public Backlog_Entity createBacklog(Backlog_Entity backlog) {
        return backlogRepository.save(backlog);
    }
    
    public Backlog_Entity getByProjectId(String projectId) {
        return backlogRepository.findByProjectId(projectId);
    }
    
    public Backlog_Entity updateBacklog(String projectId, Backlog_Entity updatedBacklog) {
        Backlog_Entity existingBacklog = backlogRepository.findByProjectId(projectId);
        if (existingBacklog == null) {
            throw new NoSuchElementException("No backlog found with the specified project ID");
        }

        // Update the backlog properties with the new values
        existingBacklog.setIssues(updatedBacklog.getIssues());

        // Save the updated backlog in the database
        return backlogRepository.save(existingBacklog);
    }
}
