package com.projectTwo.AS_PMT_Userstory_Service.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatterUtil {
	String formatSTR;
	
	public TimeFormatterUtil(String format)
	{
		this.formatSTR = format;
	}
	
	public String formatTheDate(Date date)
	{
		return new SimpleDateFormat(formatSTR).format(date);
	}
}
