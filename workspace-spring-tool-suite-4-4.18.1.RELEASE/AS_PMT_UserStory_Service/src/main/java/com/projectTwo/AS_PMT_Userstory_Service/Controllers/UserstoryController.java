package com.projectTwo.AS_PMT_Userstory_Service.Controllers;

import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projectTwo.AS_PMT_Userstory_Service.Entities.Userstory_Entity;
import com.projectTwo.AS_PMT_Userstory_Service.Services.UserstoryService;

@RestController
@RequestMapping("{projectID}/userstorirs")
public class UserstoryController {
	private final UserstoryService userStoryService;

	public UserstoryController(UserstoryService userStoryService) {
		this.userStoryService = userStoryService;
	}

	@PostMapping(value = "/create=newstory")
	public ResponseEntity<Userstory_Entity> createUserStory(@RequestBody Userstory_Entity userStory,
			@PathVariable("projectID") String projectID) {
		Userstory_Entity createduserStory = userStoryService.createUserStory(userStory, projectID);
		return ResponseEntity.status(HttpStatus.CREATED).body(createduserStory);
	}

	@GetMapping(value = "/view=alluser_stories")
	public ResponseEntity<List<Userstory_Entity>> getAllUserStory(@PathVariable("projectID") String projectID) {
		List<Userstory_Entity> userStory = userStoryService.getUserStoryByProjectId(projectID);
		return ResponseEntity.ok(userStory);
	}

	@GetMapping(value = "userstory/{userStoryId}")
	public ResponseEntity<Userstory_Entity> getUserStoryById(@PathVariable String userStoryid) {
		try {
			Userstory_Entity userStoy = userStoryService.getUserStoryByUserStoryId(userStoryid);
			if (userStoy == null) {
				return null;
			} else {
				// userStoryId found, you can perform further operations or return a success
				// message
				return ResponseEntity.ok(userStoy);
			}
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@PostMapping(value = "update/{userStoryId}")
	public ResponseEntity<Userstory_Entity> updateUserStoryDetail(@PathVariable("projectID") String projectID,
			@PathVariable String userStoryId, @RequestBody Userstory_Entity userStory) throws ParseException {
		Userstory_Entity updatedUserStory = userStoryService.updateUserStory(userStoryId, userStory, projectID);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedUserStory);
	}

	@DeleteMapping(value = "delete/{userStoryId}")
	public ResponseEntity<Void> deleteUserStory(@PathVariable String userStoryId,
			@PathVariable("projectID") String projectID) {
		userStoryService.deleteUserStory(userStoryId, projectID);
		return ResponseEntity.noContent().build();
	}
}
