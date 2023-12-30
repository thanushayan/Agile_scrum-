package com.projectTwo.AS_PMT_Poker_Service.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectTwo.AS_PMT_Poker_Service.Entities.Poker_Voting_Entity;
import com.projectTwo.AS_PMT_Poker_Service.Repositories.Poker_Voting_Repository;


@Service
public class Poker_Voting_Service implements Poker_Vote_Service {
    @Autowired
    private Poker_Voting_Repository pokerValueRepository;
    
    @Override
    public Poker_Voting_Entity createOrUpdatePokerValue(String userId, String projectId, String taskId, Integer pokerValue) {
        // Check if the poker value already exists
        Optional<Poker_Voting_Entity> existingValue = pokerValueRepository.findByUserIdAndProjectIdAndTaskId(userId, projectId, taskId);
        
        if (existingValue.isPresent()) {
        	Poker_Voting_Entity value = existingValue.get();
            value.setPokerValue(pokerValue);
            return pokerValueRepository.save(value);
        } else {
        	Poker_Voting_Entity newValue = new Poker_Voting_Entity();
            newValue.setUserId(userId);
            newValue.setProjectId(projectId);
            newValue.setTaskId(taskId);
            newValue.setPokerValue(pokerValue);
            return pokerValueRepository.save(newValue);
        }
    }
    
    @Override
    public Integer getPokerValue(String userId, String projectId, String taskId) {
        Optional<Poker_Voting_Entity> pokerValue = pokerValueRepository.findByUserIdAndProjectIdAndTaskId(userId, projectId, taskId);
        return pokerValue.map(Poker_Voting_Entity::getPokerValue).orElse(null);
    }
    
    @Override
    public void deletePokerValue(String userId, String projectId, String taskId) {
        Optional<Poker_Voting_Entity> pokerValue = pokerValueRepository.findByUserIdAndProjectIdAndTaskId(userId, projectId, taskId);
        pokerValue.ifPresent(value -> pokerValueRepository.delete(value));
    }
    
    
    @Override
    public List<Integer> getPokerValuesByProjectAndTask(String projectId, String taskId) {
        List<Poker_Voting_Entity> pokerValues = pokerValueRepository.findByProjectIdAndTaskId(projectId, taskId);
        return pokerValues.stream()
                .map(Poker_Voting_Entity::getPokerValue)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getUserIdsByProjectAndPokerValue(String projectId, Integer pokerValue) {
        List<Poker_Voting_Entity> pokerValues = pokerValueRepository.findByProjectIdAndPokerValue(projectId, pokerValue);
        return pokerValues.stream()
                .map(Poker_Voting_Entity::getUserId)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Poker_Voting_Entity> getPokerVotingEntitiesByProjectAndTask(String projectId, String taskId) {
        return pokerValueRepository.findByProjectIdAndTaskId(projectId, taskId);
    }



}
