package com.projectTwo.AS_PMT_Poker_Service.Services;

import java.util.List;

import com.projectTwo.AS_PMT_Poker_Service.Entities.Poker_Voting_Entity;

public interface Poker_Vote_Service {
	    Poker_Voting_Entity createOrUpdatePokerValue(String userId, String projectId, String taskId, Integer pokerValue);
	    Integer getPokerValue(String userId, String projectId, String taskId);
	    void deletePokerValue(String userId, String projectId, String taskId);
	    
	    List<Integer> getPokerValuesByProjectAndTask(String projectId, String taskId);
	    
	    List<String> getUserIdsByProjectAndPokerValue(String projectId, Integer pokerValue);
	    
	    List<Poker_Voting_Entity> getPokerVotingEntitiesByProjectAndTask(String projectId, String taskId);


}
