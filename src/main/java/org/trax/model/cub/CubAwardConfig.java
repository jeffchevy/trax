package org.trax.model.cub;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.trax.model.AwardConfig;


@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("F")
public class CubAwardConfig extends AwardConfig implements Comparable<CubAwardConfig> 
{
	public CubAwardConfig(){}
	
	public boolean equals(CubAwardConfig ac)
	{ 
		return new EqualsBuilder().append(getName(), ac.getName()).isEquals();
	}
	public int compareTo(CubAwardConfig ac2)
	{
		return ac2.sortOrder-this.sortOrder;
	}
	@Transient
	public String getTypeName()
	{
		return "CubAwards";
	}
	@Transient
	public String getImageSource()
	{
		return "images/cub/awards/"+getName()+".png";
	}
}