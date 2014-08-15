package org.trax.model.cub;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.trax.model.AwardConfig;


@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("P")
public class PinConfig extends AwardConfig //implements Comparable<AwardConfig> 
{
	public PinConfig(){}
	
	public boolean equals(PinConfig ac)
	{ 
		return new EqualsBuilder().append(getName(), ac.getName()).isEquals();
	}
	public int compareTo(PinConfig ac2)
	{
		return ac2.sortOrder-this.sortOrder;
	}
}