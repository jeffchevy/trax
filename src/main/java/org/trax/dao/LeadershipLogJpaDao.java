package org.trax.dao;

import org.springframework.stereotype.Repository;
import org.trax.model.LeadershipLogEntry;

@Repository("leadershipLogDao")
public class LeadershipLogJpaDao extends GenericJpaDao<LeadershipLogEntry, Long> implements LeadershipLogDao
{
}
