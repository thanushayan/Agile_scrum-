package com.projectTwo.AS_PMT_UserAuth_Service.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.core.AuthenticationException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;


import com.projectTwo.AS_PMT_UserAuth_Service.DTO.AuthRequest;
import com.projectTwo.AS_PMT_UserAuth_Service.Entity.UserCredential;
import com.projectTwo.AS_PMT_UserAuth_Service.Services.AuthService;
import com.projectTwo.AS_PMT_UserAuth_Service.DTO.UserProfile;

@SuppressWarnings("deprecation")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;
    
    private final RestTemplate restTemplate;

    public AuthController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @PostMapping("/register")
    public ResponseEntity<?> addNewUser(@RequestBody UserCredential user) {
        try {
            ResponseEntity<UserCredential> existingUserResponse = service.getUserByUserID(user.getName());
            if (existingUserResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                UserCredential savedUser = service.saveUser(user);

                UserProfile createdProfile = new UserProfile();
                createdProfile.setFirstName(user.getFname());
                createdProfile.setLastName(user.getLname());
                createdProfile.setUsername(user.getName());
                createdProfile.setEmail(user.getEmail());

                
                String createUserProfileUrl = "http://localhost:8085/api/userprofiles/create";
                ResponseEntity<UserProfile> createdProfileResponse = restTemplate.postForEntity(createUserProfileUrl, createdProfile, UserProfile.class);

                if (createdProfileResponse.getStatusCode() == HttpStatus.CREATED) {
                    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
                } else {
                    String errorMessage = "Failed to create user profile.";
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
                }
            } else {
                String errorMessage = "User with username '" + user.getName() + "' already exists.";
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
            }
        } catch (Exception e) {
            String errorMessage = "An error occurred while adding a new user.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }



    @SuppressWarnings("deprecation")
	@PostMapping("/login")
    public ResponseEntity<String> getToken(@RequestBody AuthRequest authRequest) {
    	ResponseEntity<UserCredential> existingUserResponse = service.getUserByUserID(authRequest.getUsername());
    	if (existingUserResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
    		 String errorMessage = "User with username '" + authRequest.getUsername() + "' not found.";
             return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    	}
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authenticate.isAuthenticated()) {
                String token = service.generateToken(authRequest.getUsername());
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + " wrong password");
        }
    }


    @GetMapping(value = "validate/{token}")
    public ResponseEntity<String> validateToken(@PathVariable String token) {
        try {
            String username = service.validateToken(token);
            return ResponseEntity.ok(username);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired");
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during token validation");
        }
    }
    
    @DeleteMapping("/users/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            // Check if the user exists
            ResponseEntity<UserCredential> existingUserResponse = service.getUserByUserID(username);
            if (existingUserResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                String errorMessage = "User with username '" + username + "' not found.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
            
            // Delete the user
            service.deleteUser(username);
       
            return ResponseEntity.ok("User deleted successfully.");
        } catch (Exception e) {
            String errorMessage = "An error occurred while deleting the user.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }



    
    
}