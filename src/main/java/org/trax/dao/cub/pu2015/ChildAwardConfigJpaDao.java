package org.trax.dao.cub.pu2015;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.trax.dao.GenericJpaDao;
import org.trax.model.cub.PinConfig;
import org.trax.model.cub.pu2015.ChildAwardConfig;


@Repository("childAwardConfigDao")
public class ChildAwardConfigJpaDao extends GenericJpaDao<ChildAwardConfig, Long> implements ChildAwardConfigDao
{
	@Override
	public List<ChildAwardConfig> getchildAwardConfigs(long parentAwardConfigId)
	{
			String hql = "from ChildAwardConfig where parentAwardConfigId = :parentAwardConfigId";
			List<ChildAwardConfig> childAwardConfigs = entityManager.createQuery(hql).setParameter("parentAwardConfigId", parentAwardConfigId).getResultList();
			
			return childAwardConfigs;
	}
}
