package org.trax.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.Query;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.trax.model.BaseUnitType;
import org.trax.model.Leader;
import org.trax.model.Scout;
import org.trax.model.Unit;
import org.trax.model.User;
import org.trax.model.cub.CubUnitType;

@Repository("userDao")
public class UserJpaDao extends GenericJpaDao<User, Long> implements UserDao
{
	public Map<Long, Long> getAggregateCount(long awardConfigId, Collection<Scout> scouts)
	{
		Set<Long> scoutIds = new HashSet<Long>();
		Map<Long, Long> aggregate = null;
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (user instanceof Leader)
		{
			aggregate = new HashMap<Long, Long>();

			for (Scout scout : scouts)
			{
				if (scout.isChecked() || scout.isSelected())
				{
					scoutIds.add(scout.getId());
				}
			}
			if (scoutIds.size() < 2)
			{
				return null;
			}

			String idString = scoutIds.toString().replace('[', ' ').replace(']', ' ');

			String sql = "select r.requirementConfigId, count(r.requirementConfigId) from user u, award a, requirement r" + " where u.kind='S' and u.id in (" + idString
							+ ") and a.awardConfigId = " + awardConfigId + "	and a.scoutId=u.id " + "	and r.awardId = a.id group by r.requirementConfigId";
			Query query = entityManager.createNativeQuery(sql);
			List results = query.getResultList();
			for (Object oRow : results)
			{
				Object[] r = (Object[]) oRow;
				aggregate.put(((BigInteger) r[0]).longValue(), ((BigInteger) r[1]).longValue());
			}
		}

		return aggregate;
	}

	/**
	 * each scout has a different award with the same awardconfig get the
	 * individual scouts award
	 * 
	 * @param scoutId
	 * @param awardconfigId
	 * @return
	 */
	// TODO need to rewrite using HQL
	// public Map<Long, Long> getAggregateCount(long awardConfigId,
	// Collection<Scout> scouts)
	// {
	// Set<Long> scoutIds = new HashSet<Long>();
	// Map<Long, Long> aggregate = new HashMap<Long, Long>();
	// for (Scout scout : scouts)
	// {
	// scoutIds.add(scout.getId());
	// }
	// String idString = scoutIds.toString().replace('[', ' ').replace(']',
	// ' ');
	//
	// String hql =
	// "Select a from AwardConfig ac, Scout s Left join s.awards as scoutAward where s.id = "+scoutId+
	// " and a.awardConfig.id="+awardConfigId +
	// " and scoutAward.id = a.id";
	// Query query = entityManager.createQuery(hql);
	// Award award = null;
	// try
	// {
	// award = (Award)query.getSingleResult();
	// }
	// catch (NoResultException e)
	// {
	// return award;
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// return award;
	// }

	public Map<String, Map<String, List<String>>> getScoutsUpdates(User user)
	{
		Map<String, Map<String, List<String>>> updateMap = new HashMap<String, Map<String, List<String>>>();
		getScoutsRequirementsUpdates(user, updateMap);
		getScoutsAwardsUpdates(user, updateMap);
		getScoutsLogUpdates(user, updateMap, "service", "typeOfProject");
		getScoutsLogUpdates(user, updateMap, "camp", "location");
		// getScoutsLeadershipUpdates(leader, updateMap, "text"); this does not
		// work since leadership entries are Enumeratedtypes
		return updateMap;
	}

	/*
	 * get the awardnames for requirements just passed off no details for now,
	 * (String)r[3]
	 */
	private void getScoutsRequirementsUpdates(User user, Map<String, Map<String, List<String>>> updateMap)
	{
		String sql = "select s.firstName, s.lastName, ac.name, s.email, rc.text from user s, award a, requirement r, requirementConfig rc, awardConfig ac" + " where s.kind='S' "
						+ "	and r.dateEntered >= NOW() - INTERVAL 1 HOUR " + "	and r.requirementConfigId = rc.id" + "	and r.userId = "
						+ user.getId()// this is the user that is logging out,
										// and passed off data
						+ "	and r.awardId = a.id" + "	and a.awardconfigid = ac.id" + "	and a.scoutId=s.id " + "	and s.organizationId = " + user.getOrganization().getId()
						+ " group by a.id order by ac.id";// " order by rc.id";
		Query query = entityManager.createNativeQuery(sql);
		List results = query.getResultList();

		dbToPassedOff(results, "partialAward", updateMap);
	}

	private void dbToPassedOff(List results, String typePassedOff, Map<String, Map<String, List<String>>> updateMap)
	{
		for (Object oRow : results)
		{
			Object[] r = (Object[]) oRow;
			String fullName = (String) r[0] + " " + (String) r[1];
			String awardName = (String) r[2];
			String email = (String) r[3];
			if (updateMap.containsKey(fullName))
			{
				List<String> awardNames = updateMap.get(fullName).get(typePassedOff);
				if (awardNames == null)
				{
					awardNames = new ArrayList<String>();
					updateMap.get(fullName).put(typePassedOff, awardNames);
				}
				awardNames.add(awardName);
			}
			else
			{
				Map<String, List<String>> partialAwardMap = new HashMap<String, List<String>>();
				List<String> awardNames = new ArrayList<String>();
				awardNames.add(awardName);
				partialAwardMap.put(typePassedOff, awardNames);
				updateMap.put(fullName, partialAwardMap);
			}

			if (!updateMap.containsKey("emailList"))
			{
				// kind of a kludge, put the email in a list and send it back
				List<String> emailList = new ArrayList<String>();
				emailList.add(email);
				updateMap.get(fullName).put("emailList", emailList);
			}

		}
	}

	private void getScoutsAwardsUpdates(User user, Map<String, Map<String, List<String>>> updateMap)
	{
		String sql = "select s.firstName, s.lastName, ac.name, s.email from user s, award a, awardConfig ac" + " where s.kind='S' "
						+ "	and a.dateEntered >= NOW() - INTERVAL 1 HOUR " + "	and a.awardconfigid = ac.id" + "	and a.scoutId=s.id " + "	and a.userId = " + user.getId()
						+ "	and s.organizationId = " + user.getOrganization().getId() + " and retired = 0 " + " order by ac.id";
		Query query = entityManager.createNativeQuery(sql);
		List results = query.getResultList();
		dbToPassedOff(results, "award", updateMap);
	}

	private void getScoutsLogUpdates(User user, Map<String, Map<String, List<String>>> updateMap, String logType, String columnName)
	{
		String sql = "select s.firstName, s.lastName, sl." + columnName + ", s.email  from user s, " + logType + "log sl" + "	where s.kind='S' "
						+ "	and sl.dateEntered >= NOW() - INTERVAL 1 HOUR " + "	and sl.scoutId=s.id " + "	and sl.userId = " + user.getId() + "	and s.organizationId = "
						+ user.getOrganization().getId() + " and retired = 0 " + " order by sl.id";
		Query query = entityManager.createNativeQuery(sql);
		List results = query.getResultList();
		dbToPassedOff(results, logType, updateMap);
	}

	public List<Scout> getScoutsWithBirthdaySoon(int months)
	{
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.MONTH, -months);
		from.set(Calendar.HOUR_OF_DAY, 0);
		from.set(Calendar.MINUTE, 0);
		from.set(Calendar.SECOND, 0);
		from.set(Calendar.MILLISECOND, 0);
		Date fromDate = from.getTime();

		to.add(Calendar.MONTH, -months);
		to.set(Calendar.HOUR_OF_DAY, 0);
		to.set(Calendar.MINUTE, 0);
		to.set(Calendar.SECOND, 0);
		to.set(Calendar.MILLISECOND, 0);
		to.add(Calendar.DATE, 1);
		Date toDate = to.getTime();
		System.out.println(months + "checking from " + fromDate + " to " + toDate);
		String hql = "FROM Scout WHERE birthDate >= :dateFrom AND birthDate < :dateTo and retired = 0";
		List<Scout> scouts = entityManager.createQuery(hql).setParameter("dateFrom", fromDate).setParameter("dateTo", toDate).getResultList();

		/*
		 * Calendar startCalendar = Calendar.getInstance();
		 * 
		 * startCalendar.add(Calendar.MONTH, -months); // subtracting 12 years
		 * and // 1 month Date startDate = startCalendar.getTime(); String hql =
		 * "from Scout scout where scout.birthDate = :start and retired = 0";
		 * 
		 * List<Scout> scouts =
		 * entityManager.createQuery(hql).setParameter("start",
		 * startDate).getResultList();
		 */
		return scouts;
	}

	public List<Scout> getScoutsNeedingBoardOfReview()
	{
		/*
		 * String hql =
		 * "select a.id, .9<(count(*)/(select count(*) from awardconfig ac," +
		 * "RequirementConfig rc where a.awardconfigid=ac.id and ac.kind='R' and rc.awardconfigId=ac.id group by ac.id))"
		 * +"from award a, requirement r where a.dateCompleted is null"
		 * +"and a.Kind='R' and r.awardId=a.id group by a.id"; Query query =
		 * entityManager.createNativeQuery(sql); List results =
		 * query.getResultList(); for (Object oRow : results) { Object[] r =
		 * (Object[]) oRow; aggregate.put(((BigInteger) r[0]).longValue(),
		 * ((BigInteger) r[1]).longValue()); }
		 */
		// find all scouts who have a Rank that is 90% done
		// String hql =
		// "from Scout scout where scout. = :start and retired = 0";
		// List<Scout> scouts =
		// entityManager.createQuery(hql).setParameter("start",
		// startDate).getResultList();
		// for (Award award : scoutReport.getAwards())
		// {
		// AwardConfig awardConfig = award.getAwardConfig();
		//
		// if(award.getDateCompleted() == null)
		// {
		// double percent = Math.ceil((((double) award.getRequirements().size())
		// / awardConfig.getRequirementConfigs().size() * 100));
		// award.setPercentComplete(percent);
		// }
		// }
		return new ArrayList<Scout>();
	}

	public Collection<? extends User> getNewUsers(int numberOfDays)
	{

		Calendar from = Calendar.getInstance();
		from.add(Calendar.DATE, -numberOfDays);
		Date fromDate = from.getTime();

		System.out.println("checking from " + fromDate + " to today");
		String hql = "FROM User WHERE creationDate >= :dateFrom and retired = 0";
		List<User> users = entityManager.createQuery(hql).setParameter("dateFrom", fromDate).getResultList();

		return users;
	}

	public User getUserByUsername(String username)
	{
		String hql = "from User where username = :username and retired = 0 ";
		List<User> userList = entityManager.createQuery(hql).setParameter("username", username).getResultList();

		return userList.size() > 0 ? userList.get(0) : null;
	}

	public User findUser(String username, String password)
	{
		String hql = "from User user where User.username = :username and user.password = :password and retired = 0 ";
		List<User> userList = entityManager.createQuery(hql).setParameter("username", username).setParameter("password", password).getResultList();

		return userList.size() > 0 ? userList.get(0) : null;
	}

	public List<Leader> getLeaders(long organizationId, boolean isCub)
	{
		String unitType = isCub ? "CubUnitType" : "ScoutUnitType";
		String hql = "from Leader where organization.id = :organizationId and unit.typeOfUnit.class=" + unitType + " and retired = 0 order by lastName ASC";
		return entityManager.createQuery(hql).setParameter("organizationId", organizationId).getResultList();
	}

	public List<Leader> getLeaders(long organizationId, int unitNumber)
	{
		// return the leaders for just this unit number, they must be active and
		// have logged in
		String hql = "from Leader where organization.id = :organizationId and retired = 0 and accountEnabled = 1"
						+ "and unitId = (select min(id) from Unit where number= :unitNumber and organizationId = :organizationId) order by lastName ASC";
		List<Leader> leaders = entityManager.createQuery(hql).setParameter("organizationId", organizationId).setParameter("unitNumber", unitNumber).getResultList();

		return leaders;
	}

	public List<User> getByOrganizationId(long organizationId, boolean isCub)
	{
		String unitType = isCub ? "CubUnitType" : "ScoutUnitType";
		String hql = "from User user where organization.id = :organizationId and unit.typeOfUnit.class=" + unitType
						+ " and retired = 0 order by birthDate, firstName, lastName ASC";
		return entityManager.createQuery(hql).setParameter("organizationId", organizationId).getResultList();
	}

	public List<User> getByUnitId(long unitId)
	{
		// 0 for false? should be isRetired = false?
		String hql = "from User user where unit.id = :unitId and retired = 0";
		List<User> users = entityManager.createQuery(hql).setParameter("unitId", unitId).getResultList();

		return users;
	}

	public List<User> getByUnit(String council, BaseUnitType typeOfUnit, Integer number)
	{
		// 0 for false? should be isRetired = false?
		String hql = "from User user where organization.council=:council and unit.number=:number and unit.typeOfUnit.name=:unitTypeName and retired = 0";
		List<User> users = entityManager.createQuery(hql).setParameter("council", council).setParameter("number", number).setParameter("unitTypeName", typeOfUnit.getName())
						.getResultList();

		return users;
	}

	public boolean automaticallyAdvanceToUnit(Scout scout)
	{
		boolean changedTypeOfUnit = false;
		// for now only change unit automatically for cubs to 11 year old scouts
		if (scout.getAge() == 11)
		{
			Collection<Unit> units = scout.getOrganization().getUnits();
			for (Iterator iterator = units.iterator(); iterator.hasNext();)
			{
				Unit unit = (Unit) iterator.next();
				int unitStartAge = unit.getTypeOfUnit().getStartAge();
				if (unitStartAge == scout.getAge() && scout.getUnit().getTypeOfUnit().getStartAge() != unitStartAge)// only
																													// update
																													// if
																													// not
																													// already
																													// there
				{
					// has an eleven year old unit-move the scout to this unit
					scout.getUnit().setTypeOfUnit(unit.getTypeOfUnit());
					changedTypeOfUnit = true;
				}
			}
		}
		return changedTypeOfUnit;
	}

	public List<Leader> getLeadersByUnitId(long unitId)
	{
		// 0 for false? should be isRetired = false?
		String hql = "from Leader leader where unit.id = :unitId and retired = 0";
		List<Leader> users = entityManager.createQuery(hql).setParameter("unitId", unitId).getResultList();

		return users;
	}

	public List<Scout> getScouts(long organizationId, BaseUnitType baseUnitType)
	{

		List<Scout> scouts = new ArrayList<Scout>();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		BaseUnitType unitType = user.getUnit().getTypeOfUnit();

		if (baseUnitType == null || "Pack".equals(baseUnitType.getName()))// get
																			// all
		{
			String hql = "from Scout where organization.id=:organizationId and unit.typeOfUnit.class=" + (unitType instanceof CubUnitType ? "CubUnitType" : "ScoutUnitType")
							+ " and retired = 0 order by birthDate, firstName, lastName ASC";
			scouts = entityManager.createQuery(hql).setParameter("organizationId", organizationId).getResultList();

			return scouts;
		}

		/*
		 * @TODO should make it so when a cub turns 11 automatically moved to
		 * trooop? if(unitType instanceof ScoutUnitType) { Date startDate =
		 * null; Date endDate = null;
		 * 
		 * Calendar endCalendar = Calendar.getInstance(); Calendar startCalendar
		 * = Calendar.getInstance();
		 * 
		 * startCalendar.add(Calendar.YEAR, -baseUnitType.getEndAge());
		 * startDate = startCalendar.getTime(); endCalendar.add(Calendar.YEAR,
		 * -baseUnitType.getStartAge()); endDate = endCalendar.getTime(); String
		 * hql =
		 * "from Scout scout where scout.organization.id=:organizationId and scout.retired = 0 "
		 * +
		 * "and scout.birthDate between :start and :end order by birthDate, firstName, lastName ASC"
		 * ; List resultList =
		 * entityManager.createQuery(hql).setParameter("organizationId",
		 * organizationId) .setParameter("start", startDate).setParameter("end",
		 * endDate).getResultList(); scouts.addAll(new TreeSet(resultList)); }
		 */

		// try to get them by their unittype
		String hql = "from Scout where organization.id=:organizationId and retired = 0 " + "and unit.typeOfUnit.id=:unitTypeId order by firstName, lastName ASC";
		scouts = entityManager.createQuery(hql).setParameter("organizationId", organizationId).setParameter("unitTypeId", baseUnitType.getId()).getResultList();
		return scouts;
	}

	public User getUserByNameAndOrg(String firstName, String lastName, long organizationId)
	{
		String hql = "from User user where user.firstName = :firstName and user.lastName = :lastName and user.organization.id = :organizationId and retired = 0";
		List<User> userList = entityManager.createQuery(hql).setParameter("firstName", firstName).setParameter("lastName", lastName).setParameter("organizationId", organizationId)
						.getResultList();

		return userList.size() > 0 ? userList.get(0) : null;
	}

	public User getUserByFullnameAndOrg(String firstName, String middleName, String lastName, long organizationId)
	{
		List<User> userList = null;
		String firstLastOrg = "from Scout user where user.firstName = :firstName and user.lastName = :lastName and user.organization.id = :organizationId";
		String notRetired = " and retired = 0";
		String middleNull = " and user.middleName is null";
		String middle = " and user.middleName = :middleName";

		//first try to get the user by only first and last, this way if middle name is out of sync with the council, it will still match
		String hql = firstLastOrg + notRetired;
		userList = entityManager.createQuery(hql)
						.setParameter("firstName", firstName)
						.setParameter("lastName", lastName)
						.setParameter("organizationId", organizationId)
						.getResultList();
		
		if (userList.size() == 0)
		{
			//include retired
			hql = firstLastOrg;
			userList = entityManager.createQuery(hql)
							.setParameter("firstName", firstName)
							.setParameter("lastName", lastName)
							.setParameter("organizationId", organizationId).getResultList();
		}	
		
		if (userList.size() > 1) //search again using middle
		{
			if (middleName == null)
			{
				// first and last name are required but middle may be null
				hql = firstLastOrg + middleNull;
				userList = entityManager.createQuery(hql)
								.setParameter("firstName", firstName)
								.setParameter("lastName", lastName)
								.setParameter("organizationId", organizationId)
								.getResultList();
			}
			else
			{
				//more than one boy matched by firstname and lastname, use middle name to make sure there is only one
				hql = firstLastOrg + middle;
				userList = entityManager.createQuery(hql)
								.setParameter("firstName", firstName)
								.setParameter("middleName", middleName)
								.setParameter("lastName", lastName)
								.setParameter("organizationId", organizationId).getResultList();				
			}
		}
		
		User foundUser = null;
		//if we have multiple boys matching the same name, we just want one that is active. There should never be more than one that is active.
		if(userList.size()==1)
		{
			foundUser = userList.get(0);
		}
		else if (userList.size() > 0)
		{
			for (User user : userList)
			{
				if(! user.isRetired())
				{
					foundUser = user;
				}
			}
			if (foundUser==null && userList.size()>0)
			{
				//there is an only an inactive user, return that
				foundUser = userList.get(0);
			}
		}
		return foundUser;
	}

	public Collection<User> getByIds(Collection<Long> userIds)
	{
		String hql = "from User user where user.id in (:userIds) and user.retired = 0 order by birthDate, firstName, lastName ASC";
		Query query = entityManager.createQuery(hql);
		query.setParameter("userIds", userIds);
		return query.getResultList();
	}

	public List<User> findByEmail(String email)
	{
		String hql = "from User where email = :email and retired = 0";
		List<User> userList = entityManager.createQuery(hql).setParameter("email", email).getResultList();

		return userList;
	}

	// TODO for some reason, the organization does not get set automatically on
	// the unit, so save it here
	public User saveUser(User user)
	{
		for (Unit unit : user.getOrganization().getUnits())
		{
			if (unit.getOrganization() == null)
			{
				unit.setOrganization(user.getOrganization());
			}
			
			if (user.getUnit().getOrganization() == null && unit.getTypeOfUnit().equals(user.getUnit().getTypeOfUnit()))
			{
				user.setUnit(unit);
			}
		}
		return this.save(user);
	}

	@Override
	public Map<Long, Map<Long, Set<Long>>> getScoutsAwardList(List<Long> scoutIds, Set<Long> awardConfigIds)
	{
		Map<Long, Map<Long, Set<Long>>> scoutAwardRequirements = new HashMap<Long, Map<Long, Set<Long>>>();
		String scoutIdString = scoutIds.toString().replace('[', ' ').replace(']', ' ');
		String awardConfigIdString = awardConfigIds.toString().replace('[', ' ').replace(']', ' ');
		/*
		 * String select =
		 * "select u.id, a.awardConfigId, r.requirementconfigId, a.dateCompleted"
		 * ; String from =
		 * " from user u, award a, requirement r, awardconfig ac, requirementConfig rc "
		 * ; String where = " u.id in ("+scoutIdString+
		 * ") and u.kind='S' and a.scoutid=u.id and r.awardid=a.id "
		 * +"	  and a.awardconfigid in ("+awardConfigIdString+
		 * ") and a.awardconfigid=ac.id and r.requirementconfigId=rc.id"; //
		 * order by u.id, rc.sortOrder"; String sql = select + from + " where "
		 * + where + " union "
		 * +select+from+" where a.dateCompleted is not null and "+where;
		 */
		String sql = "select u.id, a.awardConfigId, r.requirementconfigId from user u, award a, requirement r, awardconfig ac, requirementConfig rc "
						+ " where a.dateCompleted is null and" + " u.id in (" + scoutIdString + ") and u.kind='S' and a.scoutid=u.id and r.awardid=a.id  "
						+ " and a.awardconfigid in (" + awardConfigIdString
						+ ") and a.awardconfigid=ac.id and r.requirementconfigId=rc.id "
		+ " union "
		+ " select u.id, a.awardConfigId, rc.id requirementconfigId from user u, award a, awardconfig ac, requirementConfig rc   " + " where a.dateCompleted is not null and  "
						+ " u.id in (" + scoutIdString + ") and u.kind='S' and a.scoutid=u.id   " + " and a.awardconfigid in (" + awardConfigIdString
						+ ") and a.awardconfigid=ac.id and rc.awardconfigId=ac.id";
		Query query = entityManager.createNativeQuery(sql);
		List results = query.getResultList();
		populateScoutAwardRequirements(results, scoutAwardRequirements);

		// make sure all scouts have all awards, so the table is created
		// correctly
		for (Long userId : scoutIds)
		{
			if (!scoutAwardRequirements.containsKey(userId))
			{
				scoutAwardRequirements.put(userId, new HashMap<Long, Set<Long>>());
			}
			for (Long awardConfigId : awardConfigIds)
			{
				if (!scoutAwardRequirements.get(userId).containsKey(awardConfigId))
				{
					scoutAwardRequirements.get(userId).put(awardConfigId, new HashSet<Long>());
				}
			}
		}
		return scoutAwardRequirements;
	}

	private void populateScoutAwardRequirements(List results, Map<Long, Map<Long, Set<Long>>> scoutAwardRequirements)
	{
		for (Object oRow : results)
		{
			Object[] r = (Object[]) oRow;
			Long userId = ((BigInteger) r[0]).longValue();
			Long awardConfigId = ((BigInteger) r[1]).longValue();
			Long requirementConfigId = ((BigInteger) r[2]).longValue();

			if (!scoutAwardRequirements.containsKey(userId))
			{
				scoutAwardRequirements.put(userId, new TreeMap<Long, Set<Long>>());
			}

			if (!scoutAwardRequirements.get(userId).containsKey(awardConfigId))
			{
				scoutAwardRequirements.get(userId).put(awardConfigId, new HashSet<Long>());
			}
			scoutAwardRequirements.get(userId).get(awardConfigId).add(requirementConfigId);
		}
	}

	/**
	 * get a list of boys with awards where the table column header will be the
	 * scoutnames and the row header is the requirement
	 * 
	 * @param scoutIds
	 * @param awardConfigIds
	 * @return
	 */
	// @Override
	public Map<Long, Map<Long, Set<Long>>> getAwardsScoutsList(List<Long> scoutIds, Set<Long> awardConfigIds)
	{

		String scoutIdString = scoutIds.toString().replace('[', ' ').replace(']', ' ');
		String awardConfigIdString = awardConfigIds.toString().replace('[', ' ').replace(']', ' ');

		String sql = "select a.awardconfigid, u.id, r.requirementconfigId" + " from user u, award a, requirement r, awardconfig ac, requirementConfig rc " + " where u.id in ("
						+ scoutIdString + ") and u.kind='S' and a.scoutid=u.id and r.awardid=a.id " + "	  and a.awardconfigid in (" + awardConfigIdString
						+ ") and a.awardconfigid=ac.id and r.requirementconfigId=rc.id order by u.id, rc.sortOrder";
		Query query = entityManager.createNativeQuery(sql);
		List results = query.getResultList();
		// Map<Long, Map<Long, List<Long>>> scoutsAwardList =new HashMap<Long,
		// Map<Long, List<Long>>>();
		// Set<Long> requirementConfigIds = new TreeSet<Long>();
		// Map<Long, Set<Long>> awardRequirementsMap = new HashMap<Long,
		// Set<Long>>();
		Map<Long, Map<Long, Set<Long>>> awardScoutRequirements = new HashMap<Long, Map<Long, Set<Long>>>();
		if (results.size() == 0)
		{
			// nothing came back, trick it to pretend the boys have this award.
			// This could be better implemented by doing an outer join
			for (Long userId : scoutIds)
			{
				for (Long awardConfigId : awardConfigIds)
				{
					awardScoutRequirements.put(awardConfigId, new HashMap<Long, Set<Long>>());
					awardScoutRequirements.get(awardConfigId).put(userId, new HashSet<Long>());
				}
			}
		}
		else
		{
			populateScoutAwardRequirements(results, awardScoutRequirements);
		}
		return awardScoutRequirements;
	}

	/**
	 * for speed, do a quick check to see if this scouts award is complete
	 * 
	 * @param scoutId
	 * @param awardConfigId
	 * @return
	 */
	@Override
	public boolean isAwardComplete(Long scoutId, Long awardConfigId)
	{
		String sql = "select a.datecompleted from user u, award a where u.id = " + scoutId + " and a.awardconfigid=" + awardConfigId
						+ " and a.scoutid = u.id and a.dateCompleted is not null ";
		Query query = entityManager.createNativeQuery(sql);
		List results = query.getResultList();
		// if size=0 its not complete, if size=1 it is
		return results.size() != 0;
	}
}
