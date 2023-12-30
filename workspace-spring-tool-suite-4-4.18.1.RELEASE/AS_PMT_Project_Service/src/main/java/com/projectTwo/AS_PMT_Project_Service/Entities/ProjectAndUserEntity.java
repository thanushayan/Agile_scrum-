package com.projectTwo.AS_PMT_Project_Service.Entities;

import java.util.Date;

import com.projectTwo.AS_PMT_Project_Service.utils.TimeFormatterUtil;

import jakarta.persistence.*;

public class ProjectAndUserEntity {
	
	    private Long id;

	    private String projectId;

	    private String userId;

	    private String userRole;

	    private Date addedDate;

	    // Constructors, getters, and setters

	    public ProjectAndUserEntity() {
	    }

	    public ProjectAndUserEntity(String projectId, String userId, String userRole) {
	        this.projectId = projectId;
	        this.userId = userId;
	        this.userRole = userRole;
	    }

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getProjectId() {
	        return projectId;
	    }

	    public void setProjectId(String projectId) {
	        this.projectId = projectId;
	    }

	    public String getUserId() {
	        return userId;
	    }

	    public void setUserId(String userId) {
	        this.userId = userId;
	    }

	    public String getUserRole() {
	        return userRole;
	    }

	    public void setUserRole(String userRole) {
	        this.userRole = userRole;
	    }
	    
	    public String getAddedDate() {
	    	if(addedDate==null)
	    	{
	    		return null;
	    	}
	    	return new TimeFormatterUtil("yyyy-MM-dd").formatTheDate(addedDate);
	    	//return startDate;
	    }
	    
	    public void setAddedDate(Date addedDate) {
	        this.addedDate = addedDate;
	    }
}
