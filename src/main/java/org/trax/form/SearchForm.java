package org.trax.form;

import javax.validation.constraints.NotNull;

import org.trax.model.BaseUnitType;

public class SearchForm
{
	private long scoutId;
	private Integer unitNumber;
	private String councilName;
	private String stateName;
	private BaseUnitType typeOfUnit;
	
	public long getScoutId()
	{
		return scoutId;
	}
	
	public void setScoutId(long scoutId)
	{
		this.scoutId = scoutId;
	}
	@NotNull(message="Must specify the Troop, Team or Crew number")
	public Integer getUnitNumber()
	{
		return unitNumber;
	}
	
	public void setUnitNumber(Integer unitNumber)
	{
		this.unitNumber = unitNumber;
	}

	public void setCouncilName(String councilName)
	{
		this.councilName = councilName;
	}
	@NotNull(message="Must specify the Council")
	public String getCouncilName()
	{
		return councilName;
	}

	public void setStateName(String stateName)
	{
		this.stateName = stateName;
	}
	@NotNull(message="Must specify the State")
	public String getStateName()
	{
		return stateName;
	}
	
	public BaseUnitType getTypeOfUnit()
	{
		return typeOfUnit;
	}
	public void setTypeOfUnit(BaseUnitType typeOfUnit)
	{
		this.typeOfUnit = typeOfUnit;
	}
}
