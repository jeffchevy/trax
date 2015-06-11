package org.trax.dao.cub.pu2015;

import org.springframework.stereotype.Repository;
import org.trax.dao.GenericJpaDao;
import org.trax.model.cub.pu2015.ChildAward;


@Repository("childAwardDao")
public class ChildAwardJpaDao extends GenericJpaDao<ChildAward, Long> implements ChildAwardDao
{
}
