package org.trax.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.ForeignKey;
import org.springframework.format.annotation.DateTimeFormat;
import org.trax.model.Scout.ScoutPosition;

@SuppressWarnings("serial")
@Entity
@Table(name = "LeadershipLog")
public class LeadershipLogEntry
{
	private ScoutPosition position;
	@DateTimeFormat(style = "S-") //Short date only, no time
	@NotNull(message = "Must specify a start date")
	private Date startDate;
	@DateTimeFormat(style = "S-") //Short date only, no time
	private Date endDate;
	private Date dateEntered;
	private User signOffLeader;
	@NotNull(message = "Must enter a description")
	private String description;
	private long id;

	public void setId(long id)
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

	@Column (name="position", nullable=false )
	public ScoutPosition getPosition()
	{
		return position;
	}
	public void setPosition(ScoutPosition position)
	{
		this.position = position;
	}
	
	@Column(name = "startDate", nullable = true, unique = false)
	public Date getStartDate()
	{
		return startDate;
	}
	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}
	
	@Column(name = "endDate", nullable = true, unique = false)
	public Date getEndDate()
	{
		return endDate;
	}
	public void setEndDate(Date endDate)
	{
		dateEntered = new Date();
		this.endDate = endDate;
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
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	@ForeignKey(name = "FK_LEADERSHIPLOG_USER")
	public User getSignOffLeader()
	{
		return signOffLeader;
	}
	public void setSignOffLeader(User signOffLeader)
	{
		this.signOffLeader = signOffLeader;
	}
	
	@Column (name="text", nullable=false,length=1000 )
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	@Transient
	public String getRemoveLink()
	{
		return "<a class='delete' title='Remove this entry' href='removeLeadershipEntry.html?logEntryId="+id+"'>" +
				"<img src='images/delete.jpg' alt='Remove this log entry.' title='Remove this log entry.'></a>";
	}
	@Transient
	public String getEditLink()
	{
		return "<a title='Edit this entry' href='loadLeadershipEntry.html?logEntryId="+id+"'>" +
				"<img src='images/edit.jpg' alt='Edit this log entry.' title='Edit this log entry.'></a>";
	}
	
//	@ManyToOne
//	@JoinColumn(name = "scoutId")
//	public Scout getScout()
//	{
//		return scout;
//	}
//	
//	public void setScout(Scout scout)
//	{
//		this.scout = scout;
//	}
	
	@Override
	public boolean equals(Object other) 
	{ 
		LeadershipLogEntry llr = (LeadershipLogEntry)other;
		return new EqualsBuilder().append(getId(), llr.getId()).isEquals();
	}
	
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(getId()).toHashCode();
	}
}
