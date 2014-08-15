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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.ForeignKey;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

@SuppressWarnings("serial")
@Entity
@Table(name = "ServiceLog")
public class ServiceLogEntry
{
	@NotNull(message="Must enter a Type of project")
    @Size(min=1, max=256) 
	private String typeOfProject;
	@DateTimeFormat
	@NotNull(message="Must enter a service date")
	private Date serviceDate;
	private Date dateEntered;
	@DecimalMin(value="0.1",  message = "Hours cannot be 0")
	@NumberFormat(style = Style.NUMBER)
	private double timeInHours;
	private User signOffLeader;
	@NotNull(message="Description cannot be null")
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

	@Column (name="typeOfProject", nullable=false )
	public String getTypeOfProject()
	{
		return typeOfProject;
	}
	public void setTypeOfProject(String typeOfProject)
	{
		this.typeOfProject = typeOfProject;
	}

	@Column (name="serviceDate", nullable=false )
	public Date getServiceDate()
	{
		return serviceDate;
	}
	public void setServiceDate(Date serviceDate)
	{
		dateEntered = new Date();
		this.serviceDate = serviceDate;
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
	
	@Column (name="timeInHours", nullable=false)
	public double getTimeInHours()
	{
		return timeInHours;
	}
	public void setTimeInHours(double timeInHours)
	{
		this.timeInHours = timeInHours;
	}
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	@ForeignKey(name = "FK_SERVICELOG_USER")
	public User getSignOffLeader()
	{
		return signOffLeader;
	}
	public void setSignOffLeader(User signOffLeader)
	{
		this.signOffLeader = signOffLeader;
	}

	@Column (name="description", nullable=true,length=1000 )
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
		return "<a class='delete' title='Remove this entry' href='removeServiceEntry.html?logEntryId="+id+"'>" +
				"<img src='images/delete.jpg' alt='Remove this log entry.' title='Remove this log entry.'></a>";
	}
	@Transient
	public String getEditLink()
	{
		return "<a title='Edit this entry' href='loadServiceEntry.html?logEntryId="+id+"'>" +
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
	
	public boolean equals(ServiceLogEntry slr) 
	{ 
		return new EqualsBuilder().append(getId(), slr.getId()).isEquals();
	}
	
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(getId()).toHashCode();
	}
}
