package org.trax.dto;

public class NameComplete
{
	private String fullName;
	private String complete; //datestring or % complete
	private boolean awarded;
	private boolean purchased;
	
	public NameComplete(String fullName, String complete, boolean awarded, boolean purchased)
	{
		this.setFullName(fullName);
		this.setComplete(complete);
		this .setAwarded(awarded);
		this.setPurchased(purchased);
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

	public boolean isAwarded()
	{
		return awarded;
	}

	public void setAwarded(boolean awarded)
	{
		this.awarded = awarded;
	}

	public boolean isPurchased()
	{
		return purchased;
	}

	public void setPurchased(boolean purchased)
	{
		this.purchased = purchased;
	}
}
