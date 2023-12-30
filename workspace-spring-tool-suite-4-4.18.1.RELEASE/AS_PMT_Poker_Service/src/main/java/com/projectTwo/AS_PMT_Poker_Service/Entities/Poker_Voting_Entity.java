package com.projectTwo.AS_PMT_Poker_Service.Entities;

import jakarta.persistence.*;

@Entity
public class Poker_Voting_Entity  {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)

 private Long id;
 
 private String userId;
 private String projectId;
 private String taskId;
 private Integer pokerValue;
 
 
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public String getProjectId() {
	return projectId;
}
public void setProjectId(String projectId) {
	this.projectId = projectId;
}
public String getTaskId() {
	return taskId;
}
public void setTaskId(String taskId) {
	this.taskId = taskId;
}
public Integer getPokerValue() {
	return pokerValue;
}
public void setPokerValue(Integer pokerValue) {
	this.pokerValue = pokerValue;
}
 
}
