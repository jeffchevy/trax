package org.trax.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.trax.dao.CampLogDao;
import org.trax.dao.LeadershipLogDao;
import org.trax.dao.ServiceLogDao;
import org.trax.model.CampLogEntry;
import org.trax.model.LeadershipLogEntry;
import org.trax.model.ServiceLogEntry;

@Service("logService")
public class LogServiceImpl implements LogService
{
	@Autowired
	@Qualifier("campLogDao")
	private CampLogDao campLogDao;
	@Autowired
	@Qualifier("leadershipLogDao")
	private LeadershipLogDao leadershipLogDao;
	@Autowired
	@Qualifier("serviceLogDao")
	private ServiceLogDao serviceLogDao;

	public CampLogEntry getCampEntry(long logEntryId)
	{
		return campLogDao.findById(logEntryId, false);
	}
	public LeadershipLogEntry getLeadershipEntry(long logEntryId)
	{
		return leadershipLogDao.findById(logEntryId, false);
	}
	public ServiceLogEntry getServiceEntry(long logEntryId)
	{
		return serviceLogDao.findById(logEntryId, false);
	}

	public CampLogEntry updateCampEntry(CampLogEntry logEntry)
	{
		campLogDao.merge(logEntry);
		return logEntry;
	}
	public LeadershipLogEntry updateLeadershipEntry(LeadershipLogEntry logEntry)
	{
		leadershipLogDao.merge(logEntry);
		return logEntry;
	}
	public ServiceLogEntry updateServiceEntry(ServiceLogEntry logEntry)
	{
		serviceLogDao.merge(logEntry);
		return logEntry;
	}

	public void removeCampEntry(long logEntryId)
	{
		CampLogEntry entry = campLogDao.findById(logEntryId, true);
		campLogDao.remove(entry);
	}
	public void removeLeadershipEntry(long logEntryId)
	{
		LeadershipLogEntry entry = leadershipLogDao.findById(logEntryId, true);
		leadershipLogDao.remove(entry);
	}
	public void removeServiceEntry(long logEntryId)
	{
		ServiceLogEntry entry = serviceLogDao.findById(logEntryId, true);
		serviceLogDao.remove(entry);
	}
}
	