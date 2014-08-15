package org.trax.model;


public class RequirementConfigImport
{
	private int sortOrder;
	private String text;
	private Boolean leaderAuthorized;
	private long id;
 
    
	protected void setId(long id)
    {
        this.id = id;
    }
	
    public long getId()
    {
        return id;
    }
	
	public int getSortOrder()
	{
		return sortOrder;
	}
	
	public void setSortOrder(int sortOrder)
	{
		this.sortOrder = sortOrder;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}

	public Boolean getLeaderAuthorized() {
		return leaderAuthorized;
	}
	public void setLeaderAuthorized(Boolean leaderAuthorized) {
		this.leaderAuthorized = leaderAuthorized;
	}
}
