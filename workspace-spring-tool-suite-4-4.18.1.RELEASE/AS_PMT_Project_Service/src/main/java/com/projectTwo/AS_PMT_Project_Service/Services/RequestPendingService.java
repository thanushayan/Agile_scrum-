package com.projectTwo.AS_PMT_Project_Service.Services;

import com.projectTwo.AS_PMT_Project_Service.Entities.RequestPending_Entity;
import com.projectTwo.AS_PMT_Project_Service.Repositories.RequestPendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestPendingService {

    private final RequestPendingRepository requestPendingRepository;

    @Autowired
    public RequestPendingService(RequestPendingRepository requestPendingRepository) {
        this.requestPendingRepository = requestPendingRepository;
    }

    // Retrieve requestPending by username
    public List<RequestPending_Entity> getRequestPendingByUsername(String username) {
        // Implement logic to fetch requestPending by username from the repository
        return requestPendingRepository.findByUserName(username);
    }

    // Retrieve requestPending by project ID
    public List<RequestPending_Entity> getRequestPendingByProjectId(String projectId) {
        // Implement logic to fetch requestPending by projectId from the repository
        return requestPendingRepository.findByProjectId(projectId);
    }

    // Create requestPending
    public RequestPending_Entity createRequestPending(RequestPending_Entity requestPending) {
 
        // Implement logic to create requestPending in the repository
        return requestPendingRepository.save(requestPending);
    }
    
    public void deleteRequestPendingByProjectIdAndUserId(String projectId, String userId) {
        // Find the entity that matches both projectId and userId
        Optional<RequestPending_Entity> requestPendingEntity = requestPendingRepository.findByProjectIdAndUserName(projectId, userId);

        // Check if the entity exists before deleting
        requestPendingEntity.ifPresent(entity -> {
            requestPendingRepository.delete(entity);
        });
    }


}
