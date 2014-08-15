package org.trax.dao.cub;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.trax.dao.GenericJpaDao;
import org.trax.model.AwardConfig;
import org.trax.model.cub.PinConfig;


@Repository("pinConfigDao")
public class PinConfigJpaDao extends GenericJpaDao<PinConfig, Long> implements PinConfigDao
{

	public PinConfig getByName(String name)
	{
		String hql = "from PinConfig where name = :name";
		List<PinConfig> acs = entityManager.createQuery(hql).setParameter("name", name).getResultList();
		
		return acs.size() > 0 ? acs.get(0) : null;
	}
}
