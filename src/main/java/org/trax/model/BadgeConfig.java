package org.trax.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("B")
public class BadgeConfig extends AwardConfig
{
	@Transient
	public String getTypeName()
	{
		return "Badges";
	}
	@Transient
	public String getImageSource()
	{
		return "images/meritbadges/"+getName()+".png";
	}
}
