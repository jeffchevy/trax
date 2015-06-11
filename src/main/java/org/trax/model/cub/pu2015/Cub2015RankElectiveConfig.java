package org.trax.model.cub.pu2015;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.trax.model.AwardConfig;


@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("K")
public class Cub2015RankElectiveConfig extends AwardConfig//implements Comparable<AwardConfig> 
{
	public Cub2015RankElectiveConfig(){}
	
	
	public boolean equals(AwardConfig ac)
	{ 
		return new EqualsBuilder().append(getName(), ac.getName()).isEquals();
	}
	public int compareTo(AwardConfig ac)
	{
		return ac.getSortOrder()-this.sortOrder;
	}
	@Transient
	public String getImageSource()
	{
		return "images/cub/2015/"+getName()+".png";
	}
}