package org.trax.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@SuppressWarnings("serial")
@Entity
@Table(name="RequirementConfig")
public class RequirementConfig implements Comparable<RequirementConfig>, Serializable
{
	private int sortOrder;
	private String text;
	private Boolean leaderAuthorized;
	private long id;
	private AwardConfig awardConfig;
	private Boolean canSelect=true;
    
	public RequirementConfig(String text2, int sortOrder2, Boolean leaderAuthorized2)
	{
		this.setText(text2);
		this.setSortOrder(sortOrder2);
		this.setLeaderAuthorized(leaderAuthorized2);
		
	}
	//need to have a default constructor for reflection
	public RequirementConfig(){}
	
	protected void setId(long id)
    {
        this.id = id;
    }
	
	@Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    public long getId()
    {
        return id;
    }
	
	@Column (name="sortOrder", nullable=false)
	public int getSortOrder()
	{
		return sortOrder;
	}
	
	public void setSortOrder(int sortOrder)
	{
		this.sortOrder = sortOrder;
	}
	
	@Column (name="text", nullable=false,length=2500 )
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}

	//added this for use by the reflection in display of imported data
	@Column (name="leaderAuthorized", nullable=false, unique=false, columnDefinition="boolean default true")
	public Boolean getLeaderAuthorized() {
		return leaderAuthorized;
	}
	public void setLeaderAuthorized(Boolean leaderAuthorized) {
		this.leaderAuthorized = leaderAuthorized;
	}
	
	// some requirements are only for instructions, if canSelect=false, then it is an instruction, not a requirement
	@Column (name="canSelect", nullable=false, unique=false)
	public Boolean getCanSelect()
	{
		return canSelect;
	}
	public void setCanSelect(Boolean canSelect)
	{
		this.canSelect = canSelect;
	}

	@ManyToOne
	@JoinColumn(name = "awardConfigId")
	public AwardConfig getAwardConfig()
	{
		return awardConfig;
	}
	
	public void setAwardConfig(AwardConfig awardConfig)
	{
		this.awardConfig = awardConfig;
	}
	
	public int compareTo(RequirementConfig rc2)
	{
		if(this.getAwardConfig().getId()==rc2.getAwardConfig().getId())
		{
			return rc2.sortOrder-this.sortOrder;
		}
		return (int) (rc2.getAwardConfig().getId()-this.getAwardConfig().getId());
	}
	
	public boolean equals(RequirementConfig rc) 
	{ 
		return new EqualsBuilder().append(getAwardConfig(), rc.getAwardConfig())
								.append(getSortOrder(), rc.getSortOrder()).isEquals();
	}
	
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(getAwardConfig()).append(getSortOrder()).toHashCode();
	}
}
