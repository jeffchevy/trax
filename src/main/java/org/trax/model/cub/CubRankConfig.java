package org.trax.model.cub;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.trax.model.RankConfig;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("C")
public class CubRankConfig extends RankConfig
{
	@Transient
	public String getTypeName()
	{
		return "Rank"; //getName();//"CubRanks";
	}
	@Transient
	public String getImageSource()
	{
		return "images/cub/ranks/"+getName()+".png";
	}
}