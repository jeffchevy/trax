package org.trax.form;

import java.util.Collection;

import org.trax.model.AwardConfig;

public class AwardConfigForm 
{
	private Collection<AwardConfig> awardConfigs;

	public Collection<AwardConfig> getAwardConfigs()
	{
		return awardConfigs;
	}

	public void setAwardConfigs(Collection<AwardConfig> awardConfigs)
	{
		this.awardConfigs = awardConfigs;
	}
}
