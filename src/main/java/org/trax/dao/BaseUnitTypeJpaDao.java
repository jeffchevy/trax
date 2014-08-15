package org.trax.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.trax.model.BaseUnitType;


@Repository("baseUnitTypeDao")
public class BaseUnitTypeJpaDao extends GenericJpaDao<BaseUnitType, Long> implements BaseUnitTypeDao
{

	public BaseUnitType find(String unitTypeName)
	{
			String hql = "from BaseUnitType where name = :name";
			List<BaseUnitType> ut = (List<BaseUnitType>)entityManager.createQuery(hql).setParameter("name", unitTypeName).getResultList();
			
			return ut.size() > 0 ? ut.get(0) : null;
	}

}
