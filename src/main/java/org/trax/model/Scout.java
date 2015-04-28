package org.trax.model;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Past;

import org.hibernate.annotations.ForeignKey;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("S")
public class Scout extends User implements Comparable<Scout>
{
	public static final String ALL = "All";
	@Past @DateTimeFormat(style = "S-") //Short date only, no time
	private Date birthDate;
	private Set<ServiceLogEntry> serviceEntries;
	private Set<CampLogEntry> campEntries;
	private Set<LeadershipLogEntry> leadershipEntries;
	private boolean selected;
	private boolean checked;
	private byte[] profileImage;
	public enum CubPosition
	{
		DENNER("Denner"), 
		ASSISTANT_DENNER("Assistant Denner");
		
		String positionName;

		CubPosition(String positionName)
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
	public enum ScoutPosition
	{
		ASSISTANT_SENIOR_PATROL_LEADER("Assistant Senior Patrol Leader"), 
		ASSISTANT_PATROL_LEADER("Assistant Patrol Leader"), 
		BUGLER("Bugler"), 
		CHAPLAIN_AIDE("Chaplain Aide"), 
		DEN_CHIEF("Den Chief"), 
		HISTORIAN("Historian"), 
		JUNIOR_ASSISTANT_SCOUTMASTER("Junior Assistant Scoutmaster"), 
		LEAVE_NO_TRACE_TRAINER("Leave No Trace Trainer"),
		LIBRARIAN("Librarian"), 
		INSTRUCTOR("Instructor"), 
		OA_REPRESENTATIVE("OA Representative"), 
		PATROL_LEADER("Patrol Leader"), 
		QUARTERMASTER("Quartermaster"), 
		SCRIBE("Scribe"), 
		SENIOR_PATROL_LEADER("Senior Patrol Leader"), 
		TROOP_GUIDE("Troop Guide"), 
		VENTURE_PATROL_LEADER("Venture Patrol Leader"),
		WEBMASTER("Webmaster"),
		// Varsity
		VARSITY_CAPTAIN("Varsity Captain"), 
		VARSITY_COCAPTAIN("Varsity Cocaptain"), 
		VARSITY_PROGRAM_MANAGER("Varsity Program Manager"), 
		VARSITY_SQUAD_LEADER("Varsity Squad Leader"), 
		VARSITY_TEAM_SECRETARY("Varsity Team Secretary"),
		//VENTURE
		VENTURE_PRESIDENT("Venture President"),
		VENTURE_VICE_PRESIDENT("Venture Vice President"),
		VENTURE_SECRETARY("Venture Secretary"),
		VENTURE_TREASURER("Venture Treasurer"),
		SEA_SCOUT_BOATSWAIN("Sea Scout Boatswain"),
		SEA_SCOUT_BOATSWAIN_MATE("Sea Scout Boatswain's Mate"),
		SEA_SCOUT_YEOMAN("Sea Scout Yeoman"),
		SEA_SCOUT_PURSER("Sea Scout Purser"),
		SEA_SCOUT_STOREKEEPER("Sea Scout Storekeeper"),
		SEA_SCOUT_CREW_LEADER(" Sea Scout Crew Leader"),
		SEA_SCOUT_ASSITANT_CREW_LEADER("Sea Scout Assistant Crew Leader"),
		SEA_SCOUT_SPECIALIST("Sea Scout Specialist"),
		SEA_SCOUT_BUGLER("Sea Scout Bugler"),
		SEA_SCOUT_ENGINEER("Sea Scout Engineer");
		
		String positionName;

		ScoutPosition(String positionName)
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
	ScoutPosition position;
	@Enumerated(EnumType.STRING)
	CubPosition cubPosition;

	public Scout()
	{
		setCreationDate(new Date());
	}

	public Scout(Organization organization, Unit unit, String firstName, String middleName, String lastName,
			String nameSuffix, String zip)
	{
		setOrganization(organization);
		setCity(organization.getCity());
		setState(organization.getState());
		setUnit(unit);
		setFirstName(firstName);
		setLastName(lastName);
		setMiddleName(middleName);
		setNameSuffix(nameSuffix);
		setZip(zip);
		setCreationDate(new Date());
	}

	@Column(name = "birthDate", unique = false, nullable = true)
	public Date getBirthDate()
	{
		return birthDate;
	}

	/**
	 * return the birthday.  This is only used for the reminders .vm files and should be moved there
	 * @return
	 */
	@Transient
	public String getBirthday()
	{
		Format formatter = new SimpleDateFormat("EEEE, MMMM dd");
		return formatter.format(birthDate);
	}
	
	/**
	 */
	@Transient
	public int getAge()
	{
		// Create a calendar object with the date of birth
		Calendar dateOfBirth = Calendar.getInstance();
		dateOfBirth.setTime(getBirthDate());

		// Create a calendar object with today's date
		Calendar today = Calendar.getInstance();

		// Get age based on year
		int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
		
		//now subtract, if it hasn't happened this year yet
		if (today.get(Calendar.MONTH) < dateOfBirth.get(Calendar.MONTH)) 
		{
		    age--;  
		} 
		else if (today.get(Calendar.MONTH) == dateOfBirth.get(Calendar.MONTH)
		      && today.get(Calendar.DAY_OF_MONTH) < dateOfBirth.get(Calendar.DAY_OF_MONTH)) 
		{
		    age--;  
		}
			
		return age;
	}
	
    /**
     * return the age as if his birthday has already occurred. This is only used for the reminders .vm files and should be moved there
     * @return
     */
    @Transient
    public int getPreBirthdayAge()
    {
        // Create a calendar object with the date of birth
        Calendar dateOfBirth = Calendar.getInstance();
        dateOfBirth.setTime(getBirthDate());

        // Create a calendar object with today's date
        Calendar today = Calendar.getInstance();

        // Get age based on year
        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

        
        boolean bornInJanuary = dateOfBirth.get(Calendar.MONTH)==0;
        boolean todayInDecember = today.get(Calendar.MONTH)==11;
        if (bornInJanuary && todayInDecember)
        {
            //this could be prebirthday reminder for December, January birthday, 
            age++;
        }

        return age;
    }

	public ScoutPosition getPosition()
	{
		return position;
	}

	public void setPosition(ScoutPosition position)
	{
		this.position = position;
	}

	public CubPosition getCubPosition()
	{
		return cubPosition;
	}

	public void setCubPosition(CubPosition cubPosition)
	{
		this.cubPosition = cubPosition;
	}

	public void setBirthDate(Date birthDate)
	{
		this.birthDate = birthDate;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	/*
	 * don't store this, this is only to see if we view data for them or not
	 */
	@Transient
	public boolean isSelected()
	{
		return selected;
	}

	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

	/*
	 * don't store this, this is only to see if we view data for them or not
	 */
	@Transient
	public boolean isChecked()
	{
		return checked;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "scoutId", nullable = false, unique = false)
	@ForeignKey(name = "FK_SCOUT_SERVICELOG")
	public Set<ServiceLogEntry> getServiceEntries()
	{
		return serviceEntries;
	}

	public void setServiceEntries(Set<ServiceLogEntry> serviceEntries)
	{
		this.serviceEntries = serviceEntries;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "scoutId", nullable = false, unique = false)
	@ForeignKey(name = "FK_SCOUT_CAMPLOG")
	public Set<CampLogEntry> getCampEntries()
	{
		return campEntries;
	}

	public void setCampEntries(Set<CampLogEntry> campEntries)
	{
		this.campEntries = campEntries;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "scoutId", nullable = false, unique = false)
	@ForeignKey(name = "FK_SCOUT_LEADERSHIPLOG")
	public Set<LeadershipLogEntry> getLeadershipEntries()
	{
		return leadershipEntries;
	}

	public void setLeadershipEntries(Set<LeadershipLogEntry> leadershipEntries)
	{
		this.leadershipEntries = leadershipEntries;
	}

	public void setProfileImage(byte[] profileImage)
	{
		this.profileImage = profileImage;
	}

	@Basic(fetch=FetchType.EAGER)
	@Lob @Column(name="profileImage")
	public byte[] getProfileImage()
	{
		return profileImage;
	}

	public int compareTo(Scout scout)
	{
		int returnVal = 0;
		if (this.hashCode()!=scout.hashCode())
		{
			if(this.birthDate!=null && scout.birthDate!=null)
			{
				returnVal = this.birthDate.compareTo(scout.birthDate);
			}
			else
			{
				returnVal = this.getFullName().compareTo(scout.getFullName());
			}
		}
		return  returnVal;
	}
}
