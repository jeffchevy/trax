package org.trax.dao.cub;

import org.springframework.stereotype.Repository;
import org.trax.dao.GenericJpaDao;
import org.trax.model.cub.BeltLoopConfig;


@Repository("beltLoopConfigDao")
public class BeltLoopConfigJpaDao extends GenericJpaDao<BeltLoopConfig, Long> implements BeltLoopConfigDao
{
}
