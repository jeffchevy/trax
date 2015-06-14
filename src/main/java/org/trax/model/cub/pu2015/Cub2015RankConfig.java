package org.trax.model.cub.pu2015;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.trax.model.RankConfig;


@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("I")
public class Cub2015RankConfig extends RankConfig//implements Comparable<AwardConfig> 
{
	public Cub2015RankConfig(){}
	
	@Transient
	public String getTypeName()
	{
		return RANK;
	}
	@Transient
	public String getImageSource()
	{
		return "images/cub/2015/"+getName().trim()+".png";
	}
	public boolean equals(Cub2015RankConfig rc)
	{ 
		return new EqualsBuilder().append(getName(), rc.getName()).isEquals();
	}
	public int compareTo(Cub2015RankConfig rc2)
	{
		return rc2.sortOrder-this.sortOrder;
	}
}