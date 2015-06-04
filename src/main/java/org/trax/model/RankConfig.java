package org.trax.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("R")
public class RankConfig extends AwardConfig
{
	@Transient
	public String getTypeName()
	{
		return "Rank";
	}
	@Transient
	public String getImageSource()
	{
		return "images/Ranks/"+getName()+".png";
	}
}