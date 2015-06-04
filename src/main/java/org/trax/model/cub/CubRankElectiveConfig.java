package org.trax.model.cub;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.trax.model.AwardConfig;


@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("E")
public class CubRankElectiveConfig extends AwardConfig//implements Comparable<AwardConfig> 
{
	public CubRankElectiveConfig(){}
	
	public boolean equals(CubRankElectiveConfig ac)
	{ 
		return new EqualsBuilder().append(getName(), ac.getName()).isEquals();
	}
	public int compareTo(CubRankElectiveConfig ac2)
	{
		return ac2.sortOrder-this.sortOrder;
	}
	@Transient
	public String getTypeName()
	{
		return getName().substring(0, getName().indexOf(" Electives"));
	}
	@Transient
	public String getImageSource()
	{
		return "images/cub/electives/"+getName()+".png";
	}
}