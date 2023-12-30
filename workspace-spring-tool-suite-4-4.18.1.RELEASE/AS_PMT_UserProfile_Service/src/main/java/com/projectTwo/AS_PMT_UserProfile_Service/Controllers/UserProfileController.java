package com.projectTwo.AS_PMT_UserProfile_Service.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.projectTwo.AS_PMT_UserProfile_Service.Entities.UserProfile;
import com.projectTwo.AS_PMT_UserProfile_Service.Services.UserProfileService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/userprofiles")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final RestTemplate restTemplate;

    @Autowired
    public UserProfileController(UserProfileService userProfileService,RestTemplateBuilder restTemplateBuilder) {
        this.userProfileService = userProfileService;
        this.restTemplate = restTemplateBuilder.build();
    }

    @PostMapping(value = "/create")
    public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile) {
        UserProfile createdProfile = userProfileService.createUserProfile(userProfile);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserProfileByUsername(@PathVariable String username) {
        try {
            UserProfile userProfile = userProfileService.getUserProfileByUsername(username);
            return ResponseEntity.ok(userProfile);
        } catch (NoSuchElementException e) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user exists.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUserProfile(@PathVariable String username, @RequestBody UserProfile userProfile) {
        try {
            UserProfile updatedProfile = userProfileService.updateUserProfile(username, userProfile);
            return ResponseEntity.ok(updatedProfile);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                return ResponseEntity.ok("Forbidden");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable String username) {
        try {
            userProfileService.deleteUserProfile(username);
            String deleteUserProfileUrl = "http://localhost:8080/auth/users/" + username;
            restTemplate.delete(deleteUserProfileUrl);
            
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
