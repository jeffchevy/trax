package org.trax.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("D")
public class DutyToGodConfig extends AwardConfig
{
	@Transient
	public String getTypeName()
	{
		return "DutyToGod";
	}
	@Transient
	public String getImageSource()
	{
		return "images/awards/dtg/"+getName()+".png";
	}
}
