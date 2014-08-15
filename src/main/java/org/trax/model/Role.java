package org.trax.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotEmpty;

@SuppressWarnings("serial")
@Entity
@Table(name="Role")
public class Role implements Serializable 
{
	@NotEmpty
	private String name;
    private long id;
    /*@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), 
    		inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    		*/
    //private Set<Permission> permissions;

    //@ManyToMany(mappedBy = "roles")
    //private Set<User> members;

    /*
    public Set<Permission> getPermissions()
	{
		return permissions;
	}


	public void setPermissions(Set<Permission> permissions)
	{
		this.permissions = permissions;
	}


	public Set<User> getMembers()
	{
		return members;
	}


	public void setMembers(Set<User> members)
	{
		this.members = members;
	}
*/

	public Role()
	{
	}

    
	public Role(String name)
	{
		super();
		this.name = name;
	}


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
    

	@Column (name="name", nullable=false)
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean equals(Role other) 
	{ 
		return new EqualsBuilder().append(getName(), other.getName()).isEquals();
	}
	
	@Override
	public final int hashCode()
	{
		return new HashCodeBuilder().append(getName()).toHashCode();
	}
}
