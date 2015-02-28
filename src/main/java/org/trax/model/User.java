/*
 * Created on Oct 18, 2004
 */
package org.trax.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.util.StringUtils;

@SuppressWarnings("serial")
@DiscriminatorColumn(name="Kind", discriminatorType=DiscriminatorType.STRING, length=1)
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Table(name="User")
public class User implements Serializable
{
	private Date creationDate;
	private Date lastLoginDate;
	private Integer bsaMemberId;
	private boolean isRetired; // 18 year old scouts or leaders maybe retired 
	
    @NotNull(message="First name cannot be null")
    @Size(min=1, max=64, message="First name cannot be null") 
    private String firstName;
    private boolean useMiddleForFirst;
 
	private String middleName;
    private String preferredName;
    
    @NotNull(message="Last name cannot be null")
    @Size(min=1, message="Last name cannot be null")
    private String lastName;
    private String nameSuffix;
    
    @Email
    private String email;
    
    @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid text message number")
    private String textMessageNumber;

    @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone number")
    private String phone;
    @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone number")
    private String cellPhone;
    @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone number")
    private String workPhone;
    
    private String address;

    @NotNull(message="Must specify the city")
    @Size(min=1, message="Must specify the city")
    private String city;
    
    @NotNull(message="Must specify the state")
    @Size(min=1, message="Must specify the state")
    private String state;
    
    private String username;
    private String password;
    
    private String zip;
    
    private boolean accountEnabled;

    /*@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "userId", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "id"))
	*/
    private Collection<Role> roles;
    private Organization organization;
    private Unit unit;
	
//    private Collection<Authority> userAuthorities;
    private long id;
	private Set<Award> awards;
    
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

    @Column (name="firstname", nullable=false, unique=false)
    public String getFirstName()
    {
        return firstName;
    }


    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }


    @Column (name="lastname", nullable=false, unique=false)
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

    @Column (name="preferredName", nullable=true, unique=false)
	public String getPreferredName()
	{
		return preferredName;
	}

	public void setPreferredName(String preferredName)
	{
		this.preferredName = preferredName;
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


    @Column (name="phone", nullable=true, unique=false)
    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    @Column (name="cellPhone", nullable=true, unique=false)
    public String getCellPhone()
    {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone)
    {
        this.cellPhone = cellPhone;
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


    @Column (name="username", nullable=true, unique=true)
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @Column (name="password", nullable=true, unique=false)
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Column (name="accountEnabled", nullable=false, unique=false)
    public boolean getAccountEnabled()
    {
        return accountEnabled;
    }

    public void setAccountEnabled(boolean accountEnabled)
    {
        this.accountEnabled = accountEnabled;
    }
    
	@Transient
	public boolean isAccountNonExpired()
	{
		return false;
	}
	
	@Transient
	public boolean isAccountNonLocked()
	{
		return false;
	}

	@Transient
	public boolean isCredentialsNonExpired()
	{
		return false;
	}

	@Transient
	public boolean isEnabled()
	{
		return true;
	}

    @ManyToOne(fetch=FetchType.EAGER)    
    @JoinColumn(name="organizationId", nullable=false) 
    @ForeignKey(name="FK_USER_ORGANIZATION") 
    public Organization getOrganization()
    {
        return organization;
    }

    public void setOrganization(Organization organization)
    {
        this.organization = organization;
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

	public void setLastLoginDate(Date lastLoginDate)
	{
		this.lastLoginDate = lastLoginDate;
	}

	@Column (name="lastLoginDate", nullable=true, unique=false)
	public Date getLastLoginDate()
	{
		return lastLoginDate;
	}

	public void setBsaMemberId(Integer bsaMemberId)
	{
		this.bsaMemberId = bsaMemberId;
	}

	@Column (name="bsaMemberId")
	public Integer getBsaMemberId()
	{
		return bsaMemberId;
	}

	public void setNameSuffix(String nameSuffix)
	{
		this.nameSuffix = nameSuffix;
	}
	
	@Column (name="nameSuffix")
	public String getNameSuffix()
	{
		return nameSuffix;
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

	@OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="userId")
    @ForeignKey(name="FK_USER_ROLE")
	public Collection<Role> getRoles()
	{
		return roles;
	}
	
	public void setRoles(Collection<Role> roles)
	{
		this.roles = roles;
	}

	public boolean isRolePresent(String role) 
	{
	    boolean isRolePresent = false;
	    for (GrantedAuthority grantedAuthority : this.getAuthorities()) 
	    {
	      isRolePresent = grantedAuthority.getAuthority().equals(role);
	      if (isRolePresent) break;
	    }
	    return isRolePresent;
	}
	/**
	 * for display purposes get the list here
	 * @return
	 */
	@Transient
	public String getRoleNames()
	{
	    return StringUtils.collectionToCommaDelimitedString(roles);
	}

    @Transient
    public Collection<GrantedAuthority> getAuthorities()
    {
        Collection<GrantedAuthority> grantedAuthority = new ArrayList<GrantedAuthority>();
        for (Role role : this.getRoles())
		{
			grantedAuthority.add(new GrantedAuthorityImpl(role.getName()));
		}
        return grantedAuthority;
    }
    
    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)    
    @JoinColumn(name="unitId", nullable=false, unique=false) 
    @ForeignKey(name="FK_USER_UNIT") 
	public Unit getUnit()
	{
		return unit;
	}

	public void setUnit(Unit unit)
	{
		this.unit = unit;
	}
	
	public void setUnitCopy(Unit unit)
	{
		this.unit = new Unit(unit.getTypeOfUnit(), unit.getNumber(), unit.getOrganization());
	}
	
	@Column (name="useMiddleForFirst", nullable=false, unique=false)
   	public boolean getUseMiddleForFirst()
	{
		return useMiddleForFirst;
	}
	

	public void setUseMiddleForFirst(boolean useMiddleForFirst)
	{
		this.useMiddleForFirst = useMiddleForFirst;
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
	
	public boolean equals(User user) 
	{ 
		int number = getUnit()==null?0:getUnit().getNumber();
		int userNumber = user.getUnit()==null?0:user.getUnit().getNumber();
		return new EqualsBuilder().append(getFullName(), user.getFullName())
				.append(number, userNumber)
				.append(getEmail(), user.getEmail()).isEquals();
	}
	
	@Override
	public final int hashCode()
	{
		//if the number is null, there is a problem, but validation will catch it so set to 0 here
		int number = getUnit()==null?0:getUnit().getNumber()==null?0:getUnit().getNumber();
		return new HashCodeBuilder().append(getFullName())
			.append(number)
			.append(getEmail()).toHashCode();
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "scoutId", nullable = false, unique = false)
	@ForeignKey(name = "FK_SCOUT_AWARD")
	@OrderBy(value = "awardConfig")
	public Set<Award> getAwards()
	{
		return awards;
	}

	public void setAwards(Set<Award> awards)
	{
		this.awards = awards;
	}
}
