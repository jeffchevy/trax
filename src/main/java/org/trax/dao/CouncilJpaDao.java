package org.trax.dao;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.trax.model.Council;

@Repository("councilDao")
public class CouncilJpaDao extends GenericJpaDao<Council, Long> implements CouncilDao
{
	public Council getByName(String name)
	{
		String hql = "from Council c where c.name = :name";
		List<Council> acs = entityManager.createQuery(hql).setParameter("name", name).getResultList();
		
		return acs.size() > 0 ? acs.get(0) : null;
	}
	
	public List<Council> getByState(String state)
	{
		String hql = "from Council c where state = :state";
		List<Council> acs = entityManager.createQuery(hql).setParameter("state", state).getResultList();
		
		return acs;
	}
}
