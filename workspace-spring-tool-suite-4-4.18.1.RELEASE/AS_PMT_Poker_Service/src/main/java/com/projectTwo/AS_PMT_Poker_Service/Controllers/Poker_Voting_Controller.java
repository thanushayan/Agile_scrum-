package com.projectTwo.AS_PMT_Poker_Service.Controllers;

import com.projectTwo.AS_PMT_Poker_Service.Entities.Poker_Voting_Entity;

import com.projectTwo.AS_PMT_Poker_Service.Services.Poker_Vote_Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/poker")
@CrossOrigin(origins = "http://localhost:3000")
public class Poker_Voting_Controller {
	
    @Autowired
    private Poker_Vote_Service pokerVoteService;
    
    @PostMapping("/create")
    public ResponseEntity<?> createOrUpdatePokerValue(@RequestBody Poker_Voting_Entity request) {
        try {
            Poker_Voting_Entity result = pokerVoteService.createOrUpdatePokerValue(request.getUserId(), request.getProjectId(), request.getTaskId(), request.getPokerValue());
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating/updating poker value");
        }
    }

    @GetMapping("/retrieve")
    public ResponseEntity<?> getPokerValue(@RequestParam String userId, @RequestParam String projectId, @RequestParam String taskId) {
        try {
            Integer result = pokerVoteService.getPokerValue(userId, projectId, taskId);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving poker value");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePokerValue(@RequestParam String userId, @RequestParam String projectId, @RequestParam String taskId) {
        try {
            pokerVoteService.deletePokerValue(userId, projectId, taskId);
            return ResponseEntity.ok("Poker value deleted successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting poker value");
        }
    }
    
    @GetMapping("/listByProjectAndTask")
    public List<Integer> getPokerValuesByProjectAndTask(@RequestParam String projectId, @RequestParam String taskId) {
        return pokerVoteService.getPokerValuesByProjectAndTask(projectId, taskId);
    }
    
    @GetMapping("/usersByProjectAndPokerValue")
    public List<String> getUserIdsByProjectAndPokerValue(@RequestParam String projectId, @RequestParam Integer pokerValue) {
        return pokerVoteService.getUserIdsByProjectAndPokerValue(projectId, pokerValue);
    }
    
    @GetMapping("/listEntitiesByProjectAndTask")
    public List<Poker_Voting_Entity> getPokerVotingEntitiesByProjectAndTask(
            @RequestParam String projectId, 
            @RequestParam String taskId) {
        return pokerVoteService.getPokerVotingEntitiesByProjectAndTask(projectId, taskId);
    }



}
