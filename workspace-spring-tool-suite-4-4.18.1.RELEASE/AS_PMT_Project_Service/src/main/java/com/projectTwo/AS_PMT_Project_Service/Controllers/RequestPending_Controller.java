package com.projectTwo.AS_PMT_Project_Service.Controllers;

import com.projectTwo.AS_PMT_Project_Service.Entities.RequestPending_Entity;
import com.projectTwo.AS_PMT_Project_Service.Services.RequestPendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request-pending")
public class RequestPending_Controller {

    private final RequestPendingService requestPendingService;

    public RequestPending_Controller(RequestPendingService requestPendingService) {
        this.requestPendingService = requestPendingService;
    }

    // Get requestPending with username
    @GetMapping("/by-username/{username}")
    public ResponseEntity<List<RequestPending_Entity>> getRequestPendingByUsername(@PathVariable String username) {
        try {
            List<RequestPending_Entity> requestPendingList = requestPendingService.getRequestPendingByUsername(username);
            return ResponseEntity.ok(requestPendingList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Get requestPending with project ID
    @GetMapping("/by-project-id/{projectId}")
    public ResponseEntity<List<RequestPending_Entity>> getRequestPendingByProjectId(@PathVariable String projectId) {
        try {
            List<RequestPending_Entity> requestPendingList = requestPendingService.getRequestPendingByProjectId(projectId);
            return ResponseEntity.ok(requestPendingList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Create requestPending
    @PostMapping("/create")
    public ResponseEntity<RequestPending_Entity> createRequestPending(@RequestBody RequestPending_Entity requestPending) {
        try {
            RequestPending_Entity createdRequestPending = requestPendingService.createRequestPending(requestPending);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequestPending);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @DeleteMapping("/delete/{projectId}/{userId}")
    public ResponseEntity<String> deleteRequestPendingByProjectIdAndUserId(@PathVariable String projectId, @PathVariable String userId) {
        try {
            requestPendingService.deleteRequestPendingByProjectIdAndUserId(projectId, userId);
            return ResponseEntity.ok("RequestPending deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete RequestPending");
        }
    }
}
