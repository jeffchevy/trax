package org.trax.dao;

import org.springframework.stereotype.Repository;
import org.trax.model.ScoutUnitType;


@Repository("scoutUnitTypeDao")
public class ScoutUnitTypeJpaDao extends GenericJpaDao<ScoutUnitType, Long> implements ScoutUnitTypeDao
{

}
