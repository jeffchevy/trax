package org.trax.dao;

import java.util.List;

import org.trax.model.BadgeConfig;

public interface BadgeConfigDao extends GenericDao<BadgeConfig, Long>
{
	public BadgeConfig getByName(String name);

	public List<BadgeConfig> getRequired();
}
