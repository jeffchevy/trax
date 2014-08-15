package org.trax.dao;

import org.springframework.stereotype.Repository;
import org.trax.model.RankConfig;


@Repository("rankConfigDao")
public class RankConfigJpaDao extends GenericJpaDao<RankConfig, Long> implements RankConfigDao
{
}
