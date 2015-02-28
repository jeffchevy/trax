package org.trax.model.cub;

import java.util.Date;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.Requirement;
import org.trax.model.User;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("C")
public class CubRank extends Award
{
	public CubRank()
	{
	}
	
	public CubRank(AwardConfig awardConfig, Date dateCompleted, String reminders, String event, Set<Requirement> requirements, User signOffLeader)
	{
		super(awardConfig, dateCompleted, reminders, event, requirements, signOffLeader);
	}
}
