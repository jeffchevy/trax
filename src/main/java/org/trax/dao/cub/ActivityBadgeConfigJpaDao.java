package org.trax.dao.cub;

import org.springframework.stereotype.Repository;
import org.trax.dao.GenericJpaDao;
import org.trax.model.cub.ActivityBadgeConfig;


@Repository("activityBadgeConfigDao")
public class ActivityBadgeConfigJpaDao extends GenericJpaDao<ActivityBadgeConfig, Long> implements ActivityBadgeConfigDao
{
}
