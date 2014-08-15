package org.trax.dto;

import java.util.Set;

public class ScoutsAwardList {
	//protected String scoutName;
	protected String awardConfigId;
	protected Long userId;
	//protected Date birthDate; 
	protected Set<Long> re;
	
	
	public String getAwardConfigId() {
		return awardConfigId;
	}
	public void setAwardConfigId(String awardConfigId) {
		this.awardConfigId = awardConfigId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
