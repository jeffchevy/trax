package org.trax.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.trax.form.OrgUnit;
import org.trax.form.UserCredentialsForm;
import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.BadgeConfig;
import org.trax.model.BaseUnitType;
import org.trax.model.CampLogEntry;
import org.trax.model.Council;
import org.trax.model.Course;
import org.trax.model.CourseConfig;
import org.trax.model.DutyToGodConfig;
import org.trax.model.Leader;
import org.trax.model.Leader.LeaderPosition;
import org.trax.model.LeadershipLogEntry;
import org.trax.model.Organization;
import org.trax.model.RankConfig;
import org.trax.model.Scout;
import org.trax.model.Scout.ScoutPosition;
import org.trax.model.ServiceLogEntry;
import org.trax.model.User;
import org.trax.model.cub.ActivityBadgeConfig;
import org.trax.model.cub.BeltLoopConfig;
import org.trax.model.cub.CubAwardConfig;
import org.trax.model.cub.CubDutyToGodConfig;
import org.trax.model.cub.PinConfig;

/**
 * Holds the service methods used by the Trax Application
 */
public interface TraxService
{
	public User saveUser(User user) throws Exception;
	public User getUserByUsername(String username);
    public Organization refreshOrganization(Long organizationId);
    public User saveCredentials(UserCredentialsForm credentials) throws Exception;
	public List<LeaderPosition> getLeaderPositions();
	public List<ScoutPosition> getScoutPositions();
	public Scout refreshScout(Scout scout);
	public User refreshUser(long leaderId);
	public AwardConfig getAwardConfig(long awardConfigId);
	public Organization getUnit(long id);
	public void saveScout(Scout scout) throws Exception;
	public List<Scout> getScouts(long organizationId, String unitTypeName);
	public Collection<Leader> getLeaders(long organizationId, boolean isCub);
	public Collection<User> getUsers(long organizationId, boolean isCub);
	public User getUserByNameAndOrganization(String firstName, String lastName, Organization unit);
	public User getUserById(long userId);
	public Map<Long, Long> getAggregateCount(long awardConfigId, Collection<Scout> scouts);
	void updateLeader(Leader user) throws Exception;
	public void updateScout(Scout scout);
	
	public Collection<AwardConfig> getAllAwards();
	public Collection<BadgeConfig> getAllBadges();
	public Collection<RankConfig> getAllPalms();
	public Collection<DutyToGodConfig> getScoutDtgs();
	public Collection<CubDutyToGodConfig> getCubDtgs();
	public Collection<CubAwardConfig> getCubAwards();
	public Collection<CourseConfig> getAllCourses();
	public List<BadgeConfig> getRequiredMeritBadges();
	public List<BeltLoopConfig> getAllBeltLoops();
	public List<PinConfig> getAllPins();
	public List<ActivityBadgeConfig> getAllActivityBadges();

	public Award updateAwardEarned(List<Scout> scouts, AwardConfig awardConfig, boolean isAwardEarned) throws Exception;
	public Award updateAwardInprogress(List<Scout> scouts, AwardConfig awardConfig, boolean isAwardInprogress) throws Exception;
	public Award updateAward(User signOffLeader, List<Scout> scouts, AwardConfig awardConfig, Date date, boolean isChecked) throws Exception;
	public Award updateRequirement(long requirementConfigId, boolean isChecked, User signOffLeader, AwardConfig awardConfig, List<Scout> scouts, String passedOffDateString) throws Exception;
	public Award getAward(long awardId);
	public void saveServiceLog(List<Scout> scouts, ServiceLogEntry logEntry);
	public void saveCampLog(List<Scout> scouts, CampLogEntry logEntry);
	public void saveLeadershipLog(List<Scout> scouts, LeadershipLogEntry logEntry);
	public User saveAndRegisterUser(User user) throws Exception;
	public List<Council> getCouncilsByState(String stateName);
	public void retireUser(long userId);
	public List<User> getUserByEmail(String email);
	public void sendPasswordReset(User realUser);
	public User getUserFromPasscode(String userIdKey, String passcode);
	public Award findOrCreateNewScoutAward(long scoutId, long awardConfigId) throws Exception;
	public Map<String, Map<String, List<String>>> getScoutsUpdates(User user);
	public Organization findOrganization(Integer unitNumber, String councilName, BaseUnitType baseUnitType);
	public Leader getActiveSeniorLeader(long organizationId, int unitNumber);
	public Organization saveOrganization(OrgUnit orgUnit) throws Exception;
	public void updateLoginDate(long userId);
	public void addTraining(long trainingId, Collection<Long> selections, Date date);
	public void addCourse(String courseName, long userId, Date completionDate);
	public List<Course> getCourses(Leader leader);
	public PinConfig getPinConfig(String awardName);
	public AwardConfig getAwardConfig(String awardName);
	public List<? extends BaseUnitType> getUnitTypes();
	public List<? extends BaseUnitType> getUnitTypes(boolean isCub);
	public List getLeaderPositions(boolean isCub);
	
	public void addRanks(Scout scout);
	public Collection<? extends String> getUnitLeaderEmails(String council, BaseUnitType typeOfUnit, Integer number);
	public String transferScout(long scoutId, Leader leader, String councilName) throws Exception;
	public Map<Long, Map<Long, Set<Long>>> getScoutsAwardList(List<Scout> scouts, Set<Long> set) throws Exception;
	boolean isAwardComplete(Long scoutId, Long awardConfigId);
}
