package org.trax.form;

import org.trax.model.BaseUnitType;
import org.trax.model.Leader;
import org.trax.model.Organization;

public class ScoutTransfer
{
	private long scoutId;
	private Organization org;
	private Leader leader;
	private int unitNumber;
	private BaseUnitType typeOfUnit;
	private String stateName;
	private String councilName;
	
	public long getScoutId()
	{
		return scoutId;
	}
	public void setScoutId(long scoutId)
	{
		this.scoutId = scoutId;
	}
	public Organization getOrg()
	{
		return org;
	}
	public void setOrg(Organization org)
	{
		this.org = org;
	}
	public Leader getLeader()
	{
		return leader;
	}
	public void setLeader(Leader leader)
	{
		this.leader = leader;
	}
	public int getUnitNumber()
	{
		return unitNumber;
	}
	public void setUnitNumber(int number)
	{
		this.unitNumber = number;
	}
	public BaseUnitType getTypeOfUnit()
	{
		return typeOfUnit;
	}
	public void setTypeOfUnit(BaseUnitType typeOfUnit)
	{
		this.typeOfUnit = typeOfUnit;
	}
	public String getStateName()
	{
		return stateName;
	}
	public void setStateName(String stateName)
	{
		this.stateName = stateName;
	}
	public String getCouncilName()
	{
		return councilName;
	}
	public void setCouncilName(String councilName)
	{
		this.councilName = councilName;
	}
}
