package org.trax.dao;

import org.springframework.stereotype.Repository;
import org.trax.model.ServiceLogEntry;

@Repository("serviceLogDao")
public class ServiceLogJpaDao extends GenericJpaDao<ServiceLogEntry, Long> implements ServiceLogDao
{
}
