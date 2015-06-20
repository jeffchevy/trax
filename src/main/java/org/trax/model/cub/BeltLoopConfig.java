package org.trax.model.cub;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.trax.model.AwardConfig;


@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("L")
public class BeltLoopConfig extends AwardConfig //implements Comparable<AwardConfig> 
{

    public BeltLoopConfig(){}
	
	public boolean equals(BeltLoopConfig ac)
	{ 
		return new EqualsBuilder().append(getName(), ac.getName()).isEquals();
	}
	public int compareTo(BeltLoopConfig ac2)
	{
		return ac2.sortOrder-this.sortOrder;
	}
	@Transient
	public String getTypeName()
	{
		return BELT_LOOPS;
	}

	@Transient
	public String getImageSource()
	{
		return "images/cub/beltloops/"+getName()+".png";
	}
}