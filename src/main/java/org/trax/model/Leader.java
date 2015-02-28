package org.trax.model;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("L")
public class Leader extends User
{
	public Leader()
	{
		setCreationDate(new Date());
	}
	
	public enum CubLeaderPosition
	{
		CUBMASTER("Cubmaster"), 
		ASSISTANT_CUBMASTER("Assistant Cubmaster"),
		DEN_LEADER("Den Leader"), 
		ASSISTANT_DEN_LEADER("Assistant Den Leader"),
		PACK_COMMITTEE("Pack Committee"),
		FUNCTION_COMMITTEE("Function Committee"), 
		PARENT_HELPER("Parent Helper"),
		CHARTERED_ORGANIZATIONAL_REP ("Chartered Organization Representative"),
		EXECUTIVE ("Executive"),
		COMMITTEE_CHAIRMAN ("Committee Chairman"),
		PACK_TRAINER ("Pack Trainer"),
		ADVANCEMENT_CHAIR ("Advancement Chair");
		
		String positionName;

		CubLeaderPosition(String positionName)
		{
			this.positionName = positionName;
		}

		public String getPositionName()
		{
			return positionName;
		}

		public String toString()
		{
			return positionName;
		}
	}
	
	@Enumerated(EnumType.STRING)
    CubLeaderPosition cubPosition;
	
	
	public enum LeaderPosition {
		ELEVEN_YEAR_OLD_SCOUT_MASTER ("Eleven Year old Scout Master"),
		ELEVEN_YEAR_OLD_ASSISTANT_SCOUT_MASTER ("Eleven Year old Assistant Scout Master"),

		SCOUT_MASTER ("Scout Master"),
		ASSISTANT_SCOUT_MASTER ("Assistant Scout Master"),
		TROOP_COMMITTEE_CHAIR ("Troop Committee Chair"), 
		TROOP_COMMITTEE_MEMBER ("Troop Committee Member"), 
		
		VARSITY_COACH ("Varsity Coach"), 
		VARSITY_ASSISTANT_COACH ("Varsity Assistant Coach"), 
		VARSITY_COMMITTEE_CHAIR ("Varsity Committee Chair"), 
		VARSITY_COMMITTEE_MEMBER ("Varsity Committee Member"), 
		
		VENTURE_ADVISOR ("Venturing Advisor"),
		VENTURE__ASSISTANT_ADVISOR ("Venturing Associate Advisor"),
		VENTURE_COMMITTEE_CHAIR ("Venturing Committee Chair"), 
		VENTURE_COMMITTEE_MEMBER ("Venturing Committee Member"),

		CHAPLAIN ("Chaplain"),
		CHARTERED_ORGANIZATIONAL_REP ("Chartered Organization Representative"),
		EQUIPMENT_COORDINATOR ("Equipment Coordinator"),
		FRIENDS_OF_SCOUTING_CHAIR ("Friends of Scouting Chair"),
		FUND_RAISING_CHAIR ("Fundraising Chair"),
		MEMBERSHIP_CHAIR ("Membership Chair"),
		OUTDOOR_ACTIVITIES_CHAIR ("Outdoors/Activities Chair"),
		PUBLIC_RELATIONS_CHAIR ("Public Relations Chair"),
		SECRETARY ("Secretary"),
		TRAINING_CHAIR ("Training Chair"),
		SCOUTING_FOR_FOOD_CHAIR ("Scouting for Food Chair"),
		ADVANCEMENT_CHAIR ("Advancement Chair"),
		EXECUTIVE ("Executive"),
		
		SKIPPER ("Sea Scout Skipper"),
		MATE ("Sea Scout Skipper's Mate");
		

		String positionName;
	    LeaderPosition(String positionName) 
	    {
	    	this.positionName = positionName;
	    }
	    public String getPositionName()
	    {
	    	return positionName;
	    }
	    public String toString()
	    {
	    	return positionName;
	    }
	}
	
	@Enumerated(EnumType.STRING)
    LeaderPosition position;

    public LeaderPosition getPosition()
    {
        return position;
    }

    public CubLeaderPosition getCubPosition()
	{
		return cubPosition;
	}

	public void setCubPosition(CubLeaderPosition cubPosition)
	{
		this.cubPosition = cubPosition;
	}

	public void setPosition(LeaderPosition position)
    {
        this.position = position;
    }
}