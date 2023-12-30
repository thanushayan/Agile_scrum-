package com.projectTwo.AS_PMT_Project_Service.Controllers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.projectTwo.AS_PMT_Project_Service.Entities.Issue_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.ProjectAndUserEntity;
import com.projectTwo.AS_PMT_Project_Service.Entities.Project_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.ScrumBoard;
import com.projectTwo.AS_PMT_Project_Service.Entities.Sprint_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.UserRoles;
import com.projectTwo.AS_PMT_Project_Service.Services.IssueService;
import com.projectTwo.AS_PMT_Project_Service.Services.ProjectPermissionsService;
import com.projectTwo.AS_PMT_Project_Service.Entities.ProjectPermissions;
import com.projectTwo.AS_PMT_Project_Service.Services.ProjectService;
import com.projectTwo.AS_PMT_Project_Service.Services.ScrumBoardService;
import com.projectTwo.AS_PMT_Project_Service.Services.SprintService;
import com.projectTwo.AS_PMT_Project_Service.Services.UserRolesService;
import com.projectTwo.AS_PMT_Project_Service.utils.PermissionUtils;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectPermissionsService projectpermissionsService;
    private final SprintService sprintService;
    private final UserRolesService userRoleService;
    private final ScrumBoardService scrumBoardService;
    private final IssueService issueService;
    private final RestTemplate restTemplate;


    public ProjectController(ProjectService projectService,RestTemplateBuilder restTemplateBuilder,ProjectPermissionsService projectpermissionsService, UserRolesService userRolesService, SprintService sprintService, ScrumBoardService scrumBoardService, IssueService issueService) {
        this.projectService = projectService;
        this.issueService = issueService;
        this.projectpermissionsService = projectpermissionsService;
        this.userRoleService = userRolesService;
        this.sprintService = sprintService;
        this.restTemplate = restTemplateBuilder.build();
        this.scrumBoardService = scrumBoardService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody Project_Entity project) {
        try {
        	Project_Entity createdProject = projectService.createProject(project);
        	
            ProjectPermissions permission = new ProjectPermissions();
            List<UserRoles> ProjectRoles = new ArrayList<>();
            List<UserRoles> PermitRoles = new ArrayList<>();
            
            UserRoles adminRole = new UserRoles("Admin", "Administrator Role");
            UserRoles memberRole = new UserRoles("Member", "Member Role");
            UserRoles scrumMasterRole = new UserRoles("Scrum Master", "Scrum Master Role");
            UserRoles productOwnerRole = new UserRoles("Product Owner", "Product Owner Role");
            UserRoles developerRole = new UserRoles("Developer", "Developer Role");
            UserRoles qualityAnalystRole = new UserRoles("Quality Analyst", "Quality Analyst Role");

            // Checking if there are no roles, then adding the Agile roles
            if(userRoleService.getAllRoles().isEmpty()) {
                userRoleService.addRole(adminRole);
                userRoleService.addRole(memberRole);
                userRoleService.addRole(scrumMasterRole);
                userRoleService.addRole(productOwnerRole);
                userRoleService.addRole(developerRole);
                userRoleService.addRole(qualityAnalystRole);
            }
            
             
            if(userRoleService.getAllRoles().isEmpty())
            {
            	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error in role initialing");
            }

            PermitRoles.add(userRoleService.getRoleByName("Admin"));
            
            if(PermitRoles.isEmpty())
            {
            	return ResponseEntity.ok("Freek");
            }
            
            ProjectRoles.add(userRoleService.getRoleByName("Admin"));
            ProjectRoles.add(userRoleService.getRoleByName("Member"));
            ProjectRoles.add(userRoleService.getRoleByName("Scrum Master"));
            ProjectRoles.add(userRoleService.getRoleByName("Product Owner"));
            ProjectRoles.add(userRoleService.getRoleByName("Developer"));
            ProjectRoles.add(userRoleService.getRoleByName("Quality Analyst"));
            
            ProjectAndUserEntity projectAndUser = new ProjectAndUserEntity();
            projectAndUser.setUserId(project.getCreatorId());
            projectAndUser.setProjectId(project.getProjectId());
            projectAndUser.setUserRole(userRoleService.getRoleByName("Admin").getRoleName());

            permission.setProjectId(project.getProjectId());
            permission.setProjectManagementPermissions(PermitRoles);
            permission.setTaskSprintPermissions(PermitRoles);
            permission.setManageUsersPermissions(PermitRoles);
            permission.setAddCommentsPermissions(PermitRoles);
            permission.setUploadDocumentsPermissions(PermitRoles);
            permission.setViewReportsPermissions(PermitRoles);
            permission.setSystemConfigurationPermissions(PermitRoles);
            
            createdProject.setPermissions(permission);
            createdProject.setUserRoles(ProjectRoles);
            
            boolean created = !scrumBoardService.getAllScrumBoards().isEmpty();
			
			
			ScrumBoard todo = new ScrumBoard("Todo");
			ScrumBoard progress = new ScrumBoard("Progress");
			ScrumBoard done = new ScrumBoard("Done");
			
			if(!created)			
			{	
				scrumBoardService.createScrumBoard(todo);
				scrumBoardService.createScrumBoard(progress);
				scrumBoardService.createScrumBoard(done);
			}
			
			
				List<ScrumBoard> statuses = new ArrayList<>();
				statuses.add(scrumBoardService.getScrumBoardByStatusName("Todo"));
				statuses.add(scrumBoardService.getScrumBoardByStatusName("Progress"));
				statuses.add(scrumBoardService.getScrumBoardByStatusName("Done"));
				
				project.setStatus(statuses);
				projectService.updateProject(project.getProjectId(), project);
			

            if(projectpermissionsService.createProjectPermissions(permission).equals("true"))
            {
            	createdProject.setPermissions(permission);
            }
            else
            {
            	createdProject.setPermissions(permission);
            }
            
            if(permission.equals(null))
            {
            	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(projectpermissionsService.getProjectPermissionsByProjectId("Permission Not Created"));
            }
            
            createdProject = projectService.updateProject(createdProject.getProjectId(), createdProject);
            
           
            String createUserProjectUrl = "http://localhost:8087/api/project-and-user/" + project.getCreatorId() + "/add";

            try {
                restTemplate.postForEntity(createUserProjectUrl, projectAndUser, ProjectAndUserEntity.class);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
            }  catch (HttpClientErrorException httpClientException) {
                handleAPIError(createdProject);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("HTTP Client Error: " + httpClientException.getMessage());
            } catch (HttpServerErrorException httpServerErrorException) {
                handleAPIError(createdProject);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("HTTP Server Error: " + httpServerErrorException.getMessage());
            } catch (RestClientException restClientException) {
                handleAPIError(createdProject);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Rest Client Exception: " + restClientException.getMessage());
            }
        } catch (HttpClientErrorException | HttpServerErrorException httpClientException) {
            // Handling specific HTTP-related errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: HTTP Client Error occurred.");
        } catch (RestClientException restClientException) {
            // Handling generic Rest client exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Rest Client Exception occurred.");
        } catch (Exception e) {
            // Handling other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: An unexpected error occurred.");
        }
       
    }

    private void handleAPIError(Project_Entity createdProject) {
        projectService.deleteProject(createdProject.getProjectId());
    }
    
    @GetMapping("/roles/{userId}/{projectId}")
    public ResponseEntity<List <UserRoles>> getAllRoles(@PathVariable("userId") String userId, @PathVariable("projectId") String projectId) {
    	 try {
    		 String getUserProjectUrl = "http://localhost:8087/api/project-and-user/" + userId + "/" + projectId;
             ResponseEntity<ProjectAndUserEntity> response = restTemplate.getForEntity(getUserProjectUrl, ProjectAndUserEntity.class);

             if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                 return ResponseEntity.noContent().build();
             }
             
             Project_Entity project = projectService.getProjectByProjectId(projectId);
             
             if(project==null)
             {
            	 return ResponseEntity.noContent().build();
             }
             
             if(project.getUserRoles().isEmpty())
             {
            	 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
             }
             
             return ResponseEntity.ok(project.getUserRoles());
            
         } catch (HttpClientErrorException.NotFound ex) {
             return ResponseEntity.noContent().build();
         } catch (Exception e) {
             e.printStackTrace();
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
         }
        
    }
    
   
    
    @PostMapping("/roles/{userId}/{projectId}")
    public ResponseEntity<?> AddNewRoles(@PathVariable("userId") String userId, @PathVariable("projectId") String projectId , @RequestBody UserRoles userRoles) {
    	 try {
    		 String getUserProjectUrl = "http://localhost:8087/api/project-and-user/" + userId + "/" + projectId;
             ResponseEntity<ProjectAndUserEntity> response = restTemplate.getForEntity(getUserProjectUrl, ProjectAndUserEntity.class);

             if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                 return ResponseEntity.noContent().build();
             }
             
             Project_Entity project = projectService.getProjectByProjectId(projectId);
             
             if(project==null)
             {
            	 return ResponseEntity.internalServerError().body("No such project user has");
             }
             if(userRoles==null)
             {
            	 return ResponseEntity.internalServerError().body("where is roles");
             }
             
             UserRoles newRole = userRoleService.getRoleByName(userRoles.getRoleName());
             	
             if(newRole == null)
             {
            	 userRoleService.addRole(userRoles);
            	 newRole = userRoleService.getRoleByName(userRoles.getRoleName());
             }
             
             
             
             if(project.getUserRoles().contains(newRole))
             {
            	 return ResponseEntity.internalServerError().body("Already in project");
             }
              else
              {
            	  if(newRole==null)
            	  {
            		  return ResponseEntity.internalServerError().body("Not set");
            	  }
            	  project.getUserRoles().add(newRole);
            	  projectService.updateProject(projectId, project);
            	  return ResponseEntity.ok().body(newRole);
              }
              
            
         } catch (HttpClientErrorException.NotFound ex) {
        	 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
         } catch (Exception e) {
             e.printStackTrace();
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
         }
        
    }
    
    @GetMapping("/status/{userId}/{projectId}")
    public ResponseEntity<List <ScrumBoard>> getAllStatus(@PathVariable("userId") String userId, @PathVariable("projectId") String projectId) {
    	 try {
    		 String getUserProjectUrl = "http://localhost:8087/api/project-and-user/" + userId + "/" + projectId;
             ResponseEntity<ProjectAndUserEntity> response = restTemplate.getForEntity(getUserProjectUrl, ProjectAndUserEntity.class);

             if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                 return ResponseEntity.noContent().build();
             }
             
             Project_Entity project = projectService.getProjectByProjectId(projectId);
             
             if(project==null)
             {
            	 return ResponseEntity.noContent().build();
             }
             
             if(project.getStatus().isEmpty())
             {
            	 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
             }
             
             return ResponseEntity.ok(project.getStatus());
            
         } catch (HttpClientErrorException.NotFound ex) {
             return ResponseEntity.noContent().build();
         } catch (Exception e) {
             e.printStackTrace();
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
         }
        
    }
    
    @PostMapping("/status/{userId}/{projectId}")
    public ResponseEntity<?> AddNewStatus(@PathVariable("userId") String userId, @PathVariable("projectId") String projectId , @RequestBody ScrumBoard status) {
    	 try {
    		 String getUserProjectUrl = "http://localhost:8087/api/project-and-user/" + userId + "/" + projectId;
             ResponseEntity<ProjectAndUserEntity> response = restTemplate.getForEntity(getUserProjectUrl, ProjectAndUserEntity.class);

             if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                 return ResponseEntity.noContent().build();
             }
             
             Project_Entity project = projectService.getProjectByProjectId(projectId);
             
             if(project==null)
             {
            	 return ResponseEntity.internalServerError().body("No such project user has");
             }
             if(status==null)
             {
            	 return ResponseEntity.internalServerError().body("where is roles");
             }
             
            ScrumBoard newStatus = scrumBoardService.getScrumBoardByStatusName(status.getStatusName());
             	
             if(newStatus == null)
             {
            	 newStatus = new ScrumBoard(status.getStatusName());
            	 
            	 scrumBoardService.createScrumBoard(newStatus);
             }
             
             newStatus = scrumBoardService.getScrumBoardByStatusName(status.getStatusName());
             
             if(project.getStatus().contains(newStatus))
             {
            	 return ResponseEntity.internalServerError().body("Already in project");
             }
              else
              {
            	  if(newStatus==null)
            	  {
            		  return ResponseEntity.internalServerError().body("Status cannot be empty");
            	  }
            	  project.getStatus().add(newStatus);
            	  projectService.updateProject(projectId, project);
            	  return ResponseEntity.ok().body(newStatus);
              }
              
            
         } catch (HttpClientErrorException.NotFound ex) {
        	 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
         } catch (Exception e) {
             e.printStackTrace();
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
         }
        
    }


    @GetMapping("/users/{userId}/view-all")
    public ResponseEntity<List<Project_Entity>> getAllProjects(@PathVariable("userId") String userId) {
        try {
            String getUserProjectUrl = "http://localhost:8087/api/project-and-user/users/" + userId;
            ResponseEntity<ProjectAndUserEntity[]> response = restTemplate.getForEntity(getUserProjectUrl, ProjectAndUserEntity[].class);

            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.noContent().build();
            }

            ProjectAndUserEntity[] projectAndUsers = response.getBody();

            List<ProjectAndUserEntity> projectAndUserEntities = Arrays.asList(projectAndUsers);

            if (projectAndUserEntities.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<String> projectIds = projectAndUserEntities.stream()
                    .map(ProjectAndUserEntity::getProjectId)
                    .collect(Collectors.toList());

            List<Project_Entity> projects = projectService.getProjectsByIds(projectIds);
            if (projects.isEmpty()) {
                return ResponseEntity.accepted().build();
            }

            return ResponseEntity.ok(projects);
        } catch (HttpClientErrorException.NotFound ex) {
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("activeSprint/{projectId}")
    public ResponseEntity<String> updateActiveSprint(
            @PathVariable("projectId") String projectId,
            @RequestBody Sprint_Entity updatedActiveSprint) {

        try {
            if (updatedActiveSprint == null) {
                return ResponseEntity.noContent().build();
            }

            // Logic to update the active sprint
            boolean updated = projectService.updateActiveSprint(projectId, updatedActiveSprint);

            if (updated) {
                return ResponseEntity.ok("Active sprint updated successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to update the active sprint");
            }
        } catch (Exception e) {
            // Log the exception for debugging and analysis
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the active sprint.");
        }
    }
    
    @GetMapping("/pokerIssue/{projectId}")
    public ResponseEntity<Issue_Entity> getPokerIssuesByProjectId(@PathVariable String projectId) {
        try {
            Project_Entity project = projectService.getProjectByProjectId(projectId);

            if (project == null) {
                return ResponseEntity.noContent().build();
            }

         
            Issue_Entity pokerIssues = projectService.getPokerIssuesByProjectId(projectId);

            if (pokerIssues==null) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(pokerIssues);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("pokerIssue/{projectId}/{issueId}")
    public ResponseEntity<String> updatePokerIssue(
            @PathVariable("projectId") String projectId, 
            @PathVariable("issueId") Long issueId) {

        try {
        	Issue_Entity updatedPokerIssue = issueService.getIssueById(issueId);
        	
            if (updatedPokerIssue == null) {
                return ResponseEntity.noContent().build();
            }

            // Logic to update the active sprint
            boolean updated = projectService.updatePokerIssue(projectId, updatedPokerIssue);

            if (updated) {
                return ResponseEntity.ok("Active sprint updated successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to update the active sprint");
            }
        } catch (Exception e) {
            // Log the exception for debugging and analysis
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the active sprint.");
        }
    }



    @GetMapping("/users/{userId}/search/{projectId}")
    public ResponseEntity<Project_Entity> getProjectById(@PathVariable String userId, @PathVariable String projectId) {
        try {
        	String getUserProjectUrl = "http://localhost:8087/api/project-and-user/" + userId + "/" + projectId;
            // Check if the project belongs to the user 
            ResponseEntity<ProjectAndUserEntity> projectAndUser = restTemplate.getForEntity(getUserProjectUrl,ProjectAndUserEntity.class);
            if (projectAndUser.getBody() == null) {
                return ResponseEntity.noContent().build();
            }
            
            // Retrieve the project details
            Project_Entity project = projectService.getProjectByProjectId(projectId);
            if (project == null) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(project);
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.noContent().build();
        }
    }
    
    @GetMapping("/{userId}/{projectId}/activeSprint")
    public ResponseEntity<Sprint_Entity> getActiveSprintById(@PathVariable String userId, @PathVariable String projectId) {
        try {
        	String getUserProjectUrl = "http://localhost:8087/api/project-and-user/" + userId + "/" + projectId;
            // Check if the project belongs to the user 
            ResponseEntity<ProjectAndUserEntity> projectAndUser = restTemplate.getForEntity(getUserProjectUrl,ProjectAndUserEntity.class);
            if (projectAndUser.getBody() == null) {
                return ResponseEntity.noContent().build();
            }
            
            // Retrieve the project details
            Project_Entity project = projectService.getProjectByProjectId(projectId);
            if (project == null) {
                return ResponseEntity.noContent().build();
            }
            
            Sprint_Entity activeSprint = projectService.getActiveSprintByProject(project);
            
            return ResponseEntity.ok(activeSprint);
            
        } catch (NoSuchElementException e) {
            return ResponseEntity.noContent().build();
        }
    }


    @PutMapping("/update/{userId}/{projectId}")
    public ResponseEntity<Project_Entity> updateProjectDetail(@PathVariable String userId, @PathVariable String projectId, @RequestBody Project_Entity project) throws ParseException {
        try {
        	String getUserProjectUrl = "http://localhost:8087/api/project-and-user/" + userId + "/" + projectId;
            // Check if the project belongs to the user 
            ResponseEntity<ProjectAndUserEntity> projectAndUser = restTemplate.getForEntity(getUserProjectUrl,ProjectAndUserEntity.class);
            if (projectAndUser.getBody() == null) {
                return ResponseEntity.noContent().build();
            }
            
            Project_Entity existingProject = projectService.getProjectByProjectId(projectId);
            if(existingProject.equals(null))
            {
            	return ResponseEntity.noContent().build();
            }
            
            if(!PermissionUtils.getInstance().isPermitted(existingProject.getPermissions().getProjectManagementPermissions(), projectAndUser.getBody().getUserRole()))
            {
            	return ResponseEntity.noContent().build();
            }
            
            Project_Entity updatedProject = projectService.updateProject(projectId, project);
            if (updatedProject != null) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedProject);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/delete/{userId}/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable String userId, @PathVariable String projectId) {
    	String getUserProjectUrl = "http://localhost:8087/api/project-and-user/" + userId + "/" + projectId;
        // Check if the project belongs to the user 
        ResponseEntity<ProjectAndUserEntity> projectAndUser = restTemplate.getForEntity(getUserProjectUrl,ProjectAndUserEntity.class);
        if (projectAndUser.getBody() == null) {
            return ResponseEntity.noContent().build();
        }
        
        Project_Entity existingProject = projectService.getProjectByProjectId(projectId);
        
        if(!PermissionUtils.getInstance().isPermitted(existingProject.getPermissions().getProjectManagementPermissions(), projectAndUser.getBody().getUserRole()))
        {
        	return ResponseEntity.noContent().build();
        }
        
        if(existingProject.equals(null))
        {
        	return ResponseEntity.noContent().build();
        }
        
        if(!PermissionUtils.getInstance().isPermitted(existingProject.getPermissions().getProjectManagementPermissions(), projectAndUser.getBody().getUserRole()))
        {
        	return ResponseEntity.noContent().build();
        }
    	String getProjectsUrl = "http://localhost:8087/api/project-and-user/delete/" + projectId;
    	
        projectService.deleteProject(projectId);
        restTemplate.delete(getProjectsUrl);
        
        return ResponseEntity.noContent().build();
    }
}
