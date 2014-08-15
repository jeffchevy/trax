package org.trax.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;
import org.trax.model.Award;
import org.trax.model.Course;
import org.trax.model.Leader;
import org.trax.model.Rank;


@Repository("awardDao")
public class AwardJpaDao extends GenericJpaDao<Award, Long> implements AwardDao
{
	private static final Logger logger = Logger.getLogger(AwardJpaDao.class);
	
	
	public void remove(Award award)
	{
		if (award instanceof Rank)
		{
			award.setDateCompleted(null);
			award.setUser(null);
		}
		else
		{
			super.remove(award);
		}
	}
	
	/**
	 * each scout has a different award with the same awardconfig get the individual scouts award
	 * @param scoutId
	 * @param awardconfigId
	 * @return
	 * @throws Exception 
	 */
	public Award getScoutAward(long scoutId, long awardConfigId) throws Exception
	{
		String hql = "Select a from Award a, Scout s Left join s.awards as scoutAward where s.id = "+scoutId+
			" and a.awardConfig.id="+awardConfigId +
			" and scoutAward.id = a.id";
		Query query = entityManager.createQuery(hql);
		Award award = null;
		try
		{
			award = (Award)query.getSingleResult();
		}
		catch (NoResultException e)
		{
			//not an error
			return null;
		}
		catch (EmptyResultDataAccessException e) {
			//not an error
			return null;
		}
		catch(IncorrectResultSizeDataAccessException e)
		{
			//cannot seem to fix this bug, for right now put in this KLUDGE
			// somehow when the user enters requirements to quickly we create extra awards. 
			// I have been unable to fix this, so I decided if I read the users awards and there
			// are duplicates, get rid of the duplicates!
			logger.error("\n*** Multiple matching awards found for a single scout - removing duplicates! AwardConfigId["+awardConfigId+"] for a scout ["+scoutId+"] "+e.getMessage()+"\n\n", e);
			List<Award> awards = query.getResultList();
			award = (Award)awards.get(0);
			for (int i = 1; i < awards.size(); i++)
			{
				//skipping the first one
				Award extraAward = awards.get(i);
				remove(extraAward);
			}
			//this.persist(award);
			
		}
		catch (NonUniqueResultException e)
		{
			//cannot seem to fix this bug, for right now put in this KLUDGE
			// somehow when the user enters requirements to quickly we create extra awards. 
			// I have been unable to fix this, so I decided if I read the users awards and there
			// are duplicates, get rid of the duplicates!
			logger.error("\n*** Multiple matching awards found for a single scout - removing duplicates! AwardConfigId["+awardConfigId+"] for a scout ["+scoutId+"] "+e.getMessage()+"\n\n", e);
			List<Award> awards = query.getResultList();
			award = (Award)awards.get(0);
			for (int i = 1; i < awards.size(); i++)
			{
				//skipping the first one
				Award extraAward = awards.get(i);
				//make sure we don't lose any requirements
				//Set<Requirement> extraRequirements = extraAward.getRequirements();
				//for (Requirement r : extraRequirements)
				//{
				//	Requirement copyRequirement = new Requirement(r.getRequirementConfig(), r.getDateCompleted(), r.getUser());
				//	award.getRequirements().add(copyRequirement);
				//}
				remove(extraAward);
			}
			//this.persist(award);
			
		}
		catch (Exception e)
		{
			logger.error("Error when searching for award Config ["+awardConfigId+"] for a scout ["+scoutId+"] "+e.getMessage(), e);
			throw e;
		}
		return award;
	}
	/**
	 * TODO using the findById from the generic DAO caused multiple RequirementConfig objects returned for the same requirement. I wrote this method as a replacement
	 * until we figure out why it is not working
	 */
	public Award getById(long awardId)
	{
		String hql = "from Award where id = :awardId";
		Award award = (Award)entityManager.createQuery(hql).setParameter("awardId", awardId).getSingleResult();
		
		return award;
	}

	public List<Course> getLeaderCourses(Long userId, long courseConfigId)
	{
		String hql = "Select c from Course c, User u Left join u.awards as course where u.id = "+userId+
		" and c.awardConfig.id="+courseConfigId +
		" and course.id = a.id";
		Query query = entityManager.createQuery(hql);
		List<Course> courses = (List<Course>)query.getResultList();
		return courses;
	}

	public List<Course> getCourses(Leader leader)
	{
//		String hql = "Select c from Course c, User u Left join u.awards as course where u.id = "+leader.getId()+" and course.id=c.id";
		String hql = "Select c from Course c, User u join u.awards as course where u.id = "+leader.getId()+" and course.id=c.id";
		Query query = entityManager.createQuery(hql);
		List<Course> courses = (List<Course>)query.getResultList();
		return courses;
	}
}
