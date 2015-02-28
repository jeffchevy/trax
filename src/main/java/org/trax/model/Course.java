package org.trax.model;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("T")
public class Course extends Award
{
	public Course()
	{
		super();
	}
	
	public Course(AwardConfig awardConfig, Date dateCompleted, String reminders, String event, User signOffLeader)
	{
		super(awardConfig, dateCompleted, reminders, event, null, signOffLeader);
	}
}