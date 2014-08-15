package org.trax.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@SuppressWarnings("serial")
@Entity
@Table(name = "CampLog")
public class CampLogEntry
{
	@NotNull(message = "Must specify the location")
	@Size(min=1, max=150, message = "Must specify the location")
	private String location;
	@NotNull(message = "Must specify a depart date")
	@DateTimeFormat(style = "S-") //Short date only, no time
	private Date departDate;
	@NotNull(message = "Must specify a return date")
	@DateTimeFormat(style = "S-") //Short date only, no time
	private Date returnDate;
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

	@Column (name="location", nullable=false )
	public String getLocation()
	{
		return location;
	}
	public void setLocation(String location)
	{
		this.location = location;
	}
	@Column(name = "departDate", nullable = true, unique = false)
	public Date getDepartDate()
	{
		return departDate;
	}
	public void setDepartDate(Date departDate)
	{
		this.departDate = departDate;
	}
	@Column(name = "returnDate", nullable = true, unique = false)
	public Date getReturnDate()
	{
		return returnDate;
	}
	public void setReturnDate(Date returnDate)
	{
		dateEntered = new Date();
		this.returnDate = returnDate;
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
	@ForeignKey(name = "FK_CAMPLOG_USER")
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
		return "<a class='delete' title='Remove this entry' href='removeCampEntry.html?logEntryId="+id+"'>" +
				"<img src='images/delete.jpg' alt='Remove this log entry.' title='Remove this log entry.'></a>";
	}
	@Transient
	public String getEditLink()
	{
		return "<a title='Edit this entry' href='loadCampEntry.html?logEntryId="+id+"'>" +
				"<img src='images/edit.jpg' alt='Edit this log entry.' title='Edit this log entry.'></a>";
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * Log Entry is the same if the date and scout are the same- there can only be one
	 */
	@Override
	public boolean equals(Object other) 
	{ 
		CampLogEntry clr = (CampLogEntry)other;
		return new EqualsBuilder().append(getId(), clr.getId()).isEquals();
	}
	
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(getId()).toHashCode();
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
}
