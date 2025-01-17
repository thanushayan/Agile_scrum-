package com.projectTwo.AS_PMT_Project_Service.Entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.projectTwo.AS_PMT_Project_Service.utils.TimeFormatterUtil;

import jakarta.persistence.*;

@Entity
@Table(name = "Issues")
public class Issue_Entity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "issueId", nullable = true)
    private String issueId;
    
    @Column(name = "projectId", nullable = true)
    private String projectId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = true)
    private String description;
    
    @ManyToOne 
    @JoinColumn(name = "scrumboard_id") // Name of the column in Issue_Entity referencing ScrumBoard
    private ScrumBoard status;

    @Column(name = "start_date", nullable = true)
    private Date startDate;
    
    @Column(name = "type", nullable = false)
    private IssueType type;

    @Column(name = "created_by", nullable = false)
    private String createdBy;
    
    @Column(name = "priority", nullable = true)
    private String priority;

    @Column(name = "due_date", nullable = true)
    private Date dueDate;

    @Column(name = "assignee", nullable = true)
    private String assignee;

    @Column(name = "comment", nullable = true)
    private String comment;
    
    @Column(name = "progress", nullable = true)
    private float progress;

    // Constructors, getters, and setters

    public Issue_Entity(String comment, String projectId, String issueId, String title, String description, String priority, Date dueDate, IssueType type, String createdBy, String assignee, Date startDate, ScrumBoard status) {
        this.title = title;
        this.projectId = projectId;
        this.description = description;
        this.priority = priority;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.type = type;
        this.status = status;
        this.createdBy = createdBy;
        this.issueId = issueId;
        this.assignee = assignee;
        this.comment = comment;
    }

    // Getters and setters for the new fields

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDueDate() {
    	if(startDate==null)
    	{
    		return null;
    	}
    	return new TimeFormatterUtil("yyyy-MM-dd").formatTheDate(dueDate);
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    // Constructors, getters, and setters
    
    public Issue_Entity() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void setType(IssueType type)
    {
    	this.type = type;
    }
    
    public IssueType getType()
    {
    	return type;
    }
    
    public String getIssueId() {
        return issueId;
    }
    
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    
    public String getProjectId() {
        return issueId;
    }
    
    public void setProjectId(String issueId) {
        this.issueId = issueId;
    }
    
    public ScrumBoard getStatus() {
        return status;
    }
    
    public void setStatus(ScrumBoard status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
    	if(startDate==null)
    	{
    		return null;
    	}
    	return new TimeFormatterUtil("yyyy-MM-dd").formatTheDate(startDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }
    
    public Date StringTodate(String date) throws ParseException
    {
    	String dateString = date;
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	return dateFormat.parse(dateString);
    }
}
