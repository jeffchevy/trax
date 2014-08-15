package org.trax.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("R")
public class Rank extends Award
{
	public Rank(){}
	
	//need this for palms
	public Rank(AwardConfig awardConfig, Date dateCompleted, String reminders, Set<Requirement> requirements, User signOffLeader)
	{
		super(awardConfig, dateCompleted, reminders, requirements, signOffLeader);
	}
}