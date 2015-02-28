package org.trax.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.ForeignKey;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("A")
@DiscriminatorColumn(name = "Kind", discriminatorType = DiscriminatorType.STRING, length = 1)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Table(name = "Award")
public class Award implements Serializable
{
	// user can be a leader or a guardian
	private User user;
	private Date dateCompleted;
	private Date dateEntered;
	private Date dateAwarded;
	private Date datePurchased;
	private AwardConfig awardConfig;
	private Set<Requirement> requirements;
	private long id;
	private String reminders;
	private String event;
	private boolean inProgress;
	private String percentComplete;

	public Award()
	{
		
	}
	public Award(AwardConfig awardConfig, Date dateCompleted, String reminders, String event, Set<Requirement> requirements, User signOffLeader)
	{
		setAwardConfig(awardConfig);
		setDateCompleted(dateCompleted);
		setReminders(reminders);
		setEvent(event);
		setRequirements(requirements);
		setUser(signOffLeader);
	}
	
	protected void setId(long id)
	{
	    this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public long getId()
	{
		return id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	@ForeignKey(name = "FK_AWARD_USER")
	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	@Column(name = "dateCompleted", nullable = true, unique = false)
	public Date getDateCompleted()
	{
		return dateCompleted;
	}

	public void setDateCompleted(Date dateCompleted)
	{
		dateEntered = new Date();
		this.dateCompleted = dateCompleted;
	}

	@Column(name = "dateEntered", nullable = true, unique = false)
	public Date getDateEntered()
	{
		return dateEntered;
	}

	public void setDateEntered(Date dateEntered)
	{
		this.dateEntered = dateEntered;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "awardConfigId", nullable = false)
	@ForeignKey(name = "FK_AWARD_AWARDCONFIG")
	public AwardConfig getAwardConfig()
	{
		return awardConfig;
	}

	public void setAwardConfig(AwardConfig awardConfig)
	{
		this.awardConfig = awardConfig;
	}
	@Column(name = "dateAwarded", nullable = true, unique = false)
	public Date getDateAwarded()
	{
		return dateAwarded;
	}

	public void setDateAwarded(Date dateAwarded)
	{
		this.dateAwarded = dateAwarded;
	}
	
	@Column(name = "datePurchased", nullable = true, unique = false)
	public Date getDatePurchased()
	{
		return datePurchased;
	}

	public void setDatePurchased(Date datePurchased)
	{
		this.datePurchased = datePurchased;
	}
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER) 
    @JoinColumn(name="awardId", nullable=false)
	@ForeignKey(name = "FK_AWARD_REQUIREMENT")
	public Set<Requirement> getRequirements()
	{
		return requirements;
	}

	public void setRequirements(Set<Requirement> r)
	{
		this.requirements = r;
	}
	
	public void setReminders(String reminders)
	{
		this.reminders = reminders;
	}
	private void setEvent(String event)
	{
		this.event = event;
	}
	@Column (name="event", nullable=true,length=256)
	public String getEvent()
	{
		return event;
	}
	@Column (name="reminder", nullable=true,length=2500 )
	public String getReminders()
	{
		return reminders;
	}
	
	@Column (name="inprogress", nullable=false)
	public boolean getInProgress()
	{
		return inProgress;
	}
	
	public void setInProgress(boolean inProgress)
	{
		this.inProgress = inProgress;
	}
	
	/**
	 * for reports this needs to be calculated, it is not recorded in the db
	 * @return
	 */
	@Transient
	public String getPercentComplete()
	{
		return percentComplete;
	}
	
	public void setPercentComplete(String percentComplete)
	{
		this.percentComplete = percentComplete;
	}
}