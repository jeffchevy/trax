package org.trax.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.trax.model.BadgeConfig;


@Repository("badgeConfigDao")
public class BadgeConfigJpaDao extends GenericJpaDao<BadgeConfig, Long> implements BadgeConfigDao
{
	public BadgeConfig getByName(String name)
	{
		String hql = "from BadgeConfig bc where bc.name = :name";
		List<BadgeConfig> bcs = entityManager.createQuery(hql).setParameter("name", name).getResultList();
		
		return bcs.size() > 0 ? bcs.get(0) : null;
	}
	
	public List<BadgeConfig> getRequired()
	{
		String hql = "from BadgeConfig where required=true";
		List<BadgeConfig> bcs = entityManager.createQuery(hql).getResultList();
		
		return bcs;
	}
}
