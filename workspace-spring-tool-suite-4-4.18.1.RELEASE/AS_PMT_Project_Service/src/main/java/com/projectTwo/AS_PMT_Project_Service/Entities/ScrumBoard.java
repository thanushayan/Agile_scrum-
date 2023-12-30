package com.projectTwo.AS_PMT_Project_Service.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ScrumBoard")
public class ScrumBoard {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	@Column(name = "status_name", columnDefinition = "VARCHAR(255)")
	String statusName;
	
	public ScrumBoard()
	{
		
	}
	
	public ScrumBoard(String name)
	{
		this.statusName = name;
	}
	
	public String getStatusName()
	{
		return statusName;
	}
	
	public void setStatusName(String statusName)
	{
		this.statusName = statusName;
	}
	 
	 
}
