package com.projectTwo.AS_PMT_Project_Service.Controllers;

import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.projectTwo.AS_PMT_Project_Service.Entities.ProjectAndUserEntity;
import com.projectTwo.AS_PMT_Project_Service.Entities.Project_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.Sprint_Entity;
import com.projectTwo.AS_PMT_Project_Service.Services.ProjectService;
import com.projectTwo.AS_PMT_Project_Service.Services.SprintService;


@RestController
@RequestMapping("/sprints")
public class SprintController {
	private final SprintService sprintService;
	private final ProjectService projectService;
	private final RestTemplate restTemplate;
	
	
	    public SprintController(SprintService sprintService, ProjectService projectService, RestTemplateBuilder restTemplateBuilder) {
	        this.sprintService = sprintService;
	        this.projectService = projectService;
	        this.restTemplate = restTemplateBuilder.build();
	    }

	    @PostMapping(value = "/create-sprint/{userId}/add/{projectId}")
	    public ResponseEntity<?> createSprint(@RequestBody Sprint_Entity sprint, @PathVariable("userId") String userId, @PathVariable("projectId") String projectId) {
	        try {
	            // Validate project existence
	            String getUserProfileUrl = "http://localhost:8087/api/project-and-user/" + userId + "/" + projectId;
	            
	            // Check if the project belongs to the user
	            ResponseEntity<ProjectAndUserEntity> projectAndUser = restTemplate.getForEntity(getUserProfileUrl, ProjectAndUserEntity.class);
	            
	            if (projectAndUser.getBody()==null) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unauthorized request");
	            }

	            // Retrieve the project details
	            Project_Entity project = projectService.getProjectByProjectId(projectId);
	            
	            if (project == null) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
	            }
	            
	            // Create the sprint
	            Sprint_Entity createdSprint = sprintService.createSprint(sprint, projectId);
	            
	            // Add the created sprint to the project's sprint list
	            project.getSprints().add(createdSprint);
	            
	            // Update the project in the database
	            projectService.updateProject(project.getProjectId(), project);
	            
	            return ResponseEntity.status(HttpStatus.CREATED).body(createdSprint);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
	        }
	    }

	    @GetMapping(value = "get/{userId}/{projectId}")
	    public ResponseEntity<?> getSprintByProjectAndUser(@PathVariable("userId") String userId, @PathVariable("projectId") String projectId) {
	        try {
	            // Validate project existence
	            String getUserProfileUrl = "http://localhost:8087/api/project-and-user/" + userId + "/" + projectId;

	            // Check if the project belongs to the user
	            ResponseEntity<ProjectAndUserEntity> projectAndUser = restTemplate.getForEntity(getUserProfileUrl, ProjectAndUserEntity.class);

	            if (projectAndUser.getStatusCode() != HttpStatus.OK) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized request");
	            }

	            // Retrieve the sprint details
	            List<Sprint_Entity> sprint = sprintService.getSprintByProjectId(projectId);

	            if (sprint.isEmpty()) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sprint not found");
	            }

	            return ResponseEntity.ok(sprint);
	        } catch (NoSuchElementException e) {
	            // Handling NoSuchElementException, typically thrown when the projectId or userId is invalid
	            return ResponseEntity.badRequest().body("Invalid projectId or userId");
	        } catch (HttpClientErrorException e) {
	            // Handling HttpClientErrorException, typically thrown for 4xx HTTP status codes
	            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found");
	            } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized request");
	            } else {
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
	            }
	        } catch (Exception e) {
	            // Handling any other unexpected exception
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
	        }
	    }



	    @GetMapping(value = "search/{sprintId}")
	    public ResponseEntity<?> getSprintById(@PathVariable String sprintId) {
	        try {
	            Sprint_Entity sprint = sprintService.getSprintBySprintId(sprintId);
	            if (sprint == null) {
	                // Sprint not found
	                return ResponseEntity.notFound().build();
	            } else {
	                // Sprint found, you can perform further operations or return the sprint
	                return ResponseEntity.ok(sprint);
	            }
	        } catch (NoSuchElementException e) {
	            // Handling NoSuchElementException, typically thrown when the sprintId is invalid
	            return ResponseEntity.badRequest().body("Invalid sprintId");
	        } catch (Exception e) {
	            // Handling any other unexpected exception
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
	        }
	    }

	    
	    @PostMapping(value = "update/{sprintId}")
	    public ResponseEntity<?> updateSprintDetail(@PathVariable("projectID") String projectID, @PathVariable String sprintId, @RequestBody Sprint_Entity sprint) throws ParseException {
	        try {
	            Sprint_Entity updatedSprint = sprintService.updateSprint(sprintId, sprint);
	            if (updatedSprint == null) {
	                // Sprint not found or update failed
	                return ResponseEntity.notFound().build();
	            } else {
	                // Sprint updated successfully
	                return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedSprint);
	            }
	        } catch (NoSuchElementException e) {
	            // Handling NoSuchElementException, typically thrown when the sprintId is invalid
	            return ResponseEntity.badRequest().body("Invalid sprintId");
	        } catch (ParseException e) {
	            // Handling ParseException if there is an issue with date parsing
	            return ResponseEntity.badRequest().body("Invalid date format");
	        } catch (Exception e) {
	            // Handling any other unexpected exception
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
	        }
	    }

	    
	   

	    @DeleteMapping(value = "delete/{userId}/{sprintId}")
	    public ResponseEntity<?> deleteSprint(@PathVariable("userId") String userId, @PathVariable("sprintId") String sprintId) {
	        try {

	            // Get the sprint by sprintId
	            Sprint_Entity sprint = sprintService.getSprintBySprintId(sprintId);
	            
	            if (sprint == null) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sprint not found");
	            }
	            
	            String projectID = sprint.getProjectId();
	            // Validate project existence
	            String getUserProfileUrl = "http://localhost:8087/api/project-and-user/" + userId + "/" + projectID;

	            // Check if the project belongs to the user
	            ResponseEntity<ProjectAndUserEntity> projectAndUser = restTemplate.getForEntity(getUserProfileUrl, ProjectAndUserEntity.class);

	            if (projectAndUser.getBody() == null) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unauthorized request");
	            }

	            // Delete the sprint
	            sprintService.deleteSprint(sprintId, projectID);
	            return ResponseEntity.noContent().build();
	        } catch (NoSuchElementException e) {
	            // Handling NoSuchElementException, typically thrown when the sprintId is invalid or the sprint doesn't exist
	            return ResponseEntity.notFound().build();
	        } catch (Exception e) {
	            // Handling any other unexpected exception
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	        }
	    }


	}


