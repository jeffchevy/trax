package org.trax.service;

import org.springframework.transaction.annotation.Transactional;
import org.trax.model.CampLogEntry;
import org.trax.model.LeadershipLogEntry;
import org.trax.model.ServiceLogEntry;

//Let Spring manage our transactions
@Transactional
public interface LogService
{
	//getters
	CampLogEntry getCampEntry(long logEntryId);
	LeadershipLogEntry getLeadershipEntry(long logEntryId);
	ServiceLogEntry getServiceEntry(long logEntryId);
	
	//updates
	CampLogEntry updateCampEntry(CampLogEntry logEntry);
	LeadershipLogEntry updateLeadershipEntry(LeadershipLogEntry logEntry);
	ServiceLogEntry updateServiceEntry(ServiceLogEntry logEntry);
	
	//removes
	void removeCampEntry(long logEntryId);
	void removeLeadershipEntry(long logEntryId);
	void removeServiceEntry(long logEntryId);
}
