package com.projectTwo.AS_PMT_Project_Service.Entities;

import com.projectTwo.AS_PMT_Project_Service.utils.TimeFormatterUtil;

import lombok.*;
import jakarta.persistence.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
public class Project_Entity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "project_id", unique = true, nullable = false)
    private String projectId;

    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "project_type", nullable = false)
    private String projectType;
    
    @Column(name = "description", nullable = false)
    private String description;
    
    @Column(name = "creator_id", nullable = false)
    private String creatorId;
    
    @Column(name = "project_manager_id", nullable = false)
    private String projectManagerId;
    
    @Column(name = "admin_id", nullable = false)
    private String adminId;

    @Column(name = "start_date", nullable = true)
    private Date startDate;

    @Column(name = "end_date", nullable = true)
    private Date endDate;
    
    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "last_updated_date", nullable = true)
    private Date lastUpdatedDate;
    
    @OneToOne(targetEntity = Backlog_Entity.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "Project_Backlog", referencedColumnName = "project_id")
    private Backlog_Entity backlog;
    
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinTable(
        name = "active_sprints",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "active_sprint_id")
    )
    private Sprint_Entity activeSprint;
    
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinTable(
        name = "poker_Issue",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "poker_issue_id")
    )
    private Issue_Entity pokerIssue;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
        name = "project_sprints",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "sprint_id")
    )
    private List<Sprint_Entity> sprints = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
        name = "project_requestPending",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<RequestPending_Entity> requestPending = new ArrayList<>();

    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "project_userRoles",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<UserRoles> roles = new ArrayList<>();
    
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "project_Boards",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<ScrumBoard> status = new ArrayList<>();
    
    @OneToOne(targetEntity = ProjectPermissions.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ProjectPermissions", referencedColumnName = "project_id")
    private ProjectPermissions permissions;
   
    
    
    //TestJson
    /*
     {
  		"name": "Sample Project",
  		"startDate": "2022-01-01",
  		"endDate": "2022-12-31",
  		"description": "This is a sample project."
  		"projectType": "Software"
	}
*/

    // Getters
   
    public String getProjectId() {
        return projectId;
    }
    
    public String getAdminId()
    {
    	return adminId;
    }

    public String getName() {
        return name;
    }
    
    public String getProjectType() {
        return projectType;
    }
    
    public String getDescription() {
        return description;
    }

    public String getProjectManagerId() {
        return projectManagerId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getStartDate() {
    	if(startDate==null)
    	{
    		return null;
    	}
    	return new TimeFormatterUtil("yyyy-MM-dd").formatTheDate(startDate);
    	//return startDate;
    }

    public String getEndDate() {
    	if(endDate==null)
    	{
    		return null;
    	}
    	return new TimeFormatterUtil("yyyy-MM-dd").formatTheDate(endDate);
    	//return endDate;
    }

    public String getCreatedDate() {
    	if(createdDate==null)
    	{
    		return null;
    	}
    	return new TimeFormatterUtil("yyyy.MM.dd G 'at' HH:mm:ss z").formatTheDate(createdDate);
    }

    public String getLastUpdatedDate() {
    	if(lastUpdatedDate==null)
    	{
    		return null;
    	}
        return new TimeFormatterUtil("yyyy.MM.dd G 'at' HH:mm:ss z").formatTheDate(lastUpdatedDate);
    }

    //Setters

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    
    public void setAdminId(String adminId)
    {
    	this.adminId = adminId;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setProjectType(String type) {
        this.projectType = type;
    }

    public void setProjectManagerId(String projectManagerId) {
        this.projectManagerId = projectManagerId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
    

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

	public void setEndDate(Date endDate) {
		this.endDate =  endDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    public List<Sprint_Entity> getSprints() {
        return sprints;
    }
    
    public Backlog_Entity getBacklog() {
        return backlog;
    }

    public void setBacklog(Backlog_Entity backlog) {
        this.backlog = backlog;
    }

    public ProjectPermissions getPermissions() {
        return permissions;
    }

    public void setPermissions(ProjectPermissions permissions) {
        this.permissions = permissions;
    }
    
    public List <UserRoles> getUserRoles() {
        return roles;
    }

    public void setUserRoles(List <UserRoles> roles) {
        this.roles = roles;
    }
    
    public Sprint_Entity getActiveSprint() {
        return activeSprint;
    }
    
    public void setStatus(List <ScrumBoard> status) {
        this.status = status;
    }
    
    public List<ScrumBoard> getStatus() {
        return status;
    }
    
    public void setPokerIssue(Issue_Entity issue) {
        this.pokerIssue = issue;
    }
    
    public Issue_Entity getPokerIssue() {
        return pokerIssue;
    }

    public void setActiveSprint(Sprint_Entity activeSprint) {
        this.activeSprint = activeSprint;
    }
 
    //Aditional methods
    public Date StringTodate(String date) throws ParseException
    {
    	String dateString = date;
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	return dateFormat.parse(dateString);
    }

	public void addRole(UserRoles userRoles) {
		roles.add(userRoles);
	}

   

    
    

    
}
