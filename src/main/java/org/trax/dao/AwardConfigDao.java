package org.trax.dao;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.trax.dto.RankTrail;
import org.trax.model.AwardConfig;
import org.trax.model.BadgeConfig;
import org.trax.model.CourseConfig;
import org.trax.model.DutyToGodConfig;
import org.trax.model.RankConfig;
import org.trax.model.cub.CubAwardConfig;
import org.trax.model.cub.CubDutyToGodConfig;

public interface AwardConfigDao extends GenericDao<AwardConfig, Long>
{
	public AwardConfig getByName(String name);
	public AwardConfig getByDescription(String description);
	public Collection<BadgeConfig> getAllBadges();
	public Collection<DutyToGodConfig> getScoutDtgs();
	public Collection<CubDutyToGodConfig> getCubDtgs();
	public Collection<AwardConfig> getAllAwards();
	public Collection<RankConfig> getAllPalms();
	public Collection<CourseConfig> getAllCourses();
	public CourseConfig getCourseByName(String courseName);
	public Collection<CubAwardConfig> getCubAwards();
	public List<RankTrail> getRankTrailToFirstClass();
}
