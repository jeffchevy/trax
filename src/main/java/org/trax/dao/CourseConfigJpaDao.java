package org.trax.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.trax.model.CourseConfig;


@Repository("courseConfigDao")
public class CourseConfigJpaDao extends GenericJpaDao<CourseConfig, Long> implements CourseConfigDao
{
	public CourseConfig getByName(String name)
	{
		String hql = "from CourseConfig ac where ac.name = :name";
		List<CourseConfig> acs = entityManager.createQuery(hql).setParameter("name", name).getResultList();
		
		return acs.size() > 0 ? acs.get(0) : null;
	}
}
