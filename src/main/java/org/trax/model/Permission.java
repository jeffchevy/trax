package org.trax.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.trax.persistence.AbstractPersistent;

@SuppressWarnings("serial")
@Entity
@Table(name = "Permission")
public class Permission extends AbstractPersistent<Permission>
{

	private Long id;

    @NotEmpty
    private String name;

    /**
     * @return the id
     */
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    public Long getId()
    {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @return the name
     */
    @Column (name="name", nullable=false)
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name)
    {
        this.name = name.trim();
    }

    @Override
    protected boolean requiredEquals(Permission obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Permission other = (Permission) obj;
        if (this.id != other.id)
        {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name))
        {
            return false;
        }
        return true;
    }

    @Override
    protected int requiredHashCode()
    {
        return new HashCodeBuilder().append(getName()).toHashCode();
    }

    public String toString()
    {
        return this.name;
    }

}
