package com.projectTwo.AS_PMT_Project_User_Service.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeGetterUtil {
	
	//"Asia/Colombo"
	private Calendar customCalendar;
	
	public DateTimeGetterUtil(String TimeZoneName)
	{
		customCalendar = Calendar.getInstance(TimeZone.getTimeZone(TimeZoneName));
	}
	
	public Date getCurrent()
	{
		// Get the Date object from the custom Calendar
		return customCalendar.getTime();
	}
	

	

	// Set the custom created date to your project object
	//project.setCreatedDate(customDate);

}
