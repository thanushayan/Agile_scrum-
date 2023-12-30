package com.projectTwo.AS_PMT_Userstory_Service.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.projectTwo.AS_PMT_Userstory_Service.utils.TimeFormatterUtil;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_stories")
public class Userstory_Entity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "userstory_id", unique = true, nullable = false)
	private String userStoryId;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "acceptance_criteria", nullable = false, length = 2000)
	private String acceptanceCriteria;

	@Column(name = "additional_notes", length = 2000)
	private String additionalNotes;

	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	@Column(name = "updated_date", nullable = false)
	private Date updatedDate;

	@Column(name = "project_id", nullable = false)
	private String projectId;

	@Column(name = "sprint_id", nullable = false)
	private String sprintId;

	// Getters and Setters
	// Getters
	public Long getId() {
		return id;
	}
	
	public String getUserStoryID() {
		return userStoryId;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getAcceptanceCriteria() {
		return acceptanceCriteria;
	}

	public String getAdditionalNotes() {
		return additionalNotes;
	}

	public String getCreatedDate() {
		if(createdDate==null)
    	{
    		return null;
    	}
    	return new TimeFormatterUtil("yyyy.MM.dd G 'at' HH:mm:ss z").formatTheDate(createdDate);
	}

	public String getUpdatedDate() {
		if(createdDate==null)
    	{
    		return null;
    	}
    	return new TimeFormatterUtil("yyyy.MM.dd G 'at' HH:mm:ss z").formatTheDate(updatedDate);
	}

	public String getProjectId() {
		return projectId;
	}

	public String getSprintId() {
		return sprintId;
	}

	// Setters
	public void setUserStoryID(String id) {
		this.userStoryId = id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAcceptanceCriteria(String acceptanceCriteria) {
		this.acceptanceCriteria = acceptanceCriteria;
	}

	public void setAdditionalNotes(String additionalNotes) {
		this.additionalNotes = additionalNotes;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public void setSprintId(String sprintId) {
		this.sprintId = sprintId;
	}
	
	public Date StringTodate(String date) throws ParseException
    {
    	String dateString = date;
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    	return dateFormat.parse(dateString);
    }
}
