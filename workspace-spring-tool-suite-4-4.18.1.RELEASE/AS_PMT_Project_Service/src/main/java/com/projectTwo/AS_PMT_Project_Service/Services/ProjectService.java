package com.projectTwo.AS_PMT_Project_Service.Services;

import java.text.ParseException;
import java.util.*;
import org.springframework.stereotype.Service;

import com.projectTwo.AS_PMT_Project_Service.Entities.Issue_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.Project_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.ScrumBoard;
import com.projectTwo.AS_PMT_Project_Service.Entities.Sprint_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.UserRoles;
import com.projectTwo.AS_PMT_Project_Service.Repositories.ProjectRepository;
import com.projectTwo.AS_PMT_Project_Service.utils.DateTimeGetterUtil;

import jakarta.persistence.EntityManager;

@Service
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final SprintService sprintService;
	private final IssueService issueServices;


	public ProjectService(ProjectRepository projectRepository, SprintService sprintService, IssueService issueServices) {
		this.projectRepository = projectRepository;
		this.sprintService = sprintService;
		this.issueServices = issueServices;
	}

	// create new project
	public Project_Entity createProject(Project_Entity project) {
		// Generate a unique project ID and set it in the project entity
		String projectId = generateUniqueProjectId(project.getName());
		project.setProjectId(projectId);
		project.setCreatedDate(new DateTimeGetterUtil("ITC").getCurrent());
		project.setLastUpdatedDate(new DateTimeGetterUtil("ITC").getCurrent());
		// Save the project in the database
		return projectRepository.save(project);
	}

	// view all projects
	public List<Project_Entity> getAllProjects() {
		return projectRepository.findAll();
	}

	// get project by id
	public Project_Entity getProjectByProjectId(String ProjectId) {
		Project_Entity project = projectRepository.findByProjectId(ProjectId);
		if (project == null) {
			throw new NoSuchElementException("No projects found with the specified name");
		}
		return project;
	}
	
	public List<ScrumBoard> getStatusesByProjectId(String ProjectId)
	{
		Project_Entity project = projectRepository.findByProjectId(ProjectId);
		if (project == null) {
			throw new NoSuchElementException("No projects found with the specified name");
		}
		return project.getStatus();
		
	}
	
	public Sprint_Entity getActiveSprintByProject(Project_Entity project) {
		if(project.equals(null))
		{
			throw new NoSuchElementException("No Active sprints found in the specified project");
		}
		return project.getActiveSprint();
	}
	

	 public boolean updateActiveSprint(String projectId, Sprint_Entity updatedActiveSprint) {
	        Project_Entity project = projectRepository.findByProjectId(projectId);
	        if (project != null) {
	        	
	        Sprint_Entity sprint = sprintService.getSprintBySprintId(updatedActiveSprint.getSprintId());
	        
	        if(sprint.equals(null))
	        {
	        	return false;
	        }
	        
	        if(!sprintService.getSprintByProjectId(projectId).contains(sprint))
	        {
	        	return false;
	        }
	            // Update the active sprint
	        project.setActiveSprint(sprint);
	            // Save the changes
	        projectRepository.save(project);
	        return true;
	        }
	        return false; // Return false if project not found or update fails
	    }

	// delete project by id
	public void deleteProject(String ProjectId) {
		Project_Entity project = projectRepository.findByProjectId(ProjectId);
		if (project == null) {
			throw new NoSuchElementException("No projects found with the specified name");
		}
		projectRepository.delete(project);
	}

	// edit and update project
	public Project_Entity updateProject(String ProjectId, Project_Entity project) throws ParseException {
		Project_Entity ExistingProject = projectRepository.findByProjectId(ProjectId);
		if (ExistingProject == null) {
			throw new NoSuchElementException("No projects found with the specified name");
		}
			ExistingProject.setName(project.getName());
			ExistingProject.setDescription(project.getDescription());
			ExistingProject.setProjectType(project.getProjectType());
			if(project.getBacklog()!=null)
			{
				ExistingProject.setBacklog(project.getBacklog());
			}
			if(project.getStartDate()!=null)
			{
				ExistingProject.setStartDate(project.StringTodate(project.getStartDate()));
			}
			if(project.getEndDate()!=null)
			{
				ExistingProject.setEndDate(project.StringTodate(project.getEndDate()));
			}
			ExistingProject.setLastUpdatedDate(new DateTimeGetterUtil("ITC").getCurrent());

			return projectRepository.save(ExistingProject);
		

	}

	private String generateUniqueProjectId(String projectName) {
		long timestamp = new Date().getTime();

		// Generate a random number or alphanumeric string
		Random random = new Random();
		UUID uuid = UUID.randomUUID();

		String uuidGen = uuid.toString();
		int randomNumber = random.nextInt(1000); // Generate a random number between 0 and 999

		// Combine the timestamp and random number to create the project ID
		String projectId = ("value=" + uuidGen + "ts=" + String.valueOf(timestamp) + String.format("%03d", randomNumber)
				+ "name=" + projectName);

		return projectId;
		// Logic to generate a unique project ID
	}

	public List<Project_Entity> getProjectsByIds(List<String> projectIds) {
	    // Implement the logic to fetch projects based on the provided project IDs
	    // Here, you can use any data access mechanism such as a database query or an external API call
	    
	    // Example implementation:
	    List<Project_Entity> projects = new ArrayList<>();
	    for (String projectId : projectIds) {
	        // Assuming you have a method to fetch a project entity by ID, e.g., fetchProjectById(projectId)
	        Project_Entity project = getProjectByProjectId(projectId);
	        if (project != null) {
	            projects.add(project);
	        }
	    }
	    
	    return projects;
	}

	 public boolean checkSprintExistenceInSprintsList(String projectId, Sprint_Entity updatedActiveSprint) {
	        // Assuming you have a method to retrieve the project by ID, which contains the list of sprints
	        Project_Entity project = projectRepository.findByProjectId(projectId);

	        if (project != null) {
	            List<Sprint_Entity> sprints = project.getSprints();

	            for (Sprint_Entity sprint : sprints) {
	                // Compare the sprint IDs to check existence
	                if (sprint.getSprintId().equals(updatedActiveSprint.getSprintId())) {
	                    return true; // Sprint exists in the list
	                }
	            }
	        }
	        return false; // Sprint does not exist in the list
	    }

	  public List<UserRoles> getUserRolesByProjectId(String projectId) {
	       
	        List<UserRoles> userRoles = projectRepository.getUserRolesByProjectId(projectId);
	        return userRoles; // Return the retrieved UserRoles
	    }

	public boolean updatePokerIssue(String projectId, Issue_Entity updatedPokerIssue) {
		 Project_Entity project = projectRepository.findByProjectId(projectId);
	        if (project != null) {
	        	
	        Issue_Entity issue = issueServices.getIssueById(updatedPokerIssue.getId());
	        
	        if(issue.equals(null))
	        {
	        	return false;
	        }
	        
	            // Update the active sprint
	        project.setPokerIssue(issue);
	            // Save the changes
	        projectRepository.save(project);
	        return true;
	        }
	        return false; // 
	}

	public Issue_Entity getPokerIssuesByProjectId(String projectId) {
		Project_Entity project = projectRepository.findByProjectId(projectId);
		if (project == null) {
			throw new NoSuchElementException("No projects found with the specified name");
		}
		return project.getPokerIssue();
    }

}
