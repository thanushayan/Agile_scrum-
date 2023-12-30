package com.projectTwo.AS_PMT_Project_User_Service.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.projectTwo.AS_PMT_Project_User_Service.utils.PermissionUtils;
import com.projectTwo.AS_PMT_Project_User_Service.Entities.ProfileEntity;
import com.projectTwo.AS_PMT_Project_User_Service.Entities.ProjectAndUserEntity;
import com.projectTwo.AS_PMT_Project_User_Service.Entities.ProjectPermissions;
import com.projectTwo.AS_PMT_Project_User_Service.Entities.UserRoles;
import com.projectTwo.AS_PMT_Project_User_Service.Services.ProjectAndUserService;

import java.util.List;

@RestController
@RequestMapping("/api/project-and-user")
public class ProjectAndUserController {

    private final ProjectAndUserService projectAndUserService;
    private final RestTemplate restTemplate;

    @Autowired
    public ProjectAndUserController(ProjectAndUserService projectAndUserService, RestTemplateBuilder restTemplateBuilder) {
        this.projectAndUserService = projectAndUserService;
        this.restTemplate = restTemplateBuilder.build();
    }

    @PostMapping("{userId}/add")
    public ResponseEntity<?> createProjectAndUser(@PathVariable String userId, @RequestBody ProjectAndUserEntity projectAndUser) {
        try {
            ResponseEntity<ProfileEntity> userProfileResponse = restTemplate.getForEntity(
                    "http://localhost:8085/api/userprofiles/" + projectAndUser.getUserId(),
                    ProfileEntity.class
            );
            
                   
            ResponseEntity<ProjectPermissions> projectPermissions = restTemplate.getForEntity(
                    "http://localhost:8086/projectPermissions/" + projectAndUser.getProjectId(),
                    ProjectPermissions.class
                );
            
            
          
            ProjectPermissions permissions = projectPermissions.getBody();

            if (userProfileResponse.getStatusCode() == HttpStatus.NOT_FOUND || userProfileResponse.getBody() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user exists.");
            }
            
            
            ProjectAndUserEntity user = projectAndUserService.getProjectAndUserByProjectIdAndUserId(projectAndUser.getProjectId(), userId);
            if (user != null) {
                if (PermissionUtils.getInstance().isPermitted(permissions.getManageUsersPermissions(),user.getUserRole())||PermissionUtils.getInstance().isPermitted(permissions.getProjectManagementPermissions(),user.getUserRole())) {
                    ProjectAndUserEntity createdProjectAndUser = projectAndUserService.createProjectAndUser(projectAndUser);
                  
                    return ResponseEntity.status(HttpStatus.CREATED).body(createdProjectAndUser);
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authorized to add a member to this project.");
                }
            } else if (projectAndUser.getUserRole().equals("Admin")) {
                ProjectAndUserEntity createdProjectAndUser = projectAndUserService.createProjectAndUser(projectAndUser);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdProjectAndUser);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user exists.");
            }
        } catch (HttpClientErrorException.NotFound notFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error in some where");
        } catch (HttpClientErrorException httpClientException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("HTTP Client Error: " + httpClientException.getRawStatusCode() + " : " + httpClientException.getStatusText());
        } catch (RestClientException restClientException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Rest Client Exception: " + restClientException.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request: " + e.getMessage());
        }
    }

    
    @PutMapping("/{adminId}/{userId}/{projectId}/update-role")
    public ResponseEntity<?> updateProjectUserRole(
            @PathVariable String adminId, @PathVariable String userId, @PathVariable String projectId, @RequestBody ProjectAndUserEntity updatedProjectAndUser) {

        try {
            // Get the existing project and user entity for the Admin
            ProjectAndUserEntity adminProjectAndUser = projectAndUserService.getProjectAndUserByProjectIdAndUserId(projectId, adminId);

            // Check if the project and user entity exist and the user performing the update is an Admin
            if (adminProjectAndUser == null || !"Admin".equals(adminProjectAndUser.getUserRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Get the existing project and user entity for the user whose role is being updated
            ProjectAndUserEntity existingProjectAndUser = projectAndUserService.getProjectAndUserByProjectIdAndUserId(projectId, userId);

            // Check if the user whose role is being updated exists
            if (existingProjectAndUser == null) {
                return ResponseEntity.notFound().build();
            }

            // Update the user role
            existingProjectAndUser.setUserRole(updatedProjectAndUser.getUserRole());
            ProjectAndUserEntity updatedEntity = projectAndUserService.createProjectAndUser(existingProjectAndUser);

            return ResponseEntity.ok(updatedEntity);

        } catch (IllegalArgumentException illegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (RuntimeException runtimeException) {
            runtimeException.printStackTrace(); // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            // Handle other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request." + e.getMessage());
        }
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ProjectAndUserEntity>> getProjectsByUserId(@PathVariable String userId) {
        try {
            List<ProjectAndUserEntity> projects = projectAndUserService.getProjectsByUserId(userId);
            if (projects.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception for debugging purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<Void> deleteProjectAndUserByProjectId(@PathVariable String projectId) {
        try {
            projectAndUserService.deleteProjectAndUserByProjectId(projectId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception for debugging purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}/{projectId}")
    public ResponseEntity<ProjectAndUserEntity> getProjectAndUserByProjectIdAndUserId(
            @PathVariable String userId, @PathVariable String projectId) {
        try {
            ProjectAndUserEntity projectAndUser = projectAndUserService.getProjectAndUserByProjectIdAndUserId(projectId, userId);
            return ResponseEntity.ok(projectAndUser);
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception for debugging purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/get-users/{projectId}")
    public ResponseEntity<List<ProjectAndUserEntity>> getUsersByProjectId(@PathVariable String projectId) {
        try {
            List<ProjectAndUserEntity> users = projectAndUserService.getUsersByProjectId(projectId);
            if (users.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception for debugging purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    private boolean checkPermission(List<UserRoles>projectManagementPermissions, String userRole)
    {
    	boolean hasPermission = false;

    	for (UserRoles role : projectManagementPermissions) {
    		if (role.getRoleName().equals(userRole)) {
    			hasPermission = true;
    			break; 
    		}
    	}
    	return hasPermission;
    }
    

}
