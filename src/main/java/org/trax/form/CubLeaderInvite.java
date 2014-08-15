package org.trax.form;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.trax.model.BaseUnitType;
import org.trax.model.Leader.CubLeaderPosition;

public class CubLeaderInvite
{
	@NotNull(message="First name cannot be null")
    @Size(min=1, max=64, message="First name cannot be null") 
    private String firstName;
 
    @NotNull(message="Last name cannot be null")
    @Size(min=1, message="Last name cannot be null")
    private String lastName;
    
    @Email
    private String email;
    
    @NotNull(message="Must specify the Unit number (should be 4 digit)")
    private Integer number;
    private BaseUnitType typeOfUnit;
    @Enumerated(EnumType.STRING)
    CubLeaderPosition position;
    
    
	public CubLeaderPosition getPosition()
	{
		return position;
	}
	public void setPosition(CubLeaderPosition position)
	{
		this.position = position;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
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
		return typeOfUnit;
	}
	public void setTypeOfUnit(BaseUnitType typeOfUnit)
	{
		this.typeOfUnit = typeOfUnit;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getEmail()
	{
		return email;
	}
}
