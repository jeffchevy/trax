package org.trax.model.cub.pu2015;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.trax.model.AwardConfig;


@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("J")
public class ChildAwardConfig extends AwardConfig // implements Comparable<AwardConfig> 
{
	@Transient
	public String getTypeName()
	{
		return "Adventure";
	}
	@Transient
	public String getImageSource()
	{
		String imageName = getName().replace("Elective Adventures: ","").replace("Elective Adventure: ","").replace("Adventure: ","").replace("Tiger: ","").trim();
		return "images/cub/2015/"+imageName+".png";
	}
	public boolean equals(ChildAwardConfig ac)
	{ 
		return new EqualsBuilder().append(getName(), ac.getName()).isEquals();
	}
	public int compareTo(ChildAwardConfig ac)
	{
		return ac.sortOrder-this.sortOrder;
	}
}