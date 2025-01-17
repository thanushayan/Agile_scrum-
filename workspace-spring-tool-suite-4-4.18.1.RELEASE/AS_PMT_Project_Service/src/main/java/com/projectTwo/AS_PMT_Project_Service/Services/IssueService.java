package com.projectTwo.AS_PMT_Project_Service.Services;

import com.projectTwo.AS_PMT_Project_Service.Entities.Issue_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.ScrumBoard;
import com.projectTwo.AS_PMT_Project_Service.Repositories.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class IssueService {
    private final IssueRepository issueRepository;
    private final ScrumBoardService scrumBoardService;

    @Autowired
    public IssueService(IssueRepository issueRepository, ScrumBoardService scrumBoardService) {
        this.issueRepository = issueRepository;
        this.scrumBoardService = scrumBoardService;
    }

    public Issue_Entity createissue(Issue_Entity issue) {
    	String issueId = generateUniqueissueId(issue.getTitle());
    	issue.setIssueId(issueId);
        return issueRepository.save(issue);
    }

    public List<Issue_Entity> getAllissues() {
        return issueRepository.findAll();
    }

    public Issue_Entity getIssueById(Long Id) {
        Optional<Issue_Entity> optionalissue = issueRepository.findById(Id);
        return optionalissue.orElse(null);
    }

    public Issue_Entity updateissue(Long issueId, Issue_Entity updatedIssue) {
    	Issue_Entity issue = getIssueById(issueId);
        if (issue == null) {
            return null;
        }
        issue.setTitle(updatedIssue.getTitle());
        issue.setDescription(updatedIssue.getDescription());
        // Update any other fields as needed
        return issueRepository.save(issue);
    }

    public boolean deleteissue(Long issueId) {
        Issue_Entity issue = getIssueById(issueId);
        if (issue == null) {
            return false;
        }
        issueRepository.delete(issue);
        return true;
    }
    
    private String generateUniqueissueId(String issueName) {
		long timestamp = new Date().getTime();

		// Generate a random number or alphanumeric string
		Random random = new Random();
		UUID uuid = UUID.randomUUID();

		String uuidGen = uuid.toString();
		int randomNumber = random.nextInt(1000); // Generate a random number between 0 and 999

		// Combine the timestamp and random number to create the project ID
		String issueID = ("value=" + uuidGen + "ts=" + String.valueOf(timestamp) + String.format("%03d", randomNumber)
				+ "name=" + issueName);

		return issueID;
		// Logic to generate a unique project ID
	}
    
    public Issue_Entity updateIssueStatus(Long issueId, String statusName) {
        try {
            Issue_Entity issue = issueRepository.findById(issueId)
                    .orElseThrow(() -> new IllegalArgumentException("Issue not found with id: " + issueId));
            
            ScrumBoard newStatus = scrumBoardService.getScrumBoardByStatusName(statusName);
            if(newStatus==null)
            {
            	newStatus = new ScrumBoard(statusName);
            }
           
            issue.setStatus(newStatus);
            return issueRepository.save(issue);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid issueId or statusName");
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while updating issue status");
        }
    }
    
   
        // Method to update an issue
        public Issue_Entity updateIssue(Issue_Entity updatedIssue) {
            // Perform validation or additional operations if needed before saving
            // For instance, you might want to check if the issue exists in the database

            // Save the updated issue using the repository's save method
            return issueRepository.save(updatedIssue);
        }

		public List<Issue_Entity> getIssueByProjectId(String projectId) {
			return issueRepository.findByProjectId(projectId);
		}

        // Other methods related to issue management...
    }



