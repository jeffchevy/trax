package org.trax.dao;

import java.util.List;

import org.trax.model.Award;
import org.trax.model.Course;
import org.trax.model.Leader;

public interface AwardDao extends GenericDao<Award, Long>
{
	public Award getById(long awardId);
	public Award getScoutAward(long scoutId, long awardconfigId) throws Exception;
	public List<Course> getLeaderCourses(Long userId, long courseConfigId);
	public List<Course> getCourses(Leader leader);
}
