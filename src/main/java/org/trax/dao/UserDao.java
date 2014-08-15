package org.trax.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.trax.model.BaseUnitType;
import org.trax.model.Leader;
import org.trax.model.Organization;
import org.trax.model.Scout;
import org.trax.model.User;

public interface UserDao extends GenericDao<User, Long>
{
	public Map<Long, Long> getAggregateCount(long awardConfigId, Collection<Scout> scouts);
	public User getUserByUsername(String username);   
	
	public User findUser(String username, String password);
	
	public List<User> getByUnitId(long unitId);
	public List<User> getByOrganizationId(long organizationId, boolean isCub);
	public List<Scout> getScouts(long unitId, BaseUnitType baseUnitType);
	public List<Leader> getLeaders(long organizationId, boolean isCub);
	public List<Leader> getLeaders(long organizationId, int unitNumber);
	public List<Leader> getLeadersByUnitId(long unitId);

	public User getUserByNameAndOrg(String firstName, String lastName, long id);
	public User getUserByFullnameAndOrg(String firstName, String middleName, String lastName, long organizationId);
	
	public Collection<User> getByIds(Collection<Long> ids);
	public List<Scout> getScoutsWithBirthdaySoon(int months);
	public List<User> findByEmail(String email);
	public Map<String, Map<String, List<String>>> getScoutsUpdates(User user);
	public List<Scout> getScoutsNeedingBoardOfReview();
	public Collection<? extends User> getNewUsers(int numberOfDays);
	public boolean automaticallyAdvanceToUnit(Scout scout);
	public List<User> getByUnit(String council, BaseUnitType typeOfUnit, Integer number);
	public User saveUser(User user);
	public Map<Long, Map<Long, Set<Long>>> getScoutsAwardList(List<Long> scoutIds, Set<Long> awardList);
	boolean isAwardComplete(Long scoutId, Long awardConfigId);
	
}
