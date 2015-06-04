package org.trax.dao.cub.pu2015;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.trax.dao.GenericJpaDao;
import org.trax.model.cub.PinConfig;
import org.trax.model.cub.pu2015.Cub2015RankConfig;
import org.trax.model.cub.pu2015.ChildAwardConfig;


@Repository("cub2015RankConfigDao")
public class Cub2015RankConfigJpaDao extends GenericJpaDao<Cub2015RankConfig, Long> implements Cub2015RankConfigDao
{
}
