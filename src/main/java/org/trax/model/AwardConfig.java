package org.trax.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


@SuppressWarnings("serial")
@Entity
@DiscriminatorColumn(name="Kind", discriminatorType=DiscriminatorType.STRING, length=1)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Table(name="AwardConfig", uniqueConstraints = {@UniqueConstraint(columnNames={"name", "Kind"})})
@DiscriminatorValue("A")
public class AwardConfig implements Serializable//implements Comparable<AwardConfig> 
{
	private String name;
	private String description;
	private List<RequirementConfig> requirementConfigs;
	protected int sortOrder;
	private long id;
	private Boolean required;
	protected String link;
	
	public AwardConfig(){}
	
	public AwardConfig(String name, String description, int sortOrder, Boolean required)
	{
		this.setName(name);
		this.setDescription(description);
		this.setSortOrder(sortOrder);
		this.setRequired(required);
	}

	protected void setId(long id)
	{
	    this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public long getId()
	{
	    return id;
	}

	@Column(name = "name", nullable = false)
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Column(name = "description", nullable = false)
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
    @OneToMany(targetEntity =  RequirementConfig.class, mappedBy = "awardConfig" ,cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("sortOrder")
	public List<RequirementConfig> getRequirementConfigs()
	{
		return requirementConfigs;
	}
	
	public void setRequirementConfigs(List<RequirementConfig> requirementConfigs)
	{
		this.requirementConfigs = requirementConfigs;
	}

	@Column(name = "sortOrder", nullable = false)
	public int getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(int sortOrder)
	{
		this.sortOrder = sortOrder;
	}

	@Column(name = "required", nullable = false, unique = false)
	public Boolean isRequired()
	{
		return required;
	}

	public Boolean getRequired()
	{
		return required;
	}

	public void setRequired(Boolean required)
	{
		this.required = required;
	}
	
	public boolean equals(AwardConfig ac)
	{ 
		return new EqualsBuilder().append(getName(), ac.getName()).isEquals();
	}

	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(getName()).toHashCode();
	}

	@Column(name = "link", nullable = true, unique = false)
	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

//	public int compareTo(AwardConfig ac2)
//	{
//		return ac2.sortOrder-this.sortOrder;
//	}
}