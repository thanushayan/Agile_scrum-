package com.projectTwo.AS_PMT_Project_User_Service.utils;

import java.util.List;

import com.projectTwo.AS_PMT_Project_User_Service.Entities.UserRoles;

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
}