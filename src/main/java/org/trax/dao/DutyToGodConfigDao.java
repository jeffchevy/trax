package org.trax.dao;

import org.trax.model.DutyToGodConfig;

public interface DutyToGodConfigDao extends GenericDao<DutyToGodConfig, Long>
{
	public DutyToGodConfig getByName(String name);
}
