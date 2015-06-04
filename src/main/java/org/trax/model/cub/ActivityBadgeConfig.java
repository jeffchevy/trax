package org.trax.model.cub;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.trax.model.AwardConfig;


@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("G")
public class ActivityBadgeConfig extends AwardConfig//implements Comparable<AwardConfig> 
{
	public ActivityBadgeConfig(){}
	
	public boolean equals(ActivityBadgeConfig ac)
	{ 
		return new EqualsBuilder().append(getName(), ac.getName()).isEquals();
	}
	public int compareTo(ActivityBadgeConfig ac2)
	{
		return ac2.sortOrder-this.sortOrder;
	}
	@Transient
	public String getTypeName()
	{
		return "Webelos_Award";
	}
	@Transient
	public String getImageSource()
	{
		return "images/cub/activitybadges/"+getName()+".png";
	}
}