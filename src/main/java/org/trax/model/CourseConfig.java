package org.trax.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("T")
public class CourseConfig extends AwardConfig//implements Comparable<AwardConfig>
{
	public CourseConfig(){}
	
	public CourseConfig(String name, String description, int sortOrder, Boolean required)
	{
		this.setName(name);
		this.setDescription(description);
		this.setSortOrder(sortOrder);
		this.setRequired(required);
	}
	
	@Transient
	public String getImageSource()
	{
		return "images/awards/dtg/"+getName()+".png";
	}
}