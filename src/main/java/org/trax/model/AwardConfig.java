package org.trax.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.ForeignKey;
import org.trax.model.cub.pu2015.ChildAwardConfig;


@SuppressWarnings("serial")
@Entity
@DiscriminatorColumn(name="Kind", discriminatorType=DiscriminatorType.STRING, length=1)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Table(name="AwardConfig", uniqueConstraints = {@UniqueConstraint(columnNames={"name", "Kind"})})
@DiscriminatorValue("A")
public class AwardConfig implements Serializable//implements Comparable<AwardConfig> 
{
    public static final String AWARDS = "Awards";
    public static final String BELT_LOOPS = "Belt_Loops";
    public static final String PIN = "Pins";
    public static final String RANK = "Rank";
    private Set<Sponsor> sponsors;
    private String name;
	private String description;
	private boolean isSelectable=true;
	private List<RequirementConfig> requirementConfigs;
	protected int sortOrder;
	private long id;
	private Boolean required;
	protected String link;
	private Set<ChildAwardConfig> childAwardConfigs;
	/*there are Scout, Cub, Cub2015, Varsity Team and Venturing Crew awards
	 some awards go across multiple families, so need to do a many to many mapping
	 AwardConfig would have a list of familiesOfScouting and the families can be mapped to many different awards or age groups
	 private String awardGroup;
	 */
	
	public AwardConfig(){}
	
	public AwardConfig(String name, String description, int sortOrder, Boolean required)
	{
		this.setName(name);
		this.setDescription(description);
		this.setSortOrder(sortOrder);
		this.setRequired(required);
	}

	public void setId(long id)
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "parentAwardConfigId", nullable = true, unique = false)
	@ForeignKey(name="parent_award_config_award")
	@OrderBy(value = "sortOrder")
	public Set<ChildAwardConfig> getChildAwardConfigs()
	{
		return childAwardConfigs;
	}

	public void setChildAwardConfigs(Set<ChildAwardConfig> childAwardConfigs)
	{
		this.childAwardConfigs = childAwardConfigs;
	}
	@Transient
	public String getTypeName()
	{
		return AWARDS;
	}
	@Transient
	public String getImageSource()
	{
		return "images/awards/"+getName()+".png";
	}
//	public int compareTo(AwardConfig ac2)
//	{
//		return ac2.sortOrder-this.sortOrder;
//	}

	// some awards like 2015 cub hold other awards and cannot be earned.
	@Column (name="isSelectable", nullable=false, unique=false)
	public boolean isSelectable()
	{
		return isSelectable;
	}

	public void setSelectable(boolean isSelectable)
	{
		this.isSelectable = isSelectable;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "awardConfigId", nullable = true, unique = false)
    @ForeignKey(name="award_config_sponsor")
	public Set<Sponsor> getSponsors() {
        return sponsors;
    }

    public void setSponsors(Set<Sponsor> sponsors) {
        this.sponsors = sponsors;
    }
	
/*	@Column(name = "awardGroup", nullable = true)
	public String getAwardGroup()
	{
		return awardGroup;
	}

	public void setAwardGroup(String awardGroup)
	{
		this.awardGroup = awardGroup;
	}
	*/
}