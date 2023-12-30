package com.projectTwo.AS_PMT_Userstory_Service.Services;

import java.text.ParseException;
import java.util.*;

import org.springframework.stereotype.Service;

import com.projectTwo.AS_PMT_Userstory_Service.Entities.Userstory_Entity;
import com.projectTwo.AS_PMT_Userstory_Service.Repositories.UserStoryRepository;
import com.projectTwo.AS_PMT_Userstory_Service.utils.DateTimeGetterUtil;

//import com.projectTwo.AS_PMT_Userstory_Service.Repositories.UserStoryRepository;

@Service
public class UserstoryService {

	private final UserStoryRepository userStoryRepository;

	public UserstoryService(UserStoryRepository userStoryRepository) {
		this.userStoryRepository = userStoryRepository;
	}

	public Userstory_Entity createUserStory(Userstory_Entity userStory, String projectID) {
		// Generate a unique project ID and set it in the project entity
		String UserstoryId = generateUniqueUserStoryId(userStory.getTitle());
		userStory.setUserStoryID(UserstoryId);
		userStory.setCreatedDate(new DateTimeGetterUtil("ITC").getCurrent());
		userStory.setUpdatedDate(new DateTimeGetterUtil("ITC").getCurrent());
		userStory.setProjectId(projectID);
		// Save the project in the database
		return userStoryRepository.save(userStory);
	}


	public Userstory_Entity getUserStoryByUserStoryId(String sprinttId) {
		Userstory_Entity userStory = userStoryRepository.findByUserStoryId(sprinttId);
		if (userStory == null) {
			throw new NoSuchElementException("No projects found with the specified name");
		}
		return userStory;
		
	}

	// all sprints of the projects
	public List<Userstory_Entity> getUserStoryByProjectId(String projectID) {
		List<Userstory_Entity> userStories = new ArrayList<>();
		//userStories.addAll(userStoryRepository.findByProjectId(projectID));
		if (userStories.isEmpty()) {
			throw new NoSuchElementException("No sprints found with the specified project ID");
		}
		return userStories;
	}

	public Userstory_Entity updateUserStory(String userStoryID, Userstory_Entity userStory, String projectID) throws ParseException {
		Userstory_Entity ExistingUserStory = userStoryRepository.findByUserStoryId(userStoryID);
		if (ExistingUserStory == null) {
			throw new NoSuchElementException("No projects found with the specified name");
		}
		if (!ExistingUserStory.getTitle().equals(userStory.getTitle())
				|| !ExistingUserStory.getDescription().equals(userStory.getDescription())
				|| !ExistingUserStory.getAcceptanceCriteria().equals(userStory.getAcceptanceCriteria())
				|| !ExistingUserStory.getAdditionalNotes().equals(userStory.getAdditionalNotes())
				) {

			if (ExistingUserStory.getProjectId().equals(projectID)) {
					ExistingUserStory.setTitle(userStory.getTitle());
					ExistingUserStory.setDescription(userStory.getDescription());
					ExistingUserStory.setAcceptanceCriteria(userStory.getAcceptanceCriteria());
					ExistingUserStory.setAdditionalNotes(userStory.getAdditionalNotes());
					ExistingUserStory.setUpdatedDate(new DateTimeGetterUtil("ITC").getCurrent());

					return userStoryRepository.save(ExistingUserStory);
			}
		}
		return null;

	}

	public void deleteUserStory(String userStoryId, String projectID) {
		Userstory_Entity userStory = userStoryRepository.findByUserStoryId(userStoryId);
		if (userStory == null) {
			throw new NoSuchElementException("No projects found with the specified name");
		}
		if (userStory.getProjectId().equals(projectID)) {
			userStoryRepository.delete(userStory);
		}

	}

	private String generateUniqueUserStoryId(String SprintName) {
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

