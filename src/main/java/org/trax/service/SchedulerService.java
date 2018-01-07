package org.trax.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trax.dao.UserDao;
import org.trax.model.Leader;
import org.trax.model.Scout;
import org.trax.model.User;

@Service("schedulerService")
//Let Spring manage our transactions
@Transactional
public class SchedulerService
{
    private static final Logger logger = Logger.getLogger(SchedulerService.class);
	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;
	@Autowired
    private MailService mailService;
	
	/*
	 *   *      *      *      *      *  command to be executed
		 |      |      |      |      | 
		 |      |      |      |      | 
		 |      |      |      |      ------ day of week (0 - 6) (0 is Sunday, or use names)
		 |      |      |      ----------- month (1 - 12)
		 |      |      ---------------- day of month (1 - 31)
		 |      --------------------- hour (0 - 23)
		 -------------------------- min (0 - 59)
	 */
	@Transactional(readOnly=true)
	@Scheduled(cron="0 0 6 * * 0")//Sunday 6AM
    public void weeklyReportTask() 
	{
		logger.info("** Preparing weekly reminders **");
		sendNeedsBoardOfReview();
		
		
		/*
		 logger.info("checking for people joining" + new Date(System.currentTimeMillis()));
		 
		List<Scout> scouts = new ArrayList<Scout>();
		for (int i = 11; i <= 18; i++)
		{
			scouts.addAll(userDao.getScoutsWithBirthdaySoon(i*12-1));  //one month before 12-18
		}

		for (Scout scout : scouts)
		{
			logger.info("Found Scout with birthday in a month " + scout.getFullName() +" "+scout.getBirthDate());

			List<Leader> leaders = userDao.getLeaders(scout.getOrganization().getId());
			for (Leader leader : leaders)
			{
				try
				{
					logger.info("Sending Birthday reminder to " + leader.getFullName()+" for "+scout.getFullName()+" with birthday "+scout.getBirthDate());
					mailService.sendPreBirthdayReminder(leader, scout);
				}
				catch (Exception e)
				{
					logger.error("Failed to send birthday reminder for scout("+scout.getFullName()+") at " + new Date(System.currentTimeMillis()));
				}
			}
		}
		*/
	}
	
	
//	@Transactional(readOnly=true)
//	@Scheduled(cron="0 * * ? * *")
//    public void birthdayReminderTask2() 
//	{
//		logger.info("000000000000000000000 checking for upcoming birthdays at midnight");
//		sendDailyReminders();
//	}
	
	/**
	 * put all cron tasks that need to be run at 6 am in this one method
	 */
	@Transactional(readOnly=true)
	@Scheduled(cron="0 0 6 * * ?") //in the morning at 6am ...everynight at midnight "0 0 12 ? * *"
	//@Scheduled(fixedRate = 50000)
    public void sendDailyReminders() 
	{
		logger.info("** Preparing daily reminders **");
		try
		{
			sendPreBirthdayReminder();
			sendBirthdayReminder();
			sendUsersAddedReport();
			//changeUnitOnBirthdayTask();
		}
		catch (Exception e)
		{
			logger.error("failed in on one of the Cron tasks", e);
		}
    }
	
	private void sendNeedsBoardOfReview()
	{
		logger.info("Starting Needs Board of Review report at " + new Date(System.currentTimeMillis()));
		
		Collection<Scout> scouts = userDao.getScoutsNeedingBoardOfReview();
		for (Scout scout : scouts)
		{
			List<Leader> leaders = userDao.getLeadersByUnitId(scout.getUnit().getId());
			for (Leader leader : leaders)
			{
				try
				{
					logger.info("Found Scout needing board of review " + scout.getFullName() +" "+scout.getBirthDate());
	//TODO				mailService.sendNeedingBoardOfReview(leader, scout);
				}
				catch (Exception e)
				{
					logger.error("Failed to send birthday reminder for scout("+scout.getFullName()+") at " + new Date(System.currentTimeMillis()));
				}
			}
		}
	}
	
	private void sendPreBirthdayReminder()
	{
		logger.info("Starting PreBirthday report at " + new Date(System.currentTimeMillis()));

		List<Scout> scouts = new ArrayList<Scout>();
		for (int i = 8; i <= 18; i++)
		{
			scouts.addAll(userDao.getScoutsWithBirthdaySoon(i*12-1));  //one month before 8-18
		}

		for (Scout scout : scouts)
		{
			logger.info("Found Scout with birthday in a month " + scout.getFullName() +" "+scout.getBirthDate());
			String message = "";
			if (scout.getAge()==11)
			{
				boolean hasTroop = false;
				message=scout.getFirstName()+" should be moved to your eleven year old troop on his birthday ";
				//notify eleven year old leaders
				List<Leader> leaders = userDao.getLeaders(scout.getOrganization().getId(), !scout.getUnit().isCub());
				for (Leader leader : leaders)
				{
					if (leader.getUnit().getTypeOfUnit().getStartAge()==11)
					{
						hasTroop=true;
						try
						{
							logger.info("Sending PRE-Birthday reminder to " + leader.getFullName()+" for "+scout.getFullName()+" with birthday "+scout.getBirthDate());
							mailService.sendPreBirthdayReminder(leader, scout, message);
						}
						catch (Exception e)
						{
							logger.error("Failed to send birthday reminder for scout("+scout.getFullName()+") at " + new Date(System.currentTimeMillis()));
						}
					}
				}
				if(hasTroop==false)
				{
					//TODO need to get them to setup their troop
				}
			}
			List<Leader> leaders = userDao.getLeaders(scout.getOrganization().getId(), scout.getUnit().isCub());
			for (Leader leader : leaders)
			{
				try
				{
					
					logger.info("Sending PRE-Birthday reminder to " + leader.getFullName()+" for "+scout.getFullName()+" with birthday "+scout.getBirthDate());
					mailService.sendPreBirthdayReminder(leader, scout, message);
				}
				catch (Exception e)
				{
					logger.error("Failed to send birthday reminder for scout("+scout.getFullName()+") at " + new Date(System.currentTimeMillis()));
				}
			}
		}
	}
	
	// send a reminder on birthday
	private void sendBirthdayReminder()
	{
		Date currentDate = new Date(System.currentTimeMillis());
		logger.info("Starting Birthday report at " + currentDate);

		List<Scout> scouts = new ArrayList<Scout>();
		for (int i = 8; i <= 18; i++)
		{
			scouts.addAll(userDao.getScoutsWithBirthdaySoon(i*12));  // 8-18 year old birthday
		}

		for (Scout scout : scouts)
		{
			logger.info("Found Scout with birthday " + scout.getFullName() +" "+scout.getBirthDate());

			List<Leader> leaders = userDao.getLeaders(scout.getOrganization().getId(), scout.getUnit().isCub());
			for (Leader leader : leaders)
			{
				try
				{
					logger.info("Sending Birthday reminder to " + leader.getFullName()+" for "+scout.getFullName()+" with birthday "+scout.getBirthDate());
					
					//when turning 11, change from webelos to 11 year old scout
					boolean wasMoved = userDao.automaticallyAdvanceToUnit(scout);
					String message = wasMoved ? "He has been moved to your eleven year old troop": "";
					mailService.sendBirthdayReminder(leader, scout, message);
					//when turning 11, change from webelos to 11 year old scout
					userDao.automaticallyAdvanceToUnit(scout);
				}
				catch (Exception e)
				{
					logger.error("Failed to send birthday reminder for scout("+scout.getFullName()+") at " + currentDate);
				}
			}
		}
	}
	
	/**
	 * TODO Some scouts should be changed from eleven year old to troop, troop to team, team to crew, crew to retired 
	 */
    public void changeUnitOnBirthdayTask() 
	{
		try
		{
			logger.info("Change unit on Birthday report at " + new Date(System.currentTimeMillis()));
		}
		catch (Exception e)
		{
			logger.error("failed in birthdayReminderTask", e);
		}
    }
    
	public void sendUsersAddedReport()
	{
		List<User> yesterdayUsers = new ArrayList<User>();
		int days = 1;
		yesterdayUsers.addAll(userDao.getNewUsers(days));

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String subject = "ScoutTrax added " + yesterdayUsers.size()+" on "+cal.getTime()+" and ";
		logger.info(subject);
		Set<String> states = new HashSet<String>();
		Set<String> unitTypes = new HashSet<String>();
		for (User user : yesterdayUsers)
		{
			states.add(user.getCity()+", "+user.getState());
			unitTypes.add(user.getOrganization().getName()+" "+user.getUnit().getTypeOfUnit().getName());
		}

		String message = subject+"\n In "+states+"\n These Organizations"+unitTypes.toString().replace(",", ",\n");
		
		Calendar scoutORama = Calendar.getInstance();
		scoutORama.set(2013, 4, 3);
		Calendar today = Calendar.getInstance();
		//days since scout o rama
		int daysSince = (int)( (today.getTimeInMillis() - scoutORama.getTimeInMillis()) / (1000 * 60 * 60 * 24));
		List<User> monthUsers = new ArrayList<User>();
		//days = 30;
		monthUsers.addAll(userDao.getNewUsers(daysSince));
		subject += "and " + monthUsers.size()+" since May 4, 2013 Scout-O-Rama ("+daysSince+" days) in "+states.size()+" states.";

		for (User user : monthUsers)
		{
			states.add(user.getCity()+", "+user.getState());
			unitTypes.add(user.getOrganization().getName()+" "+user.getUnit().getTypeOfUnit().getName());
		}
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -days);
		message = message+"\n\n\n And over the last "+daysSince+" days since May 4, 2013 Scout-O-Rama "+monthUsers.size() +"\nIn "+states+"\n These Organizations"+unitTypes.toString().replace(",", ",\n");
		logger.info(message);
		mailService.sendEmail("no-reply@ScoutTrax.org", "jeff@scouttrax.org", subject, message, false);
		mailService.sendEmail("no-reply@ScoutTrax.org", "kc.chevalier@gmail.com", subject, message, false);
		mailService.sendEmail("no-reply@ScoutTrax.org", "gregswork@hotmail.com", subject, message, false);
		mailService.sendEmail("no-reply@ScoutTrax.org", "brentjacox@smoothlake.com", subject, message, false);
		mailService.sendEmail("no-reply@ScoutTrax.org", "schmittdarren@comcast.net", subject, message, false);
		mailService.sendEmail("no-reply@ScoutTrax.org", "volcanicchevy@gmail.com", subject, message, false);
	}

}
