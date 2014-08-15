package org.trax.model;

import java.io.Serializable;
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

import org.hibernate.annotations.ForeignKey;

@SuppressWarnings("serial")
@Entity
@Table(name = "Requirement")
public class Requirement implements Serializable
{
	private User user;
	private Date dateCompleted;
	private RequirementConfig requirementConfig;
	private long id;
	private Date dateEntered;

	
	public Requirement()
	{
	}
	
	
	public Requirement(RequirementConfig requirementConfig)
	{
		super();
		this.requirementConfig = requirementConfig;
	}


	public Requirement(RequirementConfig rc, Date dateCompleted, User signOffLeader)
	{
		this.requirementConfig = rc;
		setDateCompleted(dateCompleted);
		this.user = signOffLeader;
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
	@ForeignKey(name = "FK_REQUIREMENT_USER")
	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requirementConfigId", nullable=false)
	@ForeignKey(name = "FK_REQUIREMENT_REQUIREMENTCONFIG")
	public RequirementConfig getRequirementConfig()
	{
		return requirementConfig;
	}

	public void setRequirementConfig(RequirementConfig requirementConfig)
	{
		this.requirementConfig = requirementConfig;
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
}
