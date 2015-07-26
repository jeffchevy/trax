package org.trax.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@SuppressWarnings("serial")
@Entity
@Table(name="Sponsor")
public class Sponsor implements Serializable
{
	private Date creationDate;
	private Date lastLoginDate;
	private boolean isRetired;
	private byte[] logo;
	private String companyName;
    private String firstName;
	private String middleName;
    private String lastName;
    private String link;
    
    @Email
    private String email;
    
    @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid text message number")
    private String textMessageNumber;

    @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone number")
    private String workPhone;
    
    private String address;

    @NotNull(message="Must specify the city")
    @Size(min=1, message="Must specify the city")
    private String city;
    
    @NotNull(message="Must specify the state")
    @Size(min=1, message="Must specify the state")
    private String state;
    
    private String zip;
    
    private long id;
    
    public void setId(long id)
    {
        this.id = id;
    }

	@Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    public long getId()
    {
        return id;
    }

    @Column (name="firstname", nullable=true, unique=false)
    public String getFirstName()
    {
        return firstName;
    }


    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }


    @Column (name="lastname", nullable=true, unique=false)
    public String getLastName()
    {
        return lastName;
    }

    @Column (name="middleName", nullable=true, unique=false)
    public String getMiddleName()
	{
		return middleName;
	}

	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}

	public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    @Transient
    public String getFullName()
    {
    	return firstName + " " + lastName;
    }
    
    @Column (name="email", nullable=true, unique=false)
    public String getEmail()
    {
        return email;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    @Column (name="workPhone", nullable=true, unique=false)
    public String getWorkPhone()
    {
        return workPhone;
    }

    public void setWorkPhone(String workPhone)
    {
        this.workPhone = workPhone;
    }
    @Column (name="address", nullable=true, unique=false)
    public String getAddress()
    {
        return address;
    }


    public void setAddress(String address)
    {
        this.address = address;
    }


    @Column (name="city", nullable=false, unique=false)
    public String getCity()
    {
        return city;
    }


    public void setCity(String city)
    {
        this.city = city;
    }


    @Column (name="state", nullable=false, unique=false)
    public String getState()
    {
        return state;
    }


    public void setState(String state)
    {
        this.state = state;
    }


    @Column (name="zip", nullable=true, unique=false)
    public String getZip()
    {
        return zip;
    }


    public void setZip(String zip)
    {
        this.zip = zip;
    }


	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	@Column (name="creationDate")
	public Date getCreationDate()
	{
		return creationDate;
	}

	public void setTextMessageNumber(String textMessageNumber)
	{
		this.textMessageNumber = textMessageNumber; 
	}
	
	@Column (name="textMessageNumber", nullable=true, unique=false)
	public String getTextMessageNumber()
	{
		return textMessageNumber;
	}

	@Column (name="retired", nullable=false, unique=false)
   	public boolean getRetired()
	{
		return isRetired;
	}
	
	public void setRetired(boolean isRetired)
	{
		this.isRetired = isRetired;
	}

	public boolean isRetired()
	{
		return isRetired;
	}
	
	@Basic(fetch=FetchType.EAGER)
    @Lob @Column(name="logo")
    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    @Column (name="link", nullable=true, unique=false)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Column (name="companyName", nullable=true, unique=false)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


}
