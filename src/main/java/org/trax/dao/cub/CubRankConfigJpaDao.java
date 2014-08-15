package org.trax.dao.cub;

import org.springframework.stereotype.Repository;
import org.trax.dao.GenericJpaDao;
import org.trax.model.cub.CubRankConfig;


@Repository("cubRankConfigDao")
public class CubRankConfigJpaDao extends GenericJpaDao<CubRankConfig, Long> implements CubRankConfigDao
{
}
