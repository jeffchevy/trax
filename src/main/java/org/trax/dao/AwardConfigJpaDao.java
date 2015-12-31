package org.trax.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.jfree.util.Log;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.trax.dto.RankTrail;
import org.trax.model.AwardConfig;
import org.trax.model.BadgeConfig;
import org.trax.model.CourseConfig;
import org.trax.model.DutyToGodConfig;
import org.trax.model.Leader;
import org.trax.model.RankConfig;
import org.trax.model.Scout;
import org.trax.model.User;
import org.trax.model.cub.CubAwardConfig;
import org.trax.model.cub.CubDutyToGodConfig;


@Repository("awardConfigDao")
public class AwardConfigJpaDao extends GenericJpaDao<AwardConfig, Long> implements AwardConfigDao
{
	public AwardConfig getByName(String name)
	{
		String hql = "from AwardConfig ac where ac.name = :name";
		List<AwardConfig> acs = entityManager.createQuery(hql).setParameter("name", name).getResultList();
		
		return acs.size() > 0 ? acs.get(0) : null;
	}
	public CourseConfig getCourseByName(String name)
	{
		String hql = "from CourseConfig ac where ac.name = :name";
		CourseConfig courseConfig = (CourseConfig)entityManager.createQuery(hql).setParameter("name", name).getSingleResult();
		
		return courseConfig;
	}
	public AwardConfig getByDescription(String description)
	{
		String hql = "from AwardConfig ac where ac.description = :description";
		List<AwardConfig> acs = entityManager.createQuery(hql).setParameter("description", description).getResultList();
		
		return acs.size() > 0 ? acs.get(0) : null;
	}
	
	public Collection<BadgeConfig> getAllBadges()
	{
		String hql = "from BadgeConfig order by name";
		return entityManager.createQuery(hql).getResultList();
	}
	
	public Collection<DutyToGodConfig> getScoutDtgs()
	{
		String sql = "from AwardConfig ac where kind='D' or name = 'Faith in God' order by name";
		Collection<DutyToGodConfig> acs = entityManager.createQuery(sql).getResultList();
		//Collection<DutyToGodConfig> resultList = entityManager.createNativeQuery(sql).getResultList();
		return acs;
	}
	public Collection<CubDutyToGodConfig> getCubDtgs()
	{
		String hql = "from CubDutyToGodConfig order by name";
		return entityManager.createQuery(hql).getResultList();
	}
	public Collection<CubAwardConfig> getCubAwards()
	{
		String hql = "from CubAwardConfig order by name";
		return entityManager.createQuery(hql).getResultList();
	}
	public Collection<AwardConfig> getAllAwards()
	{
		String hql = "from AwardConfig where kind='A' order by name";
		Collection<AwardConfig> acs = entityManager.createQuery(hql).getResultList();
		return acs;
	}
	
	public Collection<CourseConfig> getAllCourses()
	{
		String hql = "from CourseConfig where kind='T' order by sortOrder";
		Collection<CourseConfig> acs = entityManager.createQuery(hql).getResultList();
		return acs;
	}

	public Collection<RankConfig> getAllPalms()
	{
		String hql = "from RankConfig where name like '%Palm%' order by sortOrder";
		return entityManager.createQuery(hql).getResultList();
	}
	@Override
	public List<RankTrail> getRankTrailToFirstClass() {
			List<RankTrail> rankTrail = new LinkedList();

			String sql = "select rc.id, ac.name, rc.text " +
					"from requirementconfig rc, awardconfig ac " +
					"where ac.id=rc.awardconfigid and ac.name in ('Scout', 'Tenderfoot', 'Second Class', 'First Class') order by ac.sortOrder, rc.sortOrder";
			Query query = entityManager.createNativeQuery(sql);
			List results = query.getResultList();
			for (Object oRow : results)
			{
				Object[] r = (Object[]) oRow;
				long requirementConfigId = ((BigInteger) r[0]).longValue();
				rankTrail.add(new RankTrail(requirementConfigId, (String)r[1], (String)r[2]));
			}

		return rankTrail;
	}
	
	/**
	 * get merit badges required for ranks
	 *  sorted by required first, and ordered by earliest completed
	 * @param userId
	 * @param skipRequireds - the number to start at
	 * @param maxRequireds - the number of requireds to return
	 * 
	 * @param skipNonRequired - the number of nonrequireds to skip
	 * @param maxNonRequireds - the number of nonrequireds to return
	 * @return
	 * so for a star, the parameters are (userid, 0, 4, 0, 2)
	 *    for a life,  (userid, 4, 3, 2, 3)
	 *    for an eagle, (userid, 7, 5, 9) 
	 */
	@Override
	public List<String> getRankMeritBadges(Long userId, int skipRequireds, int maxRequireds, int skipNonRequireds, int maxNonRequireds ) {

		List<String> awardNames = new ArrayList<String>();
		try
		{
			String sql = "(select ac.name from  user u, awardconfig ac, award a where ac.kind ='B'"
					+" and ac.id=a.awardconfigid and a.scoutid=u.id and u.id="+userId+" and ac.required=true and dateCompleted is not null " +
							"order by datecompleted limit "+skipRequireds+", "+maxRequireds+" )"
					+" union"
					+" (select ac.name from  user u, awardconfig ac, award a where ac.kind ='B'"
					+" and ac.id=a.awardconfigid and a.scoutid=u.id and u.id="+userId+" and ac.required=false and dateCompleted is not null " +
					"		order by datecompleted limit "+skipNonRequireds+", "+maxNonRequireds+" ) ";
			Query query = entityManager.createNativeQuery(sql);
			awardNames = query.getResultList();
		}
		catch (Exception e)
		{
			//log an error but keep going this is not critical
			Log.error("Failed to get Merit badges for a rank ", e);
		}

		return awardNames;
	}
}
