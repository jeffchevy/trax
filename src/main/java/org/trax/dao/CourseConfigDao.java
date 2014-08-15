package org.trax.dao;

import org.trax.model.CourseConfig;

public interface CourseConfigDao extends GenericDao<CourseConfig, Long>
{
	public CourseConfig getByName(String name);
}
