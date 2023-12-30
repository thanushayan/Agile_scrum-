package com.projectTwo.AS_PMT_Project_Service.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectTwo.AS_PMT_Project_Service.Entities.Issue_Entity;

public interface IssueRepository  extends JpaRepository<Issue_Entity, Long> {
	Issue_Entity findByIssueId(String issueID);

	List<Issue_Entity> findByProjectId(String projectId);

}
