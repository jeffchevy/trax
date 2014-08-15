package org.trax.dao.cub;

import org.springframework.stereotype.Repository;
import org.trax.dao.GenericJpaDao;
import org.trax.model.cub.CubUnitType;


@Repository("cubUnitTypeDao")
public class CubUnitTypeJpaDao extends GenericJpaDao<CubUnitType, Long> implements CubUnitTypeDao
{

}
