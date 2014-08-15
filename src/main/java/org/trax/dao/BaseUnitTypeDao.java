package org.trax.dao;

import org.trax.model.BaseUnitType;

public interface BaseUnitTypeDao extends GenericDao<BaseUnitType, Long>
{

	BaseUnitType find(String unitTypeName);
}
