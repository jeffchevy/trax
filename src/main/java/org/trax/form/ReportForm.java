package org.trax.form;

import java.util.Date;

import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

public class ReportForm
{
	@Past @DateTimeFormat(style = "S-") //Short date only, no time
	private Date startDate;
	@DateTimeFormat(style = "S-") //Short date only, no time
	private Date endDate;
	
	public Date getStartDate()
	{
		return startDate;
	}
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}
	public Date getEndDate()
	{
		return endDate;
	}
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}
}
