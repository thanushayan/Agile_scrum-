package com.projectTwo.AS_PMT_Project_Service.Entities;
import lombok.*;
import jakarta.persistence.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.projectTwo.AS_PMT_Project_Service.utils.TimeFormatterUtil;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sprints")
public class Sprint_Entity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sprint_id", unique = true, nullable = false)
    private String sprintId;

    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description", nullable = true)
    private String description;
    
    @Column(name = "creator_id", nullable = false)
    private String creatorId;
    
    @Column(name = "project_id", nullable = false)
    private String projectId;

    @Column(name = "start_date", nullable = true)
    private Date startDate;

    @Column(name = "end_date", nullable = true)
    private Date endDate;
    
    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "last_updated_date", nullable = true)
    private Date lastUpdatedDate;
    
    @ManyToMany(targetEntity = Issue_Entity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "Sprint_Tasks", referencedColumnName = "project_id")
    private List<Issue_Entity> Issue = new ArrayList<>();

   
    
    
    //TestJson
    /*
     {
  "sprintId": "SP001",
  "name": "Sample Sprint",
  "sprintType": "Agile",
  "description": "This is a sample sprint.",
  "creatorId": 12345,
  "projectId": "P001",
  "startDate": "2022-01-01",
  "endDate": "2022-12-31",
  "createdDate": "2022-01-01T12:00:00Z",
  "lastUpdatedDate": "2022-05-01T10:00:00Z"
}

*/

    // Getters
    
    public String getSprintId() {
        return sprintId;
    }

    public String getName() {
        return name;
    }
    
    
    public String getDescription() {
        return description;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getStartDate() {
    	if(startDate==null)
    	{
    		return null;
    	}
    	return new TimeFormatterUtil("dd-MM-yyyy").formatTheDate(startDate);
    	//return startDate;
    }

    public String getEndDate() {
    	if(endDate==null)
    	{
    		return null;
    	}
    	return new TimeFormatterUtil("dd-MM-yyyy").formatTheDate(endDate);
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
    
    public List<Issue_Entity> getIssues() {
        return Issue;
    }
    

    //Setters

    public void setSprintId(String sprintId) {
        this.sprintId = sprintId;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    
    //Aditional methods
    public Date StringTodate(String date) throws ParseException
    {
    	String dateString = date;
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    	return dateFormat.parse(dateString);
    }
}

