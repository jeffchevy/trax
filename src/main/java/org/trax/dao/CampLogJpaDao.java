package org.trax.dao;

import org.springframework.stereotype.Repository;
import org.trax.model.CampLogEntry;

@Repository("campLogDao")
public class CampLogJpaDao extends GenericJpaDao<CampLogEntry, Long> implements CampLogDao
{
}
