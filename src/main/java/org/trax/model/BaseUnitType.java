package org.trax.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@SuppressWarnings("serial")
@Entity
@DiscriminatorColumn(name="Kind", discriminatorType=DiscriminatorType.STRING, length=1)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Table(name="UnitType", uniqueConstraints = {@UniqueConstraint(columnNames={"name"})})
public class BaseUnitType implements Serializable
{

	private long id;
	private int sortOrder;
	private String name;
	private int startAge;
	private int endAge;

	public BaseUnitType()
	{
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public long getId()
	{
	    return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	@Column(name = "name", nullable = false, unique = true, length = 30)
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Column(name = "sortOrder")
	public int getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(int sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	@Column(name = "startAge", unique = false)
	public int getStartAge()
	{
		return startAge;
	}

	public void setStartAge(int startAge)
	{
		this.startAge = startAge;
	}

	@Column(name = "endAge", unique = false)
	public int getEndAge()
	{
		return endAge;
	}

	public void setEndAge(int endAge)
	{
		this.endAge = endAge;
	}

	@Override
	public boolean equals(Object other)
	{ 
		if(other instanceof Long)
		{
			return other.equals(getId());
		}
		BaseUnitType unit = (BaseUnitType)other;
		return new EqualsBuilder().append(getId(), unit.getId()).isEquals();
	}

	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

}