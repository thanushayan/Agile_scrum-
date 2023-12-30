package com.projectTwo.AS_PMT_Project_Service.Controllers;

import java.util.NoSuchElementException;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.projectTwo.AS_PMT_Project_Service.Entities.Backlog_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.ProjectAndUserEntity;
import com.projectTwo.AS_PMT_Project_Service.Services.BacklogService;


@RestController
@RequestMapping("/backlog")

public class BacklogController {
	
	private final RestTemplate restTemplate;
	private final BacklogService backlogService;

	public BacklogController(RestTemplateBuilder restTemplateBuilder, BacklogService backlogService) {
		this.restTemplate = restTemplateBuilder.build();
		this.backlogService = backlogService;
	}

	@GetMapping(value = "get/{userId}/{projectId}")
	public ResponseEntity<?> getBacklogByProjectAndUser(@PathVariable("userId") String userId,
			@PathVariable("projectId") String projectId) {
		try {
			// Validate project existence
			String getUserProfileUrl = "http://localhost:8087/api/project-and-user/" + userId + "/" + projectId;

			// Check if the project belongs to the user
			ResponseEntity<ProjectAndUserEntity> projectAndUser = restTemplate.getForEntity(getUserProfileUrl,
					ProjectAndUserEntity.class);

			if (projectAndUser.getStatusCode() != HttpStatus.OK) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized request");
			}

			// Retrieve the backlog details
			Backlog_Entity backlog = backlogService.getByProjectId(projectId);

			if (backlog == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Backlog not found");
			}

			return ResponseEntity.ok(backlog);
		} catch (NoSuchElementException e) {
			// Handling NoSuchElementException, typically thrown when the projectId or
			// userId is invalid
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

}
