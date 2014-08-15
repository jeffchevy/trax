package org.trax.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.trax.model.BaseUnitType;


public class OrgUnit
{
	@NotNull(message = "Must specify the unit number")
	private Integer number;
	@NotNull(message = "Must specify the unit type")
	private BaseUnitType unitType;
	@NotNull(message = "Must specify the city")
	@Size(min=1, max=150, message = "Must specify the city")
	private String city;
	@NotNull(message = "Must specify the state")
	@Size(min=1)
	private String state;
	@NotNull(message = "Must specify the Council")
	@Size(min=1)
	private String council;
	private String type;
	private String district;
	@NotNull(message="Must specify the Charter Organization")
	@Size(min=1, max=150, message = "Must specify the Charter Organization")
	private String name;
	private boolean hasTigers;
	public Integer getNumber()
	{
		return number;
	}
	public void setNumber(Integer number)
	{
		this.number = number;
	}
	public BaseUnitType getTypeOfUnit()
	{
		return unitType;
	}
	public void setTypeOfUnit(BaseUnitType unitType)
	{
		this.unitType = unitType;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city = city;
	}
	public String getState()
	{
		return state;
	}
	public void setState(String state)
	{
		this.state = state;
	}
	public String getCouncil()
	{
		return council;
	}
	public void setCouncil(String council)
	{
		this.council = council;
	}
	public String getDistrict()
	{
		return district;
	}
	public void setDistrict(String district)
	{
		this.district = district;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public boolean isAccountEnabled()
	{
		return accountEnabled;
	}
	public void setAccountEnabled(boolean accountEnabled)
	{
		this.accountEnabled = accountEnabled;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getType()
	{
		return type;
	}
	public void setHasTigers(boolean hasTigers)
	{
		this.hasTigers = hasTigers;
	}
	public boolean getHasTigers()
	{
		return hasTigers;
	}
	private boolean accountEnabled;
}
