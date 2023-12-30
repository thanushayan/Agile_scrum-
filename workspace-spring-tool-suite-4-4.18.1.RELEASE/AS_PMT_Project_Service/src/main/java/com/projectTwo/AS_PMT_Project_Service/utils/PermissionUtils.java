package com.projectTwo.AS_PMT_Project_Service.utils;

import java.util.List;

import com.projectTwo.AS_PMT_Project_Service.Entities.Sprint_Entity;
import com.projectTwo.AS_PMT_Project_Service.Entities.UserRoles;

public class PermissionUtils {
	private static PermissionUtils instance = new PermissionUtils();
	
	public static PermissionUtils getInstance()
	{
		if(instance.equals(null))
		{
			instance = new PermissionUtils();
		}
		return instance;
	}
	
	private PermissionUtils()
	{
		
	}
	
	public boolean isPermitted(List <UserRoles> roles, String testingRole)
	{
		boolean bool = false;
		for(UserRoles role : roles)
		{
			if(role.getRoleName().equalsIgnoreCase(testingRole))
			{
				bool = true;
				break;
			}
		}
		return bool;
	}
	
	public boolean sprintExistInProject(List <Sprint_Entity> sprints, String sprintId)
	{
		boolean bool = false;
		for(Sprint_Entity sprint : sprints)
		{
			if(sprint.getSprintId().equalsIgnoreCase(sprintId))
			{
				bool = true;
				break;
			}
		}
		return bool;
	}
}
