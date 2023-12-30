package com.projectTwo.AS_PMT_Project_User_Service.Entities;

import java.util.Date;

import com.projectTwo.AS_PMT_Project_User_Service.utils.TimeFormatterUtil;

import jakarta.persistence.*;

@Entity
@Table(name = "project_user_relation")
public class ProjectAndUserEntity {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "project_id",nullable = false)
	    private String projectId;

	    @Column(name = "user_id" ,nullable = false)
	    private String userId;
	    
	    @Column(name = "user_Role" ,nullable = false)
	    private String userRole;
	    
	    @Column(name = "added_date" ,nullable = false)
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
