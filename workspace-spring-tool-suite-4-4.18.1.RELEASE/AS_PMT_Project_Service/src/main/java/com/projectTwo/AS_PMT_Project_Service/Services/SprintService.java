package com.projectTwo.AS_PMT_Project_Service.Services;

import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.util.*;

import org.springframework.stereotype.Service;

import com.projectTwo.AS_PMT_Project_Service.Entities.Sprint_Entity;
import com.projectTwo.AS_PMT_Project_Service.Repositories.SprintRepository;
import com.projectTwo.AS_PMT_Project_Service.utils.DateTimeGetterUtil;

@Service
public class SprintService {

	private final SprintRepository sprintRepository;

	public SprintService(SprintRepository sprintRepository) {
		this.sprintRepository = sprintRepository;
	}

	public Sprint_Entity createSprint(Sprint_Entity sprint, String projectID) {
		// Generate a unique project ID and set it in the project entity
		String sprintId = generateUniqueSprintId(sprint.getName());
		sprint.setSprintId(sprintId);
		sprint.setCreatedDate(new DateTimeGetterUtil("ITC").getCurrent());
		sprint.setLastUpdatedDate(new DateTimeGetterUtil("ITC").getCurrent());
		sprint.setProjectId(projectID);
		// Save the project in the database
		return sprintRepository.save(sprint);
	}

	   public List<Sprint_Entity> getSprintByProjectId(String projectId) {
	        List<Sprint_Entity> sprints = sprintRepository.findByProjectId(projectId);
	        if (sprints.isEmpty()) {
	            throw new NoSuchElementException("No sprints found with the specified project ID and user ID");
	        }
	        return sprints;
	    }

	public Sprint_Entity getSprintBySprintId(String sprinttId) {
		Sprint_Entity Sprint = sprintRepository.findBySprintId(sprinttId);
		if (Sprint == null) {
			throw new NoSuchElementException("No Sprints found with the specified name");
		}
		return Sprint;
	}

	public Sprint_Entity updateSprint(String sprintId, Sprint_Entity sprint) throws ParseException {
		Sprint_Entity ExistingSprint = sprintRepository.findBySprintId(sprintId);
		if (ExistingSprint == null) {
			throw new NoSuchElementException("No projects found with the specified name");
		}

		ExistingSprint.setName(sprint.getName());
		ExistingSprint.setDescription(sprint.getDescription());
		if(sprint.getStartDate()!=null)
		{
			ExistingSprint.setStartDate(sprint.StringTodate(sprint.getStartDate()));
		}
		if(sprint.getEndDate()!=null)
		{
			ExistingSprint.setEndDate(sprint.StringTodate(sprint.getEndDate()));
		}
		
		ExistingSprint.setLastUpdatedDate(new DateTimeGetterUtil("ITC").getCurrent());

		return sprintRepository.save(ExistingSprint);

	}


	public void deleteSprint(String sprintId, String projectId) {
        Sprint_Entity sprint = sprintRepository.findBySprintId(sprintId);
        if (sprint == null) {
            throw new NoSuchElementException("No sprint found with the specified ID");
        }
        if (sprint.getProjectId().equals(projectId)) {
            sprintRepository.delete(sprint);
        } else {
            throw new NoSuchElementException("Sprint does not belong to the specified project");
        }
    }


	private String generateUniqueSprintId(String SprintName) {
		long timestamp = new Date().getTime();

		// Generate a random number or alphanumeric string
		Random random = new Random();
		UUID uuid = UUID.randomUUID();

		String uuidGen = uuid.toString();
		int randomNumber = random.nextInt(1000); // Generate a random number between 0 and 999

		// Combine the timestamp and random number to create the project ID
		String sprintID = ("value=" + uuidGen + "ts=" + String.valueOf(timestamp) + String.format("%03d", randomNumber)
				+ "name=" + SprintName);

		return sprintID;
		// Logic to generate a unique project ID
	}

}
