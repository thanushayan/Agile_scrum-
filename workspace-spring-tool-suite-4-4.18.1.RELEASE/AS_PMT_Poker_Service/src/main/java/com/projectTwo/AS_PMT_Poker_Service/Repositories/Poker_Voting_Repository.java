package com.projectTwo.AS_PMT_Poker_Service.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // Add this import

import com.projectTwo.AS_PMT_Poker_Service.Entities.Poker_Voting_Entity;

public interface Poker_Voting_Repository extends JpaRepository<Poker_Voting_Entity, Long> {
    Optional<Poker_Voting_Entity> findByUserIdAndProjectIdAndTaskId(String userId, String projectId, String taskId);
    
    @Query("SELECT p.pokerValue FROM Poker_Voting_Entity p WHERE p.projectId = :projectId AND p.taskId = :taskId")
    List<Integer> findPokerValuesByProjectIdAndTaskId(@Param("projectId") String projectId, @Param("taskId") String taskId);

	List<Poker_Voting_Entity> findByProjectIdAndPokerValue(String projectId, Integer pokerValue);

	List<Poker_Voting_Entity> findByProjectIdAndTaskId(String projectId, String taskId);
}
