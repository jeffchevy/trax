package org.trax.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trax.dao.AwardConfigDao;
import org.trax.dao.AwardDao;
import org.trax.dao.BadgeConfigDao;
import org.trax.dao.BaseUnitTypeDao;
import org.trax.dao.CouncilDao;
import org.trax.dao.CourseConfigDao;
import org.trax.dao.DutyToGodConfigDao;
import org.trax.dao.OrganizationDao;
import org.trax.dao.RankConfigDao;
import org.trax.dao.RequirementConfigDao;
import org.trax.dao.RequirementDao;
import org.trax.dao.RoleDao;
import org.trax.dao.ScoutUnitTypeDao;
import org.trax.dao.UserDao;
import org.trax.dao.cub.ActivityBadgeConfigDao;
import org.trax.dao.cub.BeltLoopConfigDao;
import org.trax.dao.cub.CubRankConfigDao;
import org.trax.dao.cub.CubUnitTypeDao;
import org.trax.dao.cub.PinConfigDao;
import org.trax.dao.cub.pu2015.ChildAwardConfigDao;
import org.trax.dao.cub.pu2015.ChildAwardDao;
import org.trax.dao.cub.pu2015.Cub2015RankConfigDao;
import org.trax.dto.RankTrail;
import org.trax.form.OrgUnit;
import org.trax.form.UserCredentialsForm;
import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.Badge;
import org.trax.model.BadgeConfig;
import org.trax.model.BaseUnitType;
import org.trax.model.CampLogEntry;
import org.trax.model.Council;
import org.trax.model.Course;
import org.trax.model.CourseConfig;
import org.trax.model.DutyToGod;
import org.trax.model.DutyToGodConfig;
import org.trax.model.Leader;
import org.trax.model.Leader.CubLeaderPosition;
import org.trax.model.Leader.LeaderPosition;
import org.trax.model.LeadershipLogEntry;
import org.trax.model.Organization;
import org.trax.model.Rank;
import org.trax.model.RankConfig;
import org.trax.model.Requirement;
import org.trax.model.RequirementConfig;
import org.trax.model.Role;
import org.trax.model.Scout;
import org.trax.model.Scout.CubPosition;
import org.trax.model.Scout.ScoutPosition;
import org.trax.model.ServiceLogEntry;
import org.trax.model.Unit;
import org.trax.model.User;
import org.trax.model.cub.ActivityBadge;
import org.trax.model.cub.ActivityBadgeConfig;
import org.trax.model.cub.BeltLoop;
import org.trax.model.cub.BeltLoopConfig;
import org.trax.model.cub.CubAward;
import org.trax.model.cub.CubAwardConfig;
import org.trax.model.cub.CubDutyToGodConfig;
import org.trax.model.cub.CubRank;
import org.trax.model.cub.CubRankConfig;
import org.trax.model.cub.CubRankElective;
import org.trax.model.cub.CubRankElectiveConfig;
import org.trax.model.cub.CubUnitType;
import org.trax.model.cub.Pin;
import org.trax.model.cub.PinConfig;
import org.trax.model.cub.pu2015.ChildAward;
import org.trax.model.cub.pu2015.ChildAwardConfig;
import org.trax.model.cub.pu2015.Cub2015Rank;
import org.trax.model.cub.pu2015.Cub2015RankConfig;
import org.trax.security.TraxPasswordEncoder;
import org.trax.util.SimpleEncrypter;

@Service("traxService")
// Let Spring manage our transactions
@Transactional
public class TraxServiceImpl implements TraxService
{
	private static final String ROLE_LEADER = "ROLE_LEADER";
	protected static final DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	private static final Logger logger = Logger.getLogger(TraxServiceImpl.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private AwardDao awardDao;

	@Autowired
	private AwardConfigDao awardConfigDao;
	@Autowired
	private BadgeConfigDao badgeConfigDao;
	@Autowired
	private DutyToGodConfigDao dtgConfigDao;
	@Autowired
	private RankConfigDao rankConfigDao;
	@Autowired
	private CubRankConfigDao cubRankConfigDao;
	@Autowired
	private Cub2015RankConfigDao cub2015RankConfigDao;
	@Autowired
	private ChildAwardConfigDao childAwardConfigDao;
	@Autowired
	private ChildAwardDao childAwardDao;
	
	@Autowired
	private BeltLoopConfigDao beltLoopConfigDao;
	@Autowired
	private PinConfigDao pinConfigDao;
	@Autowired
	private ActivityBadgeConfigDao activityBadgeConfigDao;

	@Autowired
	private CourseConfigDao courseConfigDao;
	@Autowired
	private RequirementConfigDao requirementConfigDao;

	@Autowired
	private RequirementDao requirementDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	@Qualifier("councilDao")
	private CouncilDao councilDao;

	@Autowired
	private TraxPasswordEncoder passwordEncoder;
	@Autowired
	private MailService mailService;
	@Autowired
	private CubUnitTypeDao cubUnitTypeDao;
	@Autowired
	private ScoutUnitTypeDao unitTypeDao;
	@Autowired
	private BaseUnitTypeDao baseUnitTypeDao;

	private List<LeaderPosition> leaderPositions;
	private List<CubLeaderPosition> cubLeaderPositions;
	private List<ScoutPosition> scoutPositions;
	private List<CubPosition> cubPositions;

	public TraxServiceImpl()
	{
		leaderPositions = Arrays.asList(LeaderPosition.values());
		cubLeaderPositions = Arrays.asList(CubLeaderPosition.values());
		scoutPositions = Arrays.asList(ScoutPosition.values());
		cubPositions = Arrays.asList(CubPosition.values());
	}

	public User getUserByUsername(String username)
	{
		return userDao.getUserByUsername(username);
	}

	public void saveScout(Scout scout) throws Exception
	{
		addRanks(scout);
		if (scout.getPosition() != null)
		{
			// need to update the leadership log
			LeadershipLogEntry logEntry = createLeadershipLogEntry(scout.getPosition());
			if (scout.getLeadershipEntries() == null)
			{
				scout.setLeadershipEntries(new HashSet<LeadershipLogEntry>());
			}
			scout.getLeadershipEntries().add(logEntry);
		}
		//

		scout.setCreationDate(new Date());

		/*
		 * IF THIS CODE IS ADDED THE USER NO LONGER SAVES??? TODO don't know why
		 * this is not saving, but we need to have the org be part of the unit
		 * for comparisons, so adding it here
		 * 
		 * scout.getUnit().setOrganization(scout.getOrganization()); if
		 * (scout.getUnit().getId() == 0) { //see if it exists try { for (Unit
		 * unit : scout.getOrganization().getUnits()) { if
		 * (unit.getTypeOfUnit().
		 * getName().equals(scout.getUnit().getTypeOfUnit().getName())) {
		 * scout.setUnit(unit); break; } } } catch (Exception e) { // the unit
		 * does not exist for this org, thats ok, the save below will create a
		 * new one e.printStackTrace(); } }
		 */
		userDao.saveUser(scout);
		// return scout;
	}

	/**
	 * add ranks if they do not already have them. Awards are added as needed but each boy starts with empty ranks
	 */
	public void addRanks(Scout scout)
	{
		// give the new scout ranks to fill but only add them if they are not
		// already there
		if (scout.getAwards() == null || scout.getAwards().size() == 0)
		{
			Set<Award> awards = new HashSet<Award>();

			if (scout.getUnit().isCub())
			{
				List<CubRankConfig> rankConfigs = cubRankConfigDao.findAll();
				for (CubRankConfig rankConfig : rankConfigs)
				{
					// only add tiger rank if the organization hasTigers
					if (!rankConfig.getName().startsWith("Tiger") || scout.getOrganization().hasTigers())
					{
						Award rank = new CubRank();
						rank.setAwardConfig(rankConfig);
						awards.add(rank);
					}
				}
				scout.setAwards(awards);
				addCub2015Ranks(scout, awards);
				
			}
			else
			{
				List<RankConfig> rankConfigs = rankConfigDao.findAll();
				for (RankConfig rankConfig : rankConfigs)
				{
					Award rank = new Rank();
					rank.setAwardConfig(rankConfig);
					awards.add(rank);
				}
				scout.setAwards(awards);
			}
		}
		else //they already have awards, but maybe missing the 2015 awards
		if (scout.getUnit().isCub())
		{
			boolean foundCub2015Rank = false;
			for (Award award : scout.getAwards())
			{
				if (award instanceof Cub2015Rank)
				{
					//they already have 2015 ranks, don't add them
					foundCub2015Rank=true;
					break;
				}
			}
			if (! foundCub2015Rank)
			{
				addCub2015Ranks(scout, scout.getAwards());
			}
		}
	}

	private void addCub2015Ranks(Scout scout, Set<Award> awards)
	{
		//now do the same for program update, 2015 -- boys can have both for now
		List<Cub2015RankConfig> rank2015Configs = cub2015RankConfigDao.findAll();
		for (Cub2015RankConfig rankConfig : rank2015Configs)
		{
			// only add tiger rank if the organization hasTigers
			if (!rankConfig.getName().startsWith("Tiger") || scout.getOrganization().hasTigers())
			{
				Award rank = new Cub2015Rank();
				rank.setAwardConfig(rankConfig);
				awards.add(rank);
				
				List<ChildAwardConfig> childAwardConfigs = childAwardConfigDao.getchildAwardConfigs(rankConfig.getId());
				for (ChildAwardConfig childAwardConfig : childAwardConfigs)
				{
					Award childAward = new ChildAward();
					childAward.setAwardConfig(childAwardConfig);
					awards.add(childAward);
				}
			}
		}
	}

	public User saveAndRegisterUser(User user) throws Exception
	{
		User myUser = saveUser(user);
		if (!(myUser instanceof Scout))
		{
			mailService.sendNewUser(myUser);
		}
		return myUser;
	}

	public User saveUser(User user) throws Exception
	{
		Collection<Role> roles = user.getRoles();
		if (user instanceof Leader)
		{
			Role role = new Role(ROLE_LEADER);
			if (roles == null)
			{
				roles = new HashSet<Role>();
			}
			if (!roles.contains(role))
			{
				roles.add(roleDao.save(role));
				user.setRoles(roles);
			}
		}
		if (user.getUnit() == null)
		{
			// not sure which it is, just set it to the first
			user.setUnitCopy(user.getOrganization().getUnits().iterator().next());
		}
		else
		// they entered a unit, make sure it jives with the db
		{
			boolean hasUnit = false;
			for (Unit unit : user.getOrganization().getUnits())
			{
				if (unit.getTypeOfUnit() == user.getUnit().getTypeOfUnit())
				{
					hasUnit = true;
					//@TODO This should provide a default and allow them to override, FIX ME 2/5/2015
					// if the unittypes are the same, use the same unit information, and
					// unit number regardless of what they entered. 
					user.setUnitCopy(unit);
					break;
				}
			}
			if (!hasUnit)
			{
				user.getOrganization().getUnits().add(user.getUnit());
				organizationDao.merge(user.getOrganization());
			}
		}
		if (user.getUnit().getOrganization() == null)
		{
			user.getUnit().setOrganization(user.getOrganization());
		}
		user.setCreationDate(new Date());
		User myUser = userDao.save(user);

		return myUser;
	}

	// preserve transient data when refreshing
	public Organization refreshOrganization(Long organizationId)
	{
		return (Organization) organizationDao.findById(organizationId, false);
	}

	public Organization getUnit(long id)
	{

		return organizationDao.findById(id, false);
	}

	public Organization saveOrganization(OrgUnit orgUnit) throws Exception
	{
		Organization dbOrg = organizationDao.getOrganization(orgUnit.getNumber(), orgUnit.getCouncil(), orgUnit.getTypeOfUnit());
		if (dbOrg != null)
		{
			Leader mainLeader = getActiveSeniorLeader(dbOrg.getId(), orgUnit.getNumber());
			if (mainLeader != null)
			{
				throw new Exception(orgUnit.getTypeOfUnit().getName() + " " + orgUnit.getNumber() + " has already been registered, contact " + mainLeader.getFullName() + " at "
								+ mainLeader.getEmail() + " to be invited to join.");
			}
			else
			{
				// if we cannot get the senior leader, they did not finish
				// registering last time, just ignore it and let them finish
				// registering.
				if (orgUnit.getTypeOfUnit() instanceof CubUnitType)
				{
					dbOrg.setHasTigers(orgUnit.getHasTigers());
				}

				// Set<Unit> units = new HashSet<Unit>();
				Unit unit = new Unit(orgUnit.getTypeOfUnit(), orgUnit.getNumber(), dbOrg);
				if (dbOrg.getUnits().size() > 0)
				{
					dbOrg.getUnits().remove(dbOrg.getUnits().iterator().next());
				}
				dbOrg.getUnits().add(unit);

				organizationDao.merge(dbOrg);
				return dbOrg;
			}

		}
		// The organization was not found, create a new one
		Organization org = new Organization();
		org.setCity(orgUnit.getCity());
		org.setCouncil(orgUnit.getCouncil());
		org.setName(orgUnit.getName());
		org.setDistrict(orgUnit.getDistrict());
		org.setState(orgUnit.getState());
		org.setHasTigers(orgUnit.getHasTigers());

		Set<Unit> units = new HashSet<Unit>();
		Unit unit = new Unit(orgUnit.getTypeOfUnit(), orgUnit.getNumber(), org);
		units.add(unit);
		org.setUnits(units);

		return organizationDao.saveOrg(org);
	}

	public Leader getActiveSeniorLeader(long organizationId, int unitNumber)
	{
		List<Leader> leaders = userDao.getLeaders(organizationId, unitNumber);
		Leader seniorLeader = null;
		if (leaders.isEmpty())
		{
			// transferring a second boy, and the leader has not activated his
			// account yet, go ahead and try again to retrieve the leader
			leaders = userDao.getLeaders(organizationId, false);
		}
		for (Leader leader : leaders)
		{
			/*
			 * This may be a transfer if possible check the boys birthdate, if
			 * he is just turning 11, get the 11 year scout master, and make
			 * sure the leader has logged in lately
			 * 
			 * @TODOif (leader.getLastLoginDate().) {
			 * 
			 * }
			 */
			if (leader.getPosition() == LeaderPosition.ELEVEN_YEAR_OLD_SCOUT_MASTER || leader.getPosition() == LeaderPosition.SCOUT_MASTER
							|| leader.getPosition() == LeaderPosition.VARSITY_COACH || leader.getPosition() == LeaderPosition.VENTURE_ADVISOR)
			{
				seniorLeader = leader;
				break;
			}
		}

		if (seniorLeader == null)
		{
			if (leaders.size() > 0)
			{ // just grab anyone
				seniorLeader = leaders.get(0);
			}
		}
		return seniorLeader;

	}

	public User saveCredentials(UserCredentialsForm credentials) throws Exception
	{
		User user = userDao.findById(credentials.getUserId(), false);
		if (user == null)
		{
			throw new Exception("Failed to find the user with this username " + credentials.getUsername() + " " + credentials.getUserId());
			// credentials.getUsername()
		}
		String encodedPassword = passwordEncoder.encodePassword(credentials.getPassword(), credentials.getUsername());
		user.setPassword(encodedPassword);
		user.setAccountEnabled(true);
		user.setUsername(credentials.getUsername());
		user.getOrganization().setAccountEnabled(true);

		return saveUser(user);
	}

	// private List<String> getLeaderPositionsNames()
	// {
	// List<String> results = new ArrayList<String>();
	// LeaderPosition[] values = LeaderPosition.values();
	// for (LeaderPosition leaderPosition : values)
	// {
	// results.add(leaderPosition.getPositionName());
	// }
	// return results;
	// }
	// private List<String> getScoutPositionsNames()
	// {
	// List<String> results = new ArrayList<String>();
	// ScoutPosition[] values = ScoutPosition.values();
	// for (ScoutPosition scoutPosition : values)
	// {
	// results.add(scoutPosition.getPositionName());
	// }
	// return results;
	// }

	public List getLeaderPositions()
	{
		boolean isCub = false;
		User user = null;
		try
		{
			user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			isCub = user.getUnit().isCub();
		}
		catch (Exception e)
		{
			// TODO Ignore for now, but we need to know the user type
			// default to boy scouts
		}
		return getLeaderPositions(isCub);
	}

	public List getLeaderPositions(boolean isCub)
	{
		if (isCub)
		{
			return cubLeaderPositions;
		}
		return leaderPositions;
	}

	public List getScoutPositions()
	{
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.getUnit().isCub())
		{
			return cubPositions;
		}
		return scoutPositions;
	}

	public List<RankTrail> getTrailToFirstClass(Collection<Scout> scouts)
	{
		List<RankTrail> rankTrail = awardConfigDao.getRankTrailToFirstClass();
		for (Scout scout : scouts)
		{
			if (scout.isChecked() || scout.isSelected())
			{
				for (RankTrail rankTrail2 : rankTrail)
				{
					for (Award award : scout.getAwards())
					{
						for (Requirement requirement : award.getRequirements())
						{
							if (rankTrail2.getRequirementConfigId() == requirement.getRequirementConfig().getId())
							{

							}
						}
					}
					// if()
				}
				// scoutIds.add(scout.getId());
			}
		}
		return null;
	}

	// preserve transient data when refreshing
	public Scout refreshScout(Scout scout)
	{
		boolean isChecked = scout.isChecked();
		boolean isSelected = scout.isSelected();
		scout = getScout(scout.getId());
		scout.setSelected(isSelected);
		scout.setChecked(isChecked);

		return scout;
	}

	public Scout getScout(Long id)
	{
		User user = userDao.findById(id, false);
		return (Scout) user;
	}

	public AwardConfig getAwardConfig(long awardConfigId)
	{
		return awardConfigDao.findById(awardConfigId, false);
	}

	public AwardConfig getAwardConfig(String name)
	{
		return awardConfigDao.getByName(name);
	}

	public PinConfig getPinConfig(String awardName)
	{
		return pinConfigDao.getByName(awardName);
	}

	/**
	 * 
	 * @param awardConfigId
	 * @param scouts
	 * @return map where the key is the requirementConfigId and the value is the
	 *         count of scouts that have earned that requirement
	 */
	public Map<Long, Long> getAggregateCount(long awardConfigId, Collection<Scout> scouts)
	{
		Map<Long, Long> requirementMap = userDao.getAggregateCount(awardConfigId, scouts);

		return requirementMap;
	}

	public List<Scout> getScouts(long organizationId, String unitTypeName)
	{
		BaseUnitType unitType = baseUnitTypeDao.find(unitTypeName);
		return userDao.getScouts(organizationId, unitType);
	}

	public Collection<Leader> getLeaders(long organizationId, boolean isCub)
	{
		return userDao.getLeaders(organizationId, isCub);
	}

	public Collection<User> getUsers(long organizationId, boolean isCub)
	{
		return userDao.getByOrganizationId(organizationId, isCub);
	}

	public Award updateAwardInprogress(List<Scout> scouts, long awardConfigId, boolean setAwardInprogress) throws Exception
	{
		Award thisScoutsAward = null;
		for (Scout scout : scouts)
		{
			scout = refreshScout(scout); // refresh
			if (scout.isChecked() || scout.isSelected())
			{
				thisScoutsAward = awardDao.getScoutAward(scout.getId(), awardConfigId);
				if (thisScoutsAward != null)
				{
					if (!setAwardInprogress)
					{
						// remove it from the scout
						removeRequirements(thisScoutsAward);
						if (!(thisScoutsAward instanceof Rank) && !(thisScoutsAward instanceof CubRank))
						{
							// never remove a rank from a scout
							awardDao.remove(thisScoutsAward);
						}
						thisScoutsAward.setInProgress(false);// set this so when
																// the award is
																// returned it
																// is still
																// displayed but
																// blank
					}
				}
				else if (setAwardInprogress)
				{
					thisScoutsAward = createAward(null, awardConfigId, null);
					thisScoutsAward.setInProgress(setAwardInprogress);
					thisScoutsAward = saveNewScoutAward(scout, thisScoutsAward);
				}
			}
		}
		return thisScoutsAward;
	}

	@Override
	public Award updateAwardEarned(List<Scout> scouts, long awardConfigId, boolean isAwardEarned) throws Exception
	{
		Award thisScoutsAward = null;

		for (Scout scout : scouts)
		{
			scout = refreshScout(scout); // refresh
			if (scout.isChecked() || scout.isSelected())
			{
				thisScoutsAward = awardDao.getScoutAward(scout.getId(), awardConfigId);
				User signOffLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if (thisScoutsAward != null)
				{
					if (isAwardEarned)
					{
						if (thisScoutsAward.getDateCompleted() == null)
						{
							thisScoutsAward.setDateCompleted(new Date());
							thisScoutsAward.setUser(signOffLeader);
						}
					}
					else
					{
						// just remove the earned date
						thisScoutsAward.setDateCompleted(null);
						thisScoutsAward.setInProgress(true);
					}
				}
				else
				{
					thisScoutsAward = createAward(signOffLeader, awardConfigId, new Date());
					thisScoutsAward = saveNewScoutAward(scout, thisScoutsAward);
				}
			}
		}
		return thisScoutsAward;
	}

	@Override
	public void updateAwardPurchased(List<Scout> scouts, Award award, boolean isChecked) throws Exception
	{
		for (Scout scout : scouts)
		{
			scout = refreshScout(scout); // refresh
			if (scout.isChecked() || scout.isSelected())
			{
				award = awardDao.getScoutAward(scout.getId(), award.getAwardConfig().getId());
				award.setDatePurchased(isChecked ? new Date() : null);
			}
		}
	}

	@Override
	public void updateAwardAwarded(List<Scout> scouts, Award award, boolean isChecked) throws Exception
	{
		for (Scout scout : scouts)
		{
			scout = refreshScout(scout); // refresh
			if (scout.isChecked() || scout.isSelected())
			{
				award = awardDao.getScoutAward(scout.getId(), award.getAwardConfig().getId());
				award.setDateAwarded(isChecked ? new Date() : null);
			}
		}
	}

	@Override
	public Award updateAward(List<Scout> scouts, long awardConfigId, Date dateCompleted, boolean completingAward) throws Exception
	{
		Award thisScoutsAward = null;
		User signOffLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


		for (Scout scout : scouts)
		{
			scout = refreshScout(scout); // refresh
			if (scout.isChecked() || scout.isSelected())
			{
				thisScoutsAward = awardDao.getScoutAward(scout.getId(), awardConfigId);
				if (completingAward)
				{
					if (thisScoutsAward != null)
					{
						thisScoutsAward.setDateCompleted(dateCompleted);
						thisScoutsAward.setUser(signOffLeader);
					}
					else
					// create a new one
					{
						thisScoutsAward = createAward(signOffLeader, awardConfigId, dateCompleted);
					}

					// add the new requirement if it is not there.
					/*
					 * for(RequirementConfig rc :
					 * award.getAwardConfig().getRequirementConfigs()) { boolean
					 * hasRequirement = false; boolean isAuthorized =
					 * !(scout.getId()==signOffLeader.getId() &&
					 * rc.getLeaderAuthorized()); if (isAuthorized) { for
					 * (Requirement requirement : award.getRequirements()) { if
					 * (requirement.getRequirementConfig().getId() ==
					 * rc.getId()) { hasRequirement = true; break; } }
					 * if(hasRequirement) { continue; // don't add again }
					 * Requirement r = new Requirement(rc, dateCompleted,
					 * signOffLeader); award.getRequirements().add(r); } }
					 */
					thisScoutsAward = saveNewScoutAward(scout, thisScoutsAward);
				}
				else
				// award is being removed!
				{
					thisScoutsAward = awardDao.getById(thisScoutsAward.getId());// refresh
					removeRequirements(thisScoutsAward);
					scout.getAwards().remove(thisScoutsAward);
					userDao.persist(scout);

					return thisScoutsAward;
				}
			}
		}
		return thisScoutsAward;
	}

	private Award createAward(User signOffLeader, long awardConfigId, Date dateCompleted)
	{
		return createAward(signOffLeader, awardConfigId, dateCompleted, new HashSet<Requirement>());
	}

	@Transactional
	private Award createAward(User signOffLeader, long awardConfigId, Date dateCompleted, Set<Requirement> requirements)
	{
		Award award = null;
		AwardConfig awardConfig = awardConfigDao.findById(awardConfigId, false);
		
		// must start with Cubs
		if (awardConfig instanceof BeltLoopConfig)
		{
			award = new BeltLoop(awardConfig, dateCompleted, null, null, requirements, signOffLeader);
		}
		else if (awardConfig instanceof PinConfig)
		{
			award = new Pin(awardConfig, dateCompleted, null, null, requirements, signOffLeader);
		}
		else if (awardConfig instanceof CubRankElectiveConfig)
		{
			award = new CubRankElective(awardConfig, dateCompleted, null, null, requirements, signOffLeader);
		}
		else if (awardConfig instanceof ActivityBadgeConfig)
		{
			award = new ActivityBadge(awardConfig, dateCompleted, null, null, requirements, signOffLeader);
		}
		else if (awardConfig instanceof CubRankConfig)
		{
			award = new CubRank(awardConfig, dateCompleted, null, null, requirements, signOffLeader);
		}
		else if (awardConfig instanceof CubAwardConfig)
		{
			award = new CubAward(awardConfig, dateCompleted, null, null, requirements, signOffLeader);
		}
		// now scouts
		else if (awardConfig instanceof BadgeConfig)
		{
			award = new Badge(awardConfig, dateCompleted, null, null, requirements, signOffLeader);
		}
		else if (awardConfig instanceof RankConfig)
		{
			award = new Rank(awardConfig, dateCompleted, null, null, requirements, signOffLeader);
		}
		else if (awardConfig instanceof DutyToGodConfig)
		{
			award = new DutyToGod(awardConfig, dateCompleted, null, null, requirements, signOffLeader);
		}
		else if (awardConfig instanceof AwardConfig)
		{
			award = new Award(awardConfig, dateCompleted, null, null, requirements, signOffLeader);
		}
		else
		{
			award = new Course(awardConfig, dateCompleted, null, null, signOffLeader);
		}

		return award;
	}

	public Award updateRequirement(long requirementConfigId, boolean isRequirementChecked, User signOffLeader, AwardConfig awardConfig, List<Scout> scouts,
					String passedOffDateString) throws Exception
	{
		Award thisScoutsAward = null;
		try
		{
			for (Scout scout : scouts)
			{
				if (scout.isChecked() || scout.isSelected())// ||
															// scouts.size()==1)
				{
					if (isRequirementChecked)
					{
						thisScoutsAward = completeOneScoutsRequirement(requirementConfigId, signOffLeader, awardConfig, scout, passedOffDateString);
					}
					else
					{
						thisScoutsAward = removeOneScoutsRequirement(requirementConfigId, signOffLeader, awardConfig, scout);
					}
				}
			}
		}
		catch (Exception e)
		{
			// model.addAttribute(ERROR_MESSAGE,"Failed to update requirement "+e.getMessage());
			e.printStackTrace();
		}
		return thisScoutsAward;
	}

	public Award completeOneScoutsRequirement(long requirementConfigId, User signOffLeader, AwardConfig awardConfig, Scout scout, String dateString) throws Exception
	{
		scout = refreshScout(scout);
		Award thisScoutsAward = (Award) awardDao.getScoutAward(scout.getId(), awardConfig.getId());
		Date dateCompleted = (dateString == null || dateString.trim().length() == 0) ? new Date() : formatter.parse(dateString);

		if (thisScoutsAward != null)
		{
			for (Requirement req : thisScoutsAward.getRequirements())
			{
				if (req.getRequirementConfig().getId() == requirementConfigId && req.getDateCompleted() != null)
				{
					req.setDateCompleted(dateCompleted);
					return thisScoutsAward;
				}
			}
			addRequirementToExistingAward(dateCompleted, requirementConfigId, signOffLeader, thisScoutsAward);
		}
		else if (thisScoutsAward == null)
		{
			// could be cached by hibernate

			for (Award award : scout.getAwards())
			{
				if (award.getAwardConfig().getId() == awardConfig.getId())
				{
					// found it hibernate must have it cached, now add the
					// requirement
					thisScoutsAward = award;
					addRequirementToExistingAward(dateCompleted, requirementConfigId, signOffLeader, award);
					break;
				}
			}

			if (thisScoutsAward == null)
			{
				// didn't find the award add a new one
				RequirementConfig rc = requirementConfigDao.findById(requirementConfigId, false);
				boolean isAuthorized = !(scout.getId() == signOffLeader.getId() && rc.getLeaderAuthorized());
				if (isAuthorized)
				{
					Requirement r = new Requirement(rc, dateCompleted, signOffLeader);
					Set<Requirement> requirements = new HashSet<Requirement>();
					requirements.add(r);
					thisScoutsAward = createAward(signOffLeader, awardConfig.getId(), null, requirements);
					thisScoutsAward.setInProgress(true);
					thisScoutsAward = saveNewScoutAward(scout, thisScoutsAward);
				}
				else
				{
					throw new Exception("As a scout your are not authorized to complete this requirement, it must be done by an adult scout leader");
				}
			}
		}
		return thisScoutsAward;
	}

	public Award removeOneScoutsRequirement(long requirementConfigId, User signOffLeader, AwardConfig awardConfig, Scout scout) throws Exception
	{
		Award thisScoutsAward = awardDao.getScoutAward(scout.getId(), awardConfig.getId());
		if (thisScoutsAward != null)
		{
			for (Requirement req : thisScoutsAward.getRequirements())
			{
				if (req.getRequirementConfig().getId() == requirementConfigId)
				{
					return removeRequirement(thisScoutsAward, signOffLeader, req);
				}
			}
		}
		else
		// if (!hasAward)
		{
			throw new Exception("Failed to find the award[" + awardConfig.getName() + "] awardConfigId[" + awardConfig.getId() + "] scoutId[" + scout.getId() + "]");
		}

		return thisScoutsAward;
	}

	private void addRequirementToExistingAward(Date dateCompleted, Long requirementConfigId, User signOffLeader, Award thisScoutsAward) throws Exception
	{
		// didn't find the requirement add a new one
		RequirementConfig rc = requirementConfigDao.findById(requirementConfigId, false);
		Requirement r = new Requirement(rc, dateCompleted, signOffLeader);
		boolean isAuthorized = signOffLeader instanceof Leader || !rc.getLeaderAuthorized();
		if (isAuthorized)
		{
			Collection<Requirement> requirements = thisScoutsAward.getRequirements();
			requirements.add(r);
			// cast to AwardConfig, Can't be a Training here
			AwardConfig awardConfig = (AwardConfig) thisScoutsAward.getAwardConfig();
			if (awardConfig.getRequirementConfigs().size() == thisScoutsAward.getRequirements().size())
			{
				// all are selected
				thisScoutsAward.setDateCompleted(dateCompleted);
				thisScoutsAward.setUser(signOffLeader);
			}
			thisScoutsAward = awardDao.save(thisScoutsAward);
		}
		else
		{
			throw new Exception("As a scout your are not authorized to complete this requirement, it must be done by an adult scout leader");
		}
	}

	private Award removeRequirement(Award award, User signOffLeader, Requirement req) throws Exception
	{
		boolean isAuthorized = signOffLeader instanceof Leader || !req.getRequirementConfig().getLeaderAuthorized();
		if (isAuthorized)
		{
			award.getRequirements().remove(req);
			// its been deselected, remove it
			requirementDao.remove(req);
			// if it had been previously signed off and marked complete
			// if (award.getRequirements().size()==0 && !(award instanceof
			// Rank))
			// {
			// awardDao.remove(award);
			// return null;
			// }
			// else
			// {
			award.setDateCompleted(null);
			// award.setUser(null);
			awardDao.persist(award);
			award = getAward(award.getId());
			// }
		}
		else
		{
			throw new Exception("As a scout your are not authorized to complete this requirement, it must be done by an adult scout leader");
		}
		return award;
	}

	public User getUserByNameAndOrganization(String firstName, String lastName, Organization org)
	{
		return userDao.getUserByNameAndOrg(firstName, lastName, org.getId());
	}

	public User getUserById(long userId)
	{
		return userDao.findById(userId, false);
	}

	private Award saveNewScoutAward(Scout scout, Award award)
	{
		scout = refreshScout(scout); // refresh
		if (!scout.getAwards().contains(award))
		{
			/*
			 * for (Award myAward : scout.getAwards()) {
			 * System.out.println("awards========="
			 * +myAward.getAwardConfig().getName() + "   "+
			 * award.getAwardConfig().getName()); }
			 */
			scout.getAwards().add(award);
			userDao.persist(scout);
		}
		else
		{
			System.out.println("\n\n\n&&&&&&&&&&&&&&&&& scout already had an award, not saving\n\n\n");
		}
		return award;
	}

	public void updateLeader(Leader user) throws Exception
	{
		Leader dbUser = (Leader) userDao.findById(user.getId(), false);
		dbUser.setFirstName(user.getFirstName());
		dbUser.setLastName(user.getLastName());
		dbUser.setBsaMemberId(user.getBsaMemberId());
		dbUser.setEmail(user.getEmail());
		dbUser.setPosition(user.getPosition());
		dbUser.setCubPosition(user.getCubPosition());
		dbUser.setPhone(user.getPhone());
		dbUser.setWorkPhone(user.getWorkPhone());
		dbUser.setCellPhone(user.getCellPhone());
		dbUser.setAddress(user.getAddress());
		dbUser.setState(user.getState());
		dbUser.setCity(user.getCity());
		dbUser.setZip(user.getZip());
		updateUnit(user, dbUser);
		userDao.persist(dbUser);
	}

	public void updateScout(Scout scout)
	{
		Scout dbUser = (Scout) userDao.findById(scout.getId(), false);
		dbUser.setFirstName(scout.getFirstName());
		dbUser.setMiddleName(scout.getMiddleName());
		dbUser.setLastName(scout.getLastName());
		dbUser.setBsaMemberId(scout.getBsaMemberId());
		dbUser.setEmail(scout.getEmail());
		dbUser.setBirthDate(scout.getBirthDate());
		if (dbUser.getPosition() == null && scout.getPosition() != null)
		{
			// need to update the leadership log
			LeadershipLogEntry logEntry = createLeadershipLogEntry(scout.getPosition());
			dbUser.getLeadershipEntries().add(logEntry);
		}
		dbUser.setPosition(scout.getPosition());
		dbUser.setCubPosition(scout.getCubPosition());
		dbUser.setPhone(scout.getPhone());
		dbUser.setWorkPhone(scout.getWorkPhone());
		dbUser.setCellPhone(scout.getCellPhone());
		dbUser.setAddress(scout.getAddress());
		dbUser.setState(scout.getState());
		dbUser.setCity(scout.getCity());
		dbUser.setZip(scout.getZip());
		updateUnit(scout, dbUser);
		userDao.persist(dbUser);

	}

	/**
	 * if the type of unit is changing, make sure this is a valid unit for this org
	 * @param user
	 * @param dbUser
	 */
	private void updateUnit(User user, User dbUser)
	{
		if (dbUser.getUnit().getTypeOfUnit() != user.getUnit().getTypeOfUnit())
		{
			Set<Unit> units = dbUser.getOrganization().getUnits();
			Unit newUnit = null;
			for (Unit unit : units)
			{
				if (unit.getTypeOfUnit() == user.getUnit().getTypeOfUnit())
				{
					newUnit = unit;
					dbUser.setUnitCopy(newUnit);
					break;
				}
			}
			if (newUnit == null)
			{
				// did not find it, add it
				newUnit = new Unit(user.getUnit().getTypeOfUnit(), user.getUnit().getNumber(), dbUser.getOrganization());
				units.add(newUnit);
				dbUser.getOrganization().setUnits(units);// not sure if this is necessary
				organizationDao.persist(dbUser.getOrganization());
				dbUser.setUnit(newUnit);
			}
		}
	}

	/**
	 * when a user is created or updated with a position
	 * 
	 * @param scout
	 * @return
	 */
	private LeadershipLogEntry createLeadershipLogEntry(ScoutPosition position)
	{
		LeadershipLogEntry logEntry = new LeadershipLogEntry();
		Calendar startCalendar = Calendar.getInstance();
		Date currentDate = startCalendar.getTime();

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.add(Calendar.MONTH, 6); // guess at 6 months, they will have
											// to update later
		Date endDate = endCalendar.getTime();

		User signOffLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		logEntry.setPosition(position);
		logEntry.setStartDate(currentDate);
		logEntry.setEndDate(endDate);

		logEntry.setSignOffLeader(signOffLeader);
		logEntry.setDescription("This end date was defaulted to 6 months, update to the real end date.");
		return logEntry;
	}

	public Collection<BadgeConfig> getAllBadges()
	{
		return (Collection<BadgeConfig>) awardConfigDao.getAllBadges();
	}

	public Collection<DutyToGodConfig> getScoutDtgs()
	{
		Collection<DutyToGodConfig> scoutDtgs = awardConfigDao.getScoutDtgs();
		return (Collection<DutyToGodConfig>) scoutDtgs;
	}

	public Collection<CubAwardConfig> getCubAwards()
	{
		return (Collection<CubAwardConfig>) awardConfigDao.getCubAwards();
	}

	public Collection<CubDutyToGodConfig> getCubDtgs()
	{
		return (Collection<CubDutyToGodConfig>) awardConfigDao.getCubDtgs();
	}

	public Collection<AwardConfig> getAllAwards()
	{
		return (Collection<AwardConfig>) awardConfigDao.getAllAwards();
	}

	public Collection<RankConfig> getAllPalms()
	{
		return (Collection<RankConfig>) awardConfigDao.getAllPalms();
	}

	public List<BeltLoopConfig> getAllBeltLoops()
	{
		return beltLoopConfigDao.findAll();
	}

	public List<PinConfig> getAllPins()
	{
		return pinConfigDao.findAll();
	}

	public List<ActivityBadgeConfig> getAllActivityBadges()
	{
		return activityBadgeConfigDao.findAll();
	}

	public Award getAward(long awardId)
	{
		Award award = awardDao.findById(awardId, false);
		return award;
	}

	public void saveServiceLog(List<Scout> scouts, ServiceLogEntry logEntry)
	{
		for (Scout scout : scouts)
		{
			if (scout.isChecked() || scout.isSelected())
			{
				scout = refreshScout(scout); // refresh
				scout.getServiceEntries().add(logEntry);
				userDao.save(scout);
			}
		}
	}

	public void saveCampLog(List<Scout> scouts, CampLogEntry logEntry)
	{
		for (Scout scout : scouts)
		{
			if (scout.isChecked() || scout.isSelected())
			{
				scout = refreshScout(scout); // refresh
				scout.getCampEntries().add(logEntry);
				userDao.save(scout);
			}
		}
	}

	public void saveLeadershipLog(List<Scout> scouts, LeadershipLogEntry logEntry)
	{
		for (Scout scout : scouts)
		{
			if (scout.isChecked() || scout.isSelected())
			{
				scout = refreshScout(scout); // refresh

				Calendar endCalendar = Calendar.getInstance();
				Date currentDate = endCalendar.getTime();
				if (logEntry.getEndDate() == null || logEntry.getEndDate().compareTo(currentDate) > 0)
				{
					// still in this position, update the scout record
					scout.setPosition(logEntry.getPosition());
				}

				scout.getLeadershipEntries().add(logEntry);
				userDao.save(scout);
			}
		}
	}

	public Award removeRequirements(Award award)
	{
		Set<Requirement> rs = award.getRequirements();
		for (Requirement requirement : rs)
		{
			requirementDao.remove(requirement);
		}
		award.setRequirements(new HashSet<Requirement>());
		award.setDateCompleted(null);
		return award;
	}

	/**
	 * TODO add this to the database, for now just the ones friends will use
	 * http://www.angelfire.com/wy/gilwell/links.html
	 */
	public List<Council> getCouncilsByState(String stateName)
	{
		return councilDao.getByState(stateName);
	}

	public void retireUser(long userId)
	{
		User user = userDao.findById(userId, false);
		user.setRetired(true);
		userDao.save(user);
	}

	public List<User> getUserByEmail(String email)
	{
		return userDao.findByEmail(email);
	}

	public void sendPasswordReset(User realUser)
	{
		try
		{
			mailService.sendPasswordReset(realUser);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
	}

	public User getUserFromPasscode(String userIdKey, String passcode)
	{
		String userIdString = null;

		try
		{
			userIdString = SimpleEncrypter.decrypt(userIdKey, passcode);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
		User user = userDao.findById(Long.valueOf(userIdString).longValue(), false);
		return user;
	}

	public Award findOrCreateNewScoutAward(long scoutId, long awardConfigId) throws Exception
	{
		Award award = awardDao.getScoutAward(scoutId, awardConfigId);

		if (award == null)
		{
			award = createAward(null, awardConfigId, null);
		}
		return award;
	}

	public Map<String, Map<String, List<String>>> getScoutsUpdates(User user)
	{
		return userDao.getScoutsUpdates(user);
	}

	public List<String> getUnitLeaderEmails(String council, BaseUnitType typeOfUnit, Integer number)
	{
		List<User> users = userDao.getByUnit(council, typeOfUnit, number);
		List<String> emails = new ArrayList<String>();
		for (User user : users)
		{
			if (user instanceof Leader)
			{
				String email = user.getEmail();
				if (email != null)
				{
					emails.add(email);
				}
			}
		}
		return emails;
	}

	public Organization findOrganization(Integer unitNumber, String councilName, BaseUnitType baseUnitType)
	{
		return organizationDao.getOrganization(unitNumber, councilName, baseUnitType);
	}

	public User refreshUser(long leaderId)
	{
		return userDao.findById(leaderId, false);
	}

	public String transferScout(long scoutId, Leader newLeader, String councilName) throws Exception
	{
		// send a message, create the new unit and change the scouts unit
		String successMessage = "";
		Scout scout = (Scout) userDao.findById(scoutId, false);
		Leader foundLeader = (Leader) userDao.findById(newLeader.getId(), false);
		Organization organization = null;
		try
		{
			if (foundLeader == null) // they have not yet signed up for ScoutTrax
			{
				// save org
				organization = new Organization();
				Set<Unit> units = new HashSet<Unit>();
				
				// read from db, to make sure all fields are populated
				BaseUnitType typeOfUnit = unitTypeDao.findById(newLeader.getUnit().getTypeOfUnit().getId(), false);
				Unit leaderUnit = new Unit(typeOfUnit, newLeader.getUnit().getNumber(), organization);
				units.add(leaderUnit);
				organization.setUnits(units);
				organization.setCity(newLeader.getCity());
				organization.setState(newLeader.getState());
				organization.setName("");//will be populated if they import
				organization.setCouncil(councilName);
				organization = organizationDao.saveOrg(organization);
				leaderUnit.setOrganization(organization);
				
				// save leader
				newLeader.setUnit(leaderUnit);
				newLeader.setOrganization(organization);
				newLeader = (Leader) saveUser(newLeader);
				// update scout
				scout.setOrganization(organization);
				//Its important that units match, but that they are not the same, because when a scout moves others get moved with him.
				Unit scoutUnit = new Unit(leaderUnit.getTypeOfUnit(),leaderUnit.getNumber(), leaderUnit.getOrganization());
				scout.setUnit(scoutUnit);
				userDao.save(scout);
				// now that everything is saved, send email
				mailService.sendTransferRegistration(newLeader, scout);
			}
			else
			{
				organization = foundLeader.getOrganization();
				// update scout
				scout.setOrganization(organization);
				// the id is missing in the leaders unit -not sure why???
				for (Unit unit : organization.getUnits())
				{
					if (unit.getNumber().equals(foundLeader.getUnit().getNumber()))
					{
						//Its important that units match, but that they are not the same, because when a scout moves others get moved with him.
						Unit scoutUnit = new Unit(unit.getTypeOfUnit(),unit.getNumber(), unit.getOrganization());
						scout.setUnit(scoutUnit);
						break;
					}
				}

				userDao.save(scout);
				// now that everything is saved, send email
				mailService.sendTransfer(foundLeader, scout);
			}

			successMessage = "Successfully transferred " + scout.getFullName() + " to new " + scout.getUnit().getTypeOfUnit().getName() + " " + scout.getUnit().getNumber() + " "
							+ (organization.getName() == null ? "" : organization.getName() + " ") + "in the " + organization.getCity() + ", " + organization.getState();
		}
		catch (Exception e)
		{
			successMessage = "Failed to transfer Scout. " + e.getLocalizedMessage();
		}
		logger.info(successMessage);

		return successMessage;
	}

	public void updateLoginDate(long userId)
	{
		try
		{
			//update as soon as they log in TODO this should probably be put in a seperate call. 
			User loggedInUser = refreshUser(userId);
			loggedInUser.setLastLoginDate(new Date());
			userDao.persist(loggedInUser);
		}
		catch (Exception e)
		{
			// if it can't save, this is not critical, just continue
		}
		
	}

	public void addTraining(long courseId, Collection<Long> userIds, Date courseDate)
	{
		try
		{
			Collection<User> users = userDao.getByIds(userIds);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
	}

	// TODO remove this
	public Course updateCourseEarned(List<Long> userIds, long courseConfigId, boolean isAwardEarned) throws Exception
	{
		Course newCourse = null;

		for (Long userId : userIds)
		{
			User signOffLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			AwardConfig courseConfig = courseConfigDao.findById(courseConfigId, false);
			newCourse = new Course(courseConfig, new Date(), null, null, signOffLeader);
			User leader = userDao.findById(userId, false); // refresh
			newCourse = saveNewLeaderCourse(leader, newCourse);
		}
		return newCourse;
	}

	public void addCourse(String courseName, long userId, Date completionDate)
	{
		CourseConfig courseConfig = awardConfigDao.getCourseByName(courseName);
		User user = userDao.findById(userId, false);
		User signOffLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Course newCourse = new Course(courseConfig, completionDate, null, null, signOffLeader);
		user.getAwards().add(newCourse);
		userDao.persist(user);
	}

	private Course saveNewLeaderCourse(User user, Course course)
	{
		user.getAwards().add(course);
		userDao.persist(user);

		return course;
	}

	public Collection<CourseConfig> getAllCourses()
	{
		return awardConfigDao.getAllCourses();
	}

	public List<Course> getCourses(Leader leader)
	{
		return awardDao.getCourses(leader);
	}

	public List<BadgeConfig> getRequiredMeritBadges()
	{
		return badgeConfigDao.getRequired();
	}

	public List<? extends BaseUnitType> getUnitTypes(boolean isCub)
	{
		List<? extends BaseUnitType> unitTypes;
		if (isCub)
		{
			List<? extends BaseUnitType> someUnitTypes = new ArrayList();
			unitTypes = cubUnitTypeDao.findAll();

			try
			{
				User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if (!user.getOrganization().getHasTigers())
				{
					unitTypes.remove(1); // the first one is Tiger
				}
			}
			catch (Exception e)
			{
				// do nothing, SecurityContextHolder has not been initalized yet
			}
		}
		else
		{
			unitTypes = unitTypeDao.findAll();
		}
		return unitTypes;
	}

	public List<? extends BaseUnitType> getUnitTypes()
	{
		boolean isCub = false;
		try
		{
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return getUnitTypes(user.getUnit().isCub());
		}
		catch (Exception e)
		{
			// TODO Ignore for now, but we need to know the user type
			// default to boy scouts
		}
		return getUnitTypes(isCub);
	}

	@Override
	public Map<Long, Map<Long, Set<Long>>> getToFirstClass(List<Scout> scouts) throws Exception
	{
		Set<Long> awardList = new HashSet<Long>();
		awardList.add(awardConfigDao.getByName("Scout").getId());// scout
		awardList.add(awardConfigDao.getByName("Tenderfoot").getId());// tf
		awardList.add(awardConfigDao.getByName("Second Class").getId());// 2c
		awardList.add(awardConfigDao.getByName("First Class").getId());// 1c

		List<Long> scoutIds = getSelectedScoutIds(scouts);
		if (scoutIds.isEmpty())
		{
			return null;
		}
		return userDao.getScoutsAwardList(scoutIds, awardList);
	}

	@Override
	public Map<Long, Map<Long, Set<Long>>> getToBear(List<Scout> scouts)
	{
		
		Set<Long> awardList = new LinkedHashSet<Long>();
		
		awardList.add(awardConfigDao.getByName("Bobcat").getId());
		if (scouts.get(0).getOrganization().hasTigers())
		{
			awardList.add(awardConfigDao.getByName("Tiger Cub").getId());
		}
		awardList.add(awardConfigDao.getByName("Wolf").getId());
		awardList.add(awardConfigDao.getByName("Bear").getId());

		List<Long> scoutIds = getSelectedScoutIds(scouts);
		if (scoutIds.isEmpty())
		{
			return null;
		}
		return userDao.getScoutsAwardList(scoutIds, awardList);
	}

	@Override
	public Map<Long, Map<Long, Set<Long>>> getScoutsAwardList(List<Scout> scouts, Set<Long> awardList) throws Exception
	{
		List<Long> scoutIds = getSelectedScoutIds(scouts);
		if (scoutIds.isEmpty())
		{
			return null;
		}
		return userDao.getScoutsAwardList(scoutIds, awardList);
	}

	/**
	 * order this by awards first, the table headers will be the boys names, the
	 * row headers will be the award and requirement text
	 */
	// @Override
	public Map<Long, Map<Long, Set<Long>>> getAwardScoutsList(List<Scout> scouts, Set<Long> awardList)
	{
		List<Long> scoutIds = getSelectedScoutIds(scouts);
		if (scoutIds.isEmpty())
		{
			return null;
		}
		return userDao.getScoutsAwardList(scoutIds, awardList);
	}

	private List<Long> getSelectedScoutIds(List<Scout> scouts)
	{
		List<Long> scoutIds = new ArrayList<Long>();
		for (Scout scout : scouts)
		{
			if (scout.isChecked() || scout.isSelected())
			{
				scoutIds.add(scout.getId());
			}
		}
		return scoutIds;
	}

	@Override
	public boolean isAwardComplete(Long scoutId, Long awardConfigId)
	{
		return userDao.isAwardComplete(scoutId, awardConfigId);
	}

	@Override
	public String getRankMeritBadges(String rankName, List<Scout> scouts)
	{
		String htmlString = "";
		List<String> requiredAwardNames = new ArrayList<String>();

		Scout theScout = null;
		for (Scout scout : scouts)
		{
			scout = refreshScout(scout);
			if (scout.isSelected() || scout.isChecked())
			{
				if (theScout == null)
				{
					theScout = scout;
				}
				else
				{
					// more than one selected don't do anything
					theScout = null;
				}
			}
		}
		if (theScout != null)
		{

			if (rankName.equals("Star"))
			{
				requiredAwardNames = awardConfigDao.getRankMeritBadges(theScout.getId(), 0, 4, 0, 2);
				if (!requiredAwardNames.isEmpty())
				{
					htmlString = "3. Earn 6 merit badges; including 4 from the required list for Eagle.\n" + "<hr><p style='background: #dd6;'>You have earned "
									+ requiredAwardNames.size() + " of 6 needed for your Star Rank: \n" + requiredAwardNames + "</p>";
				}
			}
			else if (rankName.equals("Life"))
			{
				requiredAwardNames = awardConfigDao.getRankMeritBadges(theScout.getId(), 4, 3, 2, 2);
				if (!requiredAwardNames.isEmpty())
				{
					htmlString = "3. Earn five more merit badges (so that you have 11 in all), including any three more"
									+ " from the required list for Eagle. (See the Eagle Rank Requirements," + " number 3, for this list.)"
									+ " \nA Scout may choose any of the 17 required merit badges in the 13 categories to fulfill this requirement.\n"
									+ "<hr><p style='background: #dd6;'>You have earned " + requiredAwardNames.size() + " of 5 needed for your Life Rank: \n" + requiredAwardNames
									+ "</p>";
				}
			}
			else if (rankName.equals("Eagle"))
			{
				requiredAwardNames = awardConfigDao.getRankMeritBadges(theScout.getId(), 0, 13, 0, 0);
				if (!requiredAwardNames.isEmpty())
				{
					htmlString = "3. Earn a total of 21 merit badges; including the following: <br>" + "     a. First Aid<br>" + "     b. Citizenship in the Community<br>"
									+ "     c. Citizenship in the Nation<br>" + "     d. Citizenship in the World<br>" + "     e. Communications<br>"
									+ "     f. Personal Fitness<br>" + "     g. Emergency Preparedness OR Lifesaving<br>" + "     h. Environmental Science OR Sustainability<br>"
									+ "     i. Personal Management<br>" + "     j. Swimming OR Hiking OR Cycling<br>" + "     k. Camping<br>" + "     l. Family Life<br>"
									+ "     m. Cooking (2014)<br>";

					for (String earnedAwardName : requiredAwardNames)
					{
						if (htmlString.contains(earnedAwardName))
						{
							htmlString = htmlString.replace(earnedAwardName, "<b>" + earnedAwardName + "</b>");
						}
					}
					if (!requiredAwardNames.isEmpty())
					{
						htmlString += "<hr><p style='background: #dd6;'>You have earned " + requiredAwardNames.size()
										+ " of 13 Required Meritbadges for your Eagle Rank. (Yours will appear in bold font.)</p>";
					}
				}

				List<String> electiveNames = new ArrayList<String>();
				electiveNames = awardConfigDao.getRankMeritBadges(theScout.getId(), 0, 0, 0, 8);
				if (!electiveNames.isEmpty())
				{
					if (!electiveNames.isEmpty())
					{
						htmlString += "<hr><p style='background: #dd6;'>You have earned " + electiveNames.size() + " of 8 elective Meritbadges needed for your Eagle Rank: \n"
										+ electiveNames + "</p>";
					}
				}
			}
		}

		return htmlString;
	}

}
