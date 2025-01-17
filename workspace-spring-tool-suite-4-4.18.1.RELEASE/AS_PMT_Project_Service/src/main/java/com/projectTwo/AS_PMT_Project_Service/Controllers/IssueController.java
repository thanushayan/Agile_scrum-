package com.projectTwo.AS_PMT_Project_Service.Controllers;

import com.projectTwo.AS_PMT_Project_Service.Entities.Project_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.Sprint_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.Backlog_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.ScrumBoard;
import com.projectTwo.AS_PMT_Project_Service.Entities.Issue_Entity;
import com.projectTwo.AS_PMT_Project_Service.Services.ProjectService;
import com.projectTwo.AS_PMT_Project_Service.Services.SprintService;
import com.projectTwo.AS_PMT_Project_Service.Services.BacklogService;
import com.projectTwo.AS_PMT_Project_Service.Services.IssueService;
import com.projectTwo.AS_PMT_Project_Service.Services.ScrumBoardService;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/issues")
public class IssueController {
	private final IssueService issueService;
	private final ProjectService projectService;
	private final SprintService sprintService;
	private final BacklogService backlogService;
	private final ScrumBoardService scrumBoardService;

	public IssueController(IssueService issueService, ProjectService projectService, SprintService sprintService, BacklogService backlogService, ScrumBoardService scrumBoardService) {
		this.issueService = issueService;
		this.sprintService = sprintService;
		this.projectService = projectService;
		this.backlogService = backlogService;
		this.scrumBoardService = scrumBoardService;
	}
	
	 @PutMapping("/{issueId}/status/{statusName}")
	    public ResponseEntity<?> updateIssueStatus(@PathVariable Long issueId, @PathVariable String statusName) {
	        try {
	            Issue_Entity updatedIssue = issueService.updateIssueStatus(issueId, statusName);
	            return ResponseEntity.ok(updatedIssue);
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.ok("Invalid issueId or statusName: " + e.getMessage());
	            
	        } catch (DataAccessException e) {
	        	return ResponseEntity.ok("Database access error: " + e.getMessage());
	        } catch (Exception e) {
	        	return ResponseEntity.ok("An unexpected error occurred: " + e.getMessage());
	        }
	    }
	
	@PostMapping("/create-issue/backlog/{projectId}")
    public ResponseEntity<?> createIssueInBacklog(@RequestBody Issue_Entity issue, @PathVariable String projectId) throws ParseException {
        // Get the backlog by project ID
        Backlog_Entity backlog = backlogService.getByProjectId(projectId);
        Project_Entity project = projectService.getProjectByProjectId(projectId);

        // Check if the backlog exists
        if (backlog == null) {
            // If the backlog does not exist, create a new backlog for the project
           
            if (project == null) {
                // If the project doesn't exist, return a not found response
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found for projectId: " + projectId);
            }

            // Create a new backlog and associate it with the project
            backlog = new Backlog_Entity();
            backlog.setProjectId(projectId);
            project.setBacklog(backlog);
            projectService.updateProject(projectId, project); // Save the updated project with the new backlog
        }

        try {
        	Issue_Entity createdissue = issueService.createissue(issue);

			if (createdissue == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("issue Creating Failed");
			}
			backlog.getIssues().add(createdissue);
			backlogService.updateBacklog(projectId, backlog);
			projectService.updateProject(project.getProjectId(), project);
            // Return a success response
            return ResponseEntity.ok("Issue added to the backlog successfully.");
        } catch (Exception e) {
            // If there's an error while adding the issue, return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add issue to the backlog: " + e.getMessage());
        }
    }



	@PostMapping(value = "/create-issue/sprint/{sprintID}")
	public ResponseEntity<?> createIssueInSprint(@RequestBody Issue_Entity issue,
			@PathVariable("sprintID") String sprintID) {
		try {
			// Validate project existence
			Sprint_Entity sprint = sprintService.getSprintBySprintId(sprintID);

			if (sprint == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sprint not found");
			}

			Project_Entity project = projectService.getProjectByProjectId(sprint.getProjectId());

			if (project == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
			}
			// Create the issue
			Issue_Entity createdissue = issueService.createissue(issue);

			if (createdissue == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("issue Creating Failed");
			}
					 
			ScrumBoard status = scrumBoardService.getScrumBoardByStatusName("Todo");
			
			if(status==null)
			{
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("status Creating Failed");
			}
			
			createdissue.setStatus(status);

			// Add the created issue to the project's issue list
			sprint.getIssues().add(createdissue);

			sprintService.updateSprint(sprint.getSprintId(), sprint);
			projectService.updateProject(project.getProjectId(), project);

			return ResponseEntity.status(HttpStatus.CREATED).body(createdissue);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}
	
	@DeleteMapping("/delete-issue/sprint/{sprintID}/{issueID}")
	public ResponseEntity<?> deleteIssueFromSprint(@PathVariable("sprintID") String sprintID,
	        @PathVariable("issueID") String issueID) {
	    try {
	        // Validate sprint existence
	        Sprint_Entity sprint = sprintService.getSprintBySprintId(sprintID);

	        if (sprint == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sprint not found");
	        }

	        // Find the issue in the sprint's issue list and remove it
	        Issue_Entity issueToRemove = sprint.getIssues().stream()
	                .filter(issue -> issue.getIssueId().equals(issueID))
	                .findFirst()
	                .orElse(null);

	        if (issueToRemove == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found in the sprint");
	        }

	        sprint.getIssues().remove(issueToRemove);
	        sprintService.updateSprint(sprint.getSprintId(), sprint);

	        return ResponseEntity.ok("Issue deleted from the sprint successfully.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
	    }
	}

	@DeleteMapping("/delete-issue/backlog/{projectId}/{issueID}")
	public ResponseEntity<?> deleteIssueFromBacklog(@PathVariable("projectId") String projectId,
	        @PathVariable("issueID") String issueID) {
	    try {
	        // Get the backlog by project ID
	        Backlog_Entity backlog = backlogService.getByProjectId(projectId);

	        if (backlog == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Backlog not found for projectId: " + projectId);
	        }

	        // Find the issue in the backlog's issue list and remove it
	        Issue_Entity issueToRemove = backlog.getIssues().stream()
	                .filter(issue -> issue.getIssueId().equals(issueID))
	                .findFirst()
	                .orElse(null);

	        if (issueToRemove == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found in the backlog");
	        }

	        backlog.getIssues().remove(issueToRemove);
	        backlogService.updateBacklog(projectId, backlog);

	        return ResponseEntity.ok("Issue deleted from the backlog successfully.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
	    }
	}

	@PutMapping("/{issueId}")
	public ResponseEntity<?> updateIssue(@PathVariable Long issueId, @RequestBody Issue_Entity updatedIssueData) {
	    try {
	        Issue_Entity existingIssue = issueService.getIssueById(issueId);

	        if (existingIssue == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Issue not found for ID: " + issueId);
	        }

	        // Check and update individual fields if they are present in updatedIssueData

	        if (updatedIssueData.getTitle() != null) {
	            existingIssue.setTitle(updatedIssueData.getTitle());
	        }
	        if (updatedIssueData.getDescription() != null) {
	            existingIssue.setDescription(updatedIssueData.getDescription());
	        }
	        if (updatedIssueData.getPriority() != null) {
	            existingIssue.setPriority(updatedIssueData.getPriority());
	        }
	        if (updatedIssueData.getDueDate() != null) {
	            existingIssue.setDueDate(existingIssue.StringTodate(updatedIssueData.getDueDate()));
	        }
	        
	        if (updatedIssueData.getStartDate() != null) {
	            existingIssue.setStartDate(existingIssue.StringTodate(updatedIssueData.getStartDate()));
	        }
	        if (updatedIssueData.getAssignee() != null) {
	            existingIssue.setAssignee(updatedIssueData.getAssignee());
	        }
	        if (updatedIssueData.getComment() != null) {
	            existingIssue.setComment(updatedIssueData.getComment());
	        }
	        if (updatedIssueData.getType() != null) {
	            existingIssue.setType(updatedIssueData.getType());
	        }
	        
	        if (updatedIssueData.getIssueId() != null) {
	            existingIssue.setIssueId(updatedIssueData.getIssueId());
	        }
	        
	        if(updatedIssueData.getProgress()!=0)
	        {
	        	existingIssue.setProgress(updatedIssueData.getProgress());
	        }

	        Issue_Entity updatedIssue = issueService.updateIssue(existingIssue);
	        if (updatedIssueData.getStatus() != null) {
	        	updatedIssue = issueService.updateIssueStatus(updatedIssue.getId(), updatedIssueData.getStatus().getStatusName());
	        }

	        return ResponseEntity.ok(updatedIssue);
	    } catch (DataAccessException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database access error: " + e.getMessage());
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
	    }
	}



	@GetMapping
	public ResponseEntity<List<Issue_Entity>> getAllissues() {
		List<Issue_Entity> issue = issueService.getAllissues();
		return ResponseEntity.ok(issue);
	}
	
	@GetMapping("/{projectId}")
	public ResponseEntity<List<Issue_Entity>> getAllissuesInProject(@PathVariable String projectId) {
		List<Issue_Entity> issue = issueService.getAllissues();
		List<Issue_Entity> issues = new ArrayList();
		
		for(Issue_Entity newIssue : issue)
		{
			if(newIssue.getProjectId().endsWith(projectId))
			{
				issues.add(newIssue);
			}
		}
		return ResponseEntity.ok(issues);
	}
	
	@GetMapping("/{projectId}/{userName}")
	public ResponseEntity<List<Issue_Entity>> getAllissuesInProject(@PathVariable String projectId, @PathVariable String userName) {
		List<Issue_Entity> issue = issueService.getAllissues();
		List<Issue_Entity> issues = new ArrayList();
		
		for(Issue_Entity newIssue : issue)
		{
			if(newIssue.getProjectId().equals(projectId))
			{
				if(newIssue.getAssignee().equals(userName))
				{
					issues.add(newIssue);	
				}
			}
		}
		return ResponseEntity.ok(issues);
	}
	
	@GetMapping("personal/{projectId}/{userName}")
	public ResponseEntity<Float> getAllissuesProgress(@PathVariable String projectId, @PathVariable String userName) {
		List<Issue_Entity> issue = issueService.getAllissues();
		List<Issue_Entity> issues = new ArrayList();
		float progress = 0;
		int count = 0;
		for(Issue_Entity newIssue : issue)
		{
			if(newIssue.getProjectId().equals(projectId))
			{
				if(newIssue.getAssignee().equals(userName))
				{
					progress += newIssue.getProgress();
					count++;
				}
			}
		}
		return ResponseEntity.ok(progress/count);
	}


}
