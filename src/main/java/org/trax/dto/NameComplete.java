package org.trax.dto;

public class NameComplete
{
	private String fullName;
	private String complete; //datestring or % complete
	
	public NameComplete(String fullName, String complete)
	{
		this.fullName = fullName;
		this.complete = complete;
	}

	public String getFullName()
	{
		return fullName;
	}

	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}

	public String getComplete()
	{
		return complete;
	}

	public void setComplete(String complete)
	{
		this.complete = complete;
	}
}
