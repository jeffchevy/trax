package org.trax.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("D")
public class DutyToGod extends Award
{
	public DutyToGod()
	{
		
	}
	
	public DutyToGod(AwardConfig awardConfig, Date dateCompleted, String reminders, String event, Set<Requirement> requirements, User signOffLeader)
	{
		super(awardConfig, dateCompleted, reminders, event, requirements, signOffLeader);
	}
}
