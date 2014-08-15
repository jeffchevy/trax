/*
 * Created on Oct 18, 2004
 */
package org.trax.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.ForeignKey;

@SuppressWarnings("serial")
@Entity
@Table(name="Organization")
public class Organization implements Serializable
{
    @NotNull(message="Must specify the city")
    @Size(min=1, message="Must specify the city")
    private String city;
    
    @NotNull(message="Must specify the state")
    @Size(min=1)
    private String state;
    
    @NotNull(message="Must specify the Council")
    @Size(min=1, message="Must specify the Council")
    private String council;
    
    private String district;
    //NotNull(message="Must specify the Charter Organization")
    //Size(min=1, message="Must specify the Charter Organization")
    private String name;

    private boolean accountEnabled;
    private boolean hasTigers;
    private long id;
    private Set<Unit> units;
    
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

    @Column (name="city", nullable=false, unique=false)
    public String getCity()
    {
        return city;
    }


    public void setCity(String city)
    {
        this.city = city;
    }


    @Column (name="state", nullable=false, unique=false)
    public String getState()
    {
        return state;
    }


    public void setState(String state)
    {
        this.state = state;
    }

    @Column (name="accountEnabled", nullable=false, unique=false)
    public boolean getAccountEnabled()
    {
        return accountEnabled;
    }

    public void setAccountEnabled(boolean accountEnabled)
    {
        this.accountEnabled = accountEnabled;
    }
    @Column (name="hasTigers", nullable=false, unique=false)
    public boolean getHasTigers()
    {
        return hasTigers;
    }

    public void setHasTigers(boolean hasTigers)
    {
        this.hasTigers = hasTigers;
    }
    @Transient
	public boolean hasTigers()
	{
		return hasTigers;
	}
    
	@Transient
	public boolean isEnabled()
	{
		return false;
	}

	@Column (name="council", nullable=false, unique=false)
	public String getCouncil()
	{
		return council;
	}

	public void setCouncil(String council)
	{
		this.council = council;
	}
	
	@Column (name="district", nullable=true, unique=false)
	public String getDistrict()
	{
		return district;
	}

	public void setDistrict(String district)
	{
		this.district = district;
	}

	@Column (name="name", length=500, nullable=true, unique=false)
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	@OneToMany(mappedBy="organization", cascade=CascadeType.ALL, fetch=FetchType.EAGER) 
	@ForeignKey(name = "FK_ORGANIZATION_UNIT")
	//OneToMany(targetEntity =  Unit.class, mappedBy = "organization" ,cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Set<Unit> getUnits()
	{
		return units;
	}

	 public void setUnits(Set<Unit> units)
	{
		this.units = units;
	}
	@Override
	public boolean equals(Object other) 
	{ 
		Organization otherOrg = (Organization)other;
		return new EqualsBuilder().append(this.getId(), otherOrg.getId()).isEquals();
		//TODO should make this based on these fields, but right not garanteed unique
			//.append(this.getCouncil(), otherOrg.getCouncil())
			//.append(this.getState(), otherOrg.getState())
			//.append(this.getName(), otherOrg.getName()).isEquals();
	}
	
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(this.getId()).toHashCode();
	}
}