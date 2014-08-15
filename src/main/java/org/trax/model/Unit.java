/*
 * Created on Oct 18, 2004
 */
package org.trax.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.ForeignKey;
import org.trax.model.cub.CubUnitType;

@SuppressWarnings("serial")
@Entity
@Table(name="Unit")
public class Unit implements Serializable
{
    private long id;
    @NotNull(message="Must specify the Unit number (should be 4 digit)")
    private Integer number;
    private String calendar;
    private BaseUnitType typeOfUnit;
    private Organization organization;
    
    @ManyToOne(fetch=FetchType.EAGER)    
    @JoinColumn(name="unitTypeId", nullable=true) 
    @ForeignKey(name="FK_UNIT_UNITTYPE") 
    public BaseUnitType getTypeOfUnit()
    {
        return typeOfUnit;
    }

    public void setTypeOfUnit(BaseUnitType typeOfUnit)
    {
        this.typeOfUnit = typeOfUnit;
    }
	
    public Unit()
    {
    }
	
    public Unit(BaseUnitType typeOfUnit, Integer number)
	{
    	this.setNumber(number);
    	this.setTypeOfUnit(typeOfUnit);
	}

	
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    public long getId()
    {
        return id;
    }
	 
	public void setId(long id)
	{
		this.id = id;
	}

    @Column (name="number", nullable=false, unique=false)
	public Integer getNumber()
	{
		return number;
	}

	public void setNumber(Integer number)
	{
		this.number = number;
	}
	
	@Column (name="calendar", nullable=true,length=2500 )
	public String getCalendar()
	{
		return calendar;
	}
	public void setCalendar(String calendar)
	{
		this.calendar = calendar;
	}
	
	@Transient
	public boolean getIsCub()
	{
		return (typeOfUnit instanceof CubUnitType);
	}
	
	@Transient
	public boolean isCub()
	{
		return (typeOfUnit instanceof CubUnitType);
	}
	
	@Override
	public boolean equals(Object other) 
	{ 
		Unit unit = (Unit)other;
		return new EqualsBuilder().append(getOrganization(), unit.getOrganization()).append(getTypeOfUnit().getName(), unit.getTypeOfUnit().getName()).isEquals();
//		return new EqualsBuilder().append(getId(), unit.getId()).isEquals();
	}
	
	@Override
	public final int hashCode()
	{
		try
		{
			return new HashCodeBuilder().append(getOrganization()).append(getTypeOfUnit().getName()).toHashCode();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			System.out.println("There appears to be a problem with the data, TODO fix this, right now just printing out the stack trace!");
			//e.printStackTrace();
		}
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	@ManyToOne
	@JoinColumn(name="organizationId")
	public Organization getOrganization()
	{
		return organization;
	}
	public void setOrganization(Organization organization)
	{
		this.organization = organization;
	}
}
