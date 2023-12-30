package com.projectTwo.AS_PMT_Project_Service.Controllers;

import com.projectTwo.AS_PMT_Project_Service.Entities.ProjectPermissions;
import com.projectTwo.AS_PMT_Project_Service.Services.ProjectPermissionsService;
import com.projectTwo.AS_PMT_Project_Service.Services.UserRolesService;
import com.projectTwo.AS_PMT_Project_Service.Entities.UserRoles;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projectPermissions")
public class ProjectPermissionsController {

    private final ProjectPermissionsService projectPermissionsService;
    private final UserRolesService userRoleService;

    public ProjectPermissionsController(ProjectPermissionsService projectPermissionsService, UserRolesService userRoleService) {
        this.projectPermissionsService = projectPermissionsService;
        this.userRoleService = userRoleService;
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectPermissions(@PathVariable String projectId) {
        try {
            if (projectId == null || projectId.isEmpty()) {
                return ResponseEntity.badRequest().body("Project ID is required.");
            }

            ProjectPermissions projectPermissions = projectPermissionsService.getProjectPermissionsByProjectId(projectId);
            if (projectPermissions == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project Permissions not found for the provided ID: " + projectId);
            }
            return ResponseEntity.ok(projectPermissions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> createProjectPermissionS(@RequestBody ProjectPermissions permissions) {
        try {
            ProjectPermissions existingPermissions = projectPermissionsService.getProjectPermissionsByProjectId(permissions.getProjectId());

            if (existingPermissions == null) {
            	String outPut =  projectPermissionsService.createProjectPermissions(permissions);
                boolean created =outPut.equals("true");
                if (created) {
                    return ResponseEntity.ok(permissions); // Successfully created
                } else {
                    return ResponseEntity.badRequest().body("Failed to create project permissions " + outPut); // Provide a message for failed creation
                }
            } else {
                return ResponseEntity.badRequest().body("Project permissions already exist for the provided ID"); // Inform that permissions already exist
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
     }
    
    @PutMapping("/update")
    public ResponseEntity<String> updateProjectPermissions(@RequestBody ProjectPermissions permissions) {
        try {
            // Retrieve existing permissions from the database based on the provided project ID
            ProjectPermissions existingPermissions = projectPermissionsService.getProjectPermissionsByProjectId(permissions.getProjectId());
            
            if (existingPermissions == null) {
                return ResponseEntity.badRequest().body("Permissions not found for the provided project ID");
            }
            
//            if(existingPermissions.getManageUsersPermissions().contains())

            // Update the existing permissions with the new data
            existingPermissions.setProjectManagementPermissions(permissions.getProjectManagementPermissions());
            existingPermissions.setTaskSprintPermissions(permissions.getTaskSprintPermissions());
            existingPermissions.setManageUsersPermissions(permissions.getManageUsersPermissions());
            existingPermissions.setAddCommentsPermissions(permissions.getAddCommentsPermissions());
            existingPermissions.setUploadDocumentsPermissions(permissions.getUploadDocumentsPermissions());
            existingPermissions.setViewReportsPermissions(permissions.getViewReportsPermissions());
            existingPermissions.setSystemConfigurationPermissions(permissions.getSystemConfigurationPermissions());

            // Perform the update operation
            String updated = projectPermissionsService.updateProjectPermissions(existingPermissions);

            if (updated.equals("true")) {
                return ResponseEntity.ok("Permissions updated successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to update project permissions");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }


    @PostMapping("/create/{projectId}/{permissionType}/{userId}")
    public ResponseEntity<String> createProjectPermission(
            @PathVariable String projectId,
            @PathVariable String userId,
            @PathVariable String permissionType,
            @RequestBody UserRoles userRole
    ) {
        try {
            if (userId == null || userId.isEmpty()) {
                return ResponseEntity.badRequest().body("User ID is required.");
            }

            ProjectPermissions projectPermissions = projectPermissionsService.getProjectPermissionsByProjectId(projectId);

            if (projectPermissions == null) {
                // If projectPermissions is null, create a new ProjectPermissions object
                projectPermissions = new ProjectPermissions();
                
                // Set the projectId for the new ProjectPermissions object
                projectPermissions.setProjectId(projectId);
                
                // Create new lists for different permissions if required
                // Example:
                projectPermissions.setProjectManagementPermissions(new ArrayList<>());
                projectPermissions.setTaskSprintPermissions(new ArrayList<>());
                // ... set other permission lists similarly
                
                // Save or update the new ProjectPermissions object in the database
                projectPermissionsService.createProjectPermissions(projectPermissions);
            }


            if (userRole == null) {
                return ResponseEntity.badRequest().body("User Role data is required.");
            }
            
            UserRoles userRoleObj = null;
            
            for (UserRoles userRoles : userRoleService.getAllRoles())
            {
            	if(userRoles.getRoleName().equals(userRole.getRoleName()))
            	{
            		userRoleObj = userRoles;
            	}
            }
            
            if (!userRoleService.getAllRoles().contains(userRoleObj))
            {
            	return ResponseEntity.badRequest().body("User Role data Not Exist.");
            }

            // Use a switch case to determine the permission type
            switch (permissionType) {
                case "projectManagementPermissions":
                	if(projectPermissions.getProjectManagementPermissions().contains(userRoleObj))
                	{
                		return ResponseEntity.badRequest().body("Already in"); 
                	}
                    projectPermissions.getProjectManagementPermissions().add(userRoleObj);
                    break;
                case "taskSprintPermissions":
                	if(projectPermissions.getTaskSprintPermissions().contains(userRoleObj))
                	{
                		return ResponseEntity.badRequest().body("Already in"); 
                	}
                    projectPermissions.getTaskSprintPermissions().add(userRoleObj);
                    break;
                case "manageUsersPermissions":
                	if(projectPermissions.getManageUsersPermissions().contains(userRoleObj))
                	{
                		return ResponseEntity.badRequest().body("Already in"); 
                	}
                    projectPermissions.getManageUsersPermissions().add(userRoleObj);
                    break;
                case "addCommentsPermissions":
                	if(projectPermissions.getAddCommentsPermissions().contains(userRoleObj))
                	{
                		return ResponseEntity.badRequest().body("Already in"); 
                	}
                    projectPermissions.getAddCommentsPermissions().add(userRoleObj);
                    break;
                case "uploadDocumentsPermissions":
                	if(projectPermissions.getUploadDocumentsPermissions().contains(userRoleObj))
                	{
                		return ResponseEntity.badRequest().body("Already in"); 
                	}
                    projectPermissions.getUploadDocumentsPermissions().add(userRoleObj);
                    break;
                case "viewReportsPermissions":
                	if(projectPermissions.getViewReportsPermissions().contains(userRoleObj))
                	{
                		return ResponseEntity.badRequest().body("Already in"); 
                	}
                    projectPermissions.getViewReportsPermissions().add(userRoleObj);
                    break;
                case "systemConfigurationPermissions":
                	if(projectPermissions.getSystemConfigurationPermissions().contains(userRoleObj))
                	{
                		return ResponseEntity.badRequest().body("Already in"); 
                	}
                    projectPermissions.getSystemConfigurationPermissions().add(userRoleObj);
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid permission type");
            }

            String updated = projectPermissionsService.updateProjectPermissions(projectPermissions);
            if (updated.equals("true")) {
                return new ResponseEntity<>("Permission added successfully", HttpStatus.OK);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/update/{projectId}/{permissionType}/{userId}")
    public ResponseEntity<String> updateProjectPermission(
            @PathVariable String projectId,
            @PathVariable String userId,
            @PathVariable String permissionType,
            @RequestBody UserRoles updatedUserRole
    ) {
        try {
            if (userId == null || userId.isEmpty()) {
                return ResponseEntity.badRequest().body("User ID is required.");
            }

            ProjectPermissions projectPermissions = projectPermissionsService.getProjectPermissionsByProjectId(projectId);
            if (projectPermissions == null) {
                return ResponseEntity.notFound().build();
            }

            List<UserRoles> userRolesList;
            switch (permissionType) {
                case "projectManagementPermissions":
                    userRolesList = projectPermissions.getProjectManagementPermissions();
                    break;
                case "taskSprintPermissions":
                    userRolesList = projectPermissions.getTaskSprintPermissions();
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid permission type");
            }

            if (updatedUserRole == null || updatedUserRole.getId() == null || updatedUserRole.getId() == 0L) {
                return ResponseEntity.badRequest().body("Invalid or empty UserRoles data.");
            }

            boolean updated = false;
            for (UserRoles userRole : userRolesList) {
                if (userRole.getId().equals(updatedUserRole.getId())) {
                    userRole.setRoleName(updatedUserRole.getRoleName());
                    userRole.setDescription(updatedUserRole.getDescription());
                    updated = true;
                    break;
                }
            }

            if (updated) {
                String isUpdated = projectPermissionsService.updateProjectPermissions(projectPermissions);
                if (isUpdated.equals("true")) {
                    return new ResponseEntity<>("Permission updated successfully", HttpStatus.OK);
                }
            }

            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    // Other operations such as delete can be added similarly
}

