package org.trax.dto;

import java.util.Date;
import java.util.Set;

import org.trax.model.Award;
import org.trax.model.CampLogEntry;
import org.trax.model.LeadershipLogEntry;
import org.trax.model.ServiceLogEntry;

public class ScoutReportDto
{
	private Set<Award> awards;
	private Set<CampLogEntry> campEntries;
	private Set<LeadershipLogEntry> leadershipEntries;
	private Set<ServiceLogEntry> serviceEntries;
	private String fullname;
	private Integer bsaMemberId;
	private String positionName;
	private boolean isCub=false;
	private Date birthday;

	public Set<Award> getAwards()
	{
		return awards;
	}

	public Set<CampLogEntry> getCampEntries()
	{
		return campEntries;
	}

	public Set<LeadershipLogEntry> getLeadershipEntries()
	{
		return leadershipEntries;
	}

	public Set<ServiceLogEntry> getServiceEntries()
	{
		return serviceEntries;
	}

	public void setAwards(Set<Award> awards)
	{
		this.awards = awards;
	}

	public void setCampEntries(Set<CampLogEntry> campEntries)
	{
		this.campEntries = campEntries;
	}

	public void setLeadershipEntries(Set<LeadershipLogEntry> leadershipEntries)
	{
		this.leadershipEntries = leadershipEntries;
	}

	public void setServiceEntries(Set<ServiceLogEntry> serviceEntries)
	{
		this.serviceEntries = serviceEntries;
	}

	public Object getBsaMemberId()
	{
		return bsaMemberId;
	}

	public String getFullname()
	{
		return fullname;
	}

	public void setFullname(String fullname)
	{
		this.fullname = fullname;
	}

	public String getPositionName()
	{
		return positionName;
	}

	public void setPositionName(String positionName)
	{
		this.positionName = positionName;
	}

	public void setBsaMemberId(Integer bsaMemberId)
	{
		this.bsaMemberId = bsaMemberId;
	}

	public void setCub(boolean isCub)
	{
		this.isCub = isCub;
	}

	public boolean isCub()
	{
		return isCub;
	}

	public Date getBirthday()
	{
		return birthday;
	}

	public void setBirthday(Date birthday)
	{
		this.birthday = birthday;
	}

}
