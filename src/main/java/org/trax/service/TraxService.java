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
	User saveUser(User user) throws Exception;
	User getUserByUsername(String username);
    Organization refreshOrganization(Long organizationId);
    User saveCredentials(UserCredentialsForm credentials) throws Exception;
	List<LeaderPosition> getLeaderPositions();
	List<ScoutPosition> getScoutPositions();
	Scout refreshScout(Scout scout);
	User refreshUser(long leaderId);
	AwardConfig getAwardConfig(long awardConfigId);
	Organization getUnit(long id);
	void saveScout(Scout scout) throws Exception;
	List<Scout> getScouts(long organizationId, String unitTypeName);
	Collection<Leader> getLeaders(long organizationId, boolean isCub);
	Collection<User> getUsers(long organizationId, boolean isCub);
	User getUserByNameAndOrganization(String firstName, String lastName, Organization unit);
	User getUserById(long userId);
	Map<Long, Long> getAggregateCount(long awardConfigId, Collection<Scout> scouts);
	void updateLeader(Leader user) throws Exception;
	void updateScout(Scout scout);
	
	Collection<AwardConfig> getAllAwards();
	Collection<BadgeConfig> getAllBadges();
	Collection<RankConfig> getAllPalms();
	Collection<DutyToGodConfig> getScoutDtgs();
	Collection<CubDutyToGodConfig> getCubDtgs();
	Collection<CubAwardConfig> getCubAwards();
	Collection<CourseConfig> getAllCourses();
	List<BadgeConfig> getRequiredMeritBadges();
	List<BeltLoopConfig> getAllBeltLoops();
	List<PinConfig> getAllPins();
	List<ActivityBadgeConfig> getAllActivityBadges();

	Award updateAwardEarned(List<Scout> scouts, long awardConfigId, boolean isAwardEarned) throws Exception;
	Award updateAwardInprogress(List<Scout> scouts, long awardConfigId, boolean isAwardInprogress) throws Exception;
	Award updateAward(List<Scout> scouts, long awardConfigId, Date date, boolean isChecked) throws Exception;
	Award updateRequirement(long requirementConfigId, boolean isChecked, User signOffLeader, AwardConfig awardConfig, List<Scout> scouts, String passedOffDateString) throws Exception;
	Award getAward(long awardId);
	void saveServiceLog(List<Scout> scouts, ServiceLogEntry logEntry);
	void saveCampLog(List<Scout> scouts, CampLogEntry logEntry);
	void saveLeadershipLog(List<Scout> scouts, LeadershipLogEntry logEntry);
	User saveAndRegisterUser(User user) throws Exception;
	List<Council> getCouncilsByState(String stateName);
	void retireUser(long userId);
	List<User> getUserByEmail(String email);
	void sendPasswordReset(User realUser);
	User getUserFromPasscode(String userIdKey, String passcode);
	Award findOrCreateNewScoutAward(long scoutId, long awardConfigId) throws Exception;
	Map<String, Map<String, List<String>>> getScoutsUpdates(User user);
	Organization findOrganization(Integer unitNumber, String councilName, BaseUnitType baseUnitType);
	Leader getActiveSeniorLeader(long organizationId, int unitNumber);
	Organization saveOrganization(OrgUnit orgUnit) throws Exception;
	void updateLoginDate(long userId);
	void addTraining(long trainingId, Collection<Long> selections, Date date);
	void addCourse(String courseName, long userId, Date completionDate);
	List<Course> getCourses(Leader leader);
	PinConfig getPinConfig(String awardName);
	AwardConfig getAwardConfig(String awardName);
	List<? extends BaseUnitType> getUnitTypes();
	List<? extends BaseUnitType> getUnitTypes(boolean isCub);
	List getLeaderPositions(boolean isCub);
	
	void addRanks(Scout scout);
	Collection<? extends String> getUnitLeaderEmails(String council, BaseUnitType typeOfUnit, Integer number);
	String transferScout(long scoutId, Leader leader, String councilName) throws Exception;
	Map<Long, Map<Long, Set<Long>>> getScoutsAwardList(List<Scout> scouts, Set<Long> set) throws Exception;
	boolean isAwardComplete(Long scoutId, Long awardConfigId);
	String getRankMeritBadges(String name, List<Scout> scouts);
	void updateAwardPurchased(List<Scout> scouts, Award award, boolean isChecked) throws Exception;
	void updateAwardAwarded(List<Scout> scouts, Award award, boolean isChecked) throws Exception;
	Map<Long, Map<Long, Set<Long>>> getAwardScoutsList(List<Scout> scouts, Set<Long> keySet);
	Map<Long, Map<Long, Set<Long>>> getToFirstClass(List<Scout> scouts) throws Exception;
	Map<Long, Map<Long, Set<Long>>> getToBear(List<Scout> scouts);
}
