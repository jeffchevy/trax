package org.trax.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.trax.model.DutyToGodConfig;


@Repository("dtgConfigDao")
public class DutyToGodConfigJpaDao extends GenericJpaDao<DutyToGodConfig, Long> implements DutyToGodConfigDao
{
	public DutyToGodConfig getByName(String name)
	{
		String hql = "from DutyToGodConfig dtg where name = :name";
		List<DutyToGodConfig> dtgs = entityManager.createQuery(hql).setParameter("name", name).getResultList();
		
		return dtgs.size() > 0 ? dtgs.get(0) : null;
	}
}
