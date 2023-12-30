package com.projectTwo.AS_PMT_UserProfile_Service.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectTwo.AS_PMT_UserProfile_Service.Entities.UserProfile;
import com.projectTwo.AS_PMT_UserProfile_Service.Repositories.UserProfileRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserProfileService {
	
	@Autowired
	private UserProfileRepository userProfileRepository;

	public UserProfile createUserProfile(UserProfile userProfile) {
		// Save the user profile in the database
		return userProfileRepository.save(userProfile);
	}

	public List<UserProfile> getAllUserProfiles() {
		return userProfileRepository.findAll();
	}

	public UserProfile getUserProfileByUsername(String username) {
		UserProfile userProfile = userProfileRepository.findByUsername(username);
		if (userProfile == null) {
			throw new NoSuchElementException("No user profile found with the specified username");
		}
		return userProfile;
	}

	public void deleteUserProfile(String username) {
		UserProfile userProfile = userProfileRepository.findByUsername(username);
		if (userProfile == null) {
			throw new NoSuchElementException("No user profile found with the specified username");
		}
		userProfileRepository.delete(userProfile);
	}

	public UserProfile updateUserProfile(String username, UserProfile updatedUserProfile) {
		UserProfile existingUserProfile = userProfileRepository.findByUsername(username);
		if (existingUserProfile == null) {
			throw new NoSuchElementException("No user profile found with the specified username");
		}

		// Update the fields of the existing user profile with the new values
		existingUserProfile.setLocation(updatedUserProfile.getLocation());
		existingUserProfile.setEmail(updatedUserProfile.getEmail());
		existingUserProfile.setPhone(updatedUserProfile.getPhone());
		existingUserProfile.setFirstName(updatedUserProfile.getFirstName());
		existingUserProfile.setLastName(updatedUserProfile.getLastName());
		existingUserProfile.setSkills(updatedUserProfile.getSkills());
		existingUserProfile.setExperience(updatedUserProfile.getExperience());
		existingUserProfile.setInterest(updatedUserProfile.getInterest());
		existingUserProfile.setProfilePicture(updatedUserProfile.getProfilePicture());
		
		// Save the updated user profile in the database
		return userProfileRepository.save(existingUserProfile);
	}
}
