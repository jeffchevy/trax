package org.trax.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.trax.dao.UserDao;
import org.trax.model.Leader;
import org.trax.model.Scout;
import org.trax.model.User;
import org.trax.util.SimpleEncrypter;

@Service("messageService")
public class MailService
{
	private static final String NO_REPLY_SCOUT_TRAX_ORG = "no-reply@ScoutTrax.org";
	private static final Logger logger = Logger.getLogger(MailService.class);
	private JavaMailSenderImpl mailSender;
	@Autowired
	protected TraxService traxService;

	@Value("#{myProperties.mailhost}")
	private String mailhost;

	@Value("#{myProperties.mailpassword}")
	private String password;

	@Value("#{myProperties.mailprotocol}")
	private String protocol;

	@Value("#{myProperties.mailusername}")
	private String username;

	@Value("#{myProperties.mailport}")
	private int port;

	@Autowired
	@Qualifier("velocityEngine")
	private VelocityEngine velocityEngine;
	@Autowired
	private UserDao userDao;

	public MailService()
	{
		mailSender = new JavaMailSenderImpl();
	}

	public void sendNewUser(User user) throws Exception
	{
		Collection<User> users = new ArrayList<User>();
		users.add(user);
		sendNewUsers(users);
	}

	public void sendNewUsers(Collection<User> users) throws Exception
	{
		for (User user : users)
		{
			String passcode = SimpleEncrypter.getPassCode();
			String userIdKey = SimpleEncrypter.encrypt(String.valueOf(user.getId()), passcode);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("user", user);
			data.put("userIdKey", userIdKey);
			data.put("passcode", passcode);
			String text = "";
			String email = NO_REPLY_SCOUT_TRAX_ORG;
			try
			{
				Leader principal = (Leader) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				data.put("leader", principal);
				/* always send from no-reply@scouttrax.org if (principal.getEmail() != null)
				{
					//email = user.getEmail();
				}*/

				if (user instanceof Leader)
				{
					text = VelocityEngineUtils
							.mergeTemplateIntoString(velocityEngine, "mails/newleaderverify.vm", data);
				}
				else
				// instance of scout
				{
					text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mails/newuserverify.vm", data);
				}
			}
			catch (Exception e)
			{
				// There is no Security context -- this is the first
				// registration, use the right template
				logger.info("Sending first leader new email to: "+email+"\nsubject: Welcome to Scout Trax!");
				text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mails/newregistration.vm", data);
			}

			logger.debug("mail text: " + text);

			sendEmail(email, user.getEmail(), "Welcome to Scout Trax!", text, true);
		}
	}

	public void sendEmail(final String fromAddress, final String toAddress, final String subject, final String text,
			final boolean isHtml)
	{
		mailSender.setHost(mailhost);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		mailSender.setProtocol(protocol);
		mailSender.setPort(port);
		try
		{
			MimeMessagePreparator preparator = new MimeMessagePreparator()
			{
				public void prepare(MimeMessage mimeMessage) throws Exception
				{
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
					message.setTo(toAddress);
					message.setSubject(subject);
					message.setFrom(fromAddress);
					message.setText(text, isHtml);
				}
			};
			this.mailSender.send(preparator);
			logger.info("Sending email to: "+fromAddress+"\nsubject: "+subject+"\nbody "+text);
		}
		catch (Exception e)
		{
			logger.error("\n*** FAILED to send email, the Mail server must not be setup correctly.\n"+ toAddress+" "+subject +"\n"+text);
		}
	}

	public void sendEmail(final String fromAddress, Collection<User> toUsers, final String subject, final String text,
			final boolean isHtml) throws AddressException
	{
		mailSender.setHost(mailhost);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		mailSender.setProtocol(protocol);
		mailSender.setPort(port);
		
		//do this in two loops so we know how many valid email addresses we have, to create the Array of InternetAddreses
		List<String> emailList = new ArrayList<String>();
		for (User user : toUsers)
		{
			if (user.getEmail()!=null && !user.getEmail().isEmpty())
			{
				emailList.add(user.getEmail());
			}
		}
		
		final InternetAddress[] recipients = new InternetAddress[emailList.size()];
		int i=0;
		for (String emailAddress : emailList)
		{
			InternetAddress e = new InternetAddress(emailAddress);
			recipients[i++]=e;
		}
		
		try
		{
			MimeMessagePreparator preparator = new MimeMessagePreparator()
			{
				public void prepare(MimeMessage mimeMessage) throws Exception
				{
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
					message.setTo(recipients);
					message.setSubject(subject);
					message.setFrom(fromAddress);
					message.setText(text, isHtml);
				}
			};
			this.mailSender.send(preparator);
		}
		catch (Exception e)
		{
			logger.error("\n*** FAILED to send email, the Mail server must not be setup correctly.\n"+ recipients+" "+subject +"\n"+text);
		}
	}

	
	public void sendUpdateNotification()
	{
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// find records that has been modified in the last hour and send
		// report to boy/parent

		// each record, should contain, awardName, requirementName,
		// boyFullName
		// do the same for each log entry...
		// List<> partial =

		Map<String, Map<String, List<String>>> allUpdates = traxService.getScoutsUpdates(user);

		// got all unique now send one message for each boy
		for (String fullName : allUpdates.keySet())
		{
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("user", user);
			data.put("fullName", fullName);
			Map<String, List<String>> scoutsUpdates = allUpdates.get(fullName);
			if (scoutsUpdates.get("emailList") != null && scoutsUpdates.get("emailList").get(0) != null)
			{
				data.put("passedOffMap", scoutsUpdates);

				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mails/progressnotification.vm", data);
				List<String> emailList = scoutsUpdates.get("emailList");
				// if a user just updated a scouts record, just send
				// notification to the scout

				if (user instanceof Scout)
				{
					// if a scout just updated his own record, send updates to
					// himself and his unit leaders, cuz his parents might be
					// the ones who updated the record!
					// TODO there is a bug where this is sending to lots of different
					// people- don't add these for now
					 emailList.addAll(traxService.getUnitLeaderEmails(user.getOrganization().getCouncil(), user.getUnit().getTypeOfUnit(), user.getUnit().getNumber()));
				}

				for (String toEmail : emailList)
				{
					String subject = "Congratulations:" + fullName + " is making progress!";
					logger.info("Sending email to "+toEmail+"\t"+fullName+"\n"+subject+"\n"+text);
					sendEmail(NO_REPLY_SCOUT_TRAX_ORG, toEmail, subject, text, true);
				}
			}
		}
	}

	public void sendPreBirthdayReminder(Leader leader, Scout scout, String message) throws Exception
	{
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("user", leader);
		data.put("scout", scout);
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mails/prebirthdayreminder.vm", data);
		if (scout.getAge()==11 || scout.getAge()==14 || scout.getAge()==16)
		{
			//TODO it is time to recharter -my need to change the message here!
			
		}
		sendEmail(NO_REPLY_SCOUT_TRAX_ORG, leader.getEmail(), "Reminder:" + scout.getFirstName() + " is having a birthday soon! "+message,
				text +"\n\n"+message, true);
	}

	public void sendBirthdayReminder(Leader leader, Scout scout, String message) throws Exception
	{
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("user", leader);
		data.put("scout", scout);
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mails/birthdayreminder.vm", data);

		sendEmail(NO_REPLY_SCOUT_TRAX_ORG, leader.getEmail(), "Reminder: " + scout.getFirstName()
				+ " is celebrating a birthday today! "+message,
				text +"\n\n"+message, true);
	}

	public void sendPasswordReset(User user) throws Exception
	{
		String passcode = SimpleEncrypter.getPassCode();
		String userIdKey = SimpleEncrypter.encrypt(String.valueOf(user.getId()), passcode);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("user", user);
		data.put("userIdKey", userIdKey);
		data.put("passcode", passcode);
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mails/passwordReset.vm", data);
		logger.warn("mail text: " + text);

		sendEmail(NO_REPLY_SCOUT_TRAX_ORG, user.getEmail(), "Password Reset", text, true);
	}

	
	// Send out invitations to activate accounts
	public void sendInvitations(Collection<Long> userIds)
	{
		try
		{
			Collection<User> users = userDao.getByIds(userIds);
			sendNewUsers(users);
		} 
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
		}
	}
	public void sendFeedback(String priority, String feedback) throws Exception
	{
		Map<String, Object> data = new HashMap<String, Object>();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		data.put("user", user);
		data.put("feedback", feedback);
		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mails/feedback.vm", data);

		sendEmail(user.getEmail(), "jeff@scouttrax.org", "ScoutTrax Feedback - Priority: " + priority, text, true);
	}
	
	public void sendGroup(String subject, String message, Collection<Long> userIds) throws AddressException
	{
		Map<String, Object> data = new HashMap<String, Object>();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Collection<User> recipients = userDao.getByIds(userIds);
		data.put("user", user);

		sendEmail(user.getEmail(), recipients, subject, message, false);
	}

	public void sendTransferRegistration(Leader newLeader, Scout scout) throws Exception
	{
		User oldLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> data = new HashMap<String, Object>();

		String passcode = SimpleEncrypter.getPassCode();
		String userIdKey = SimpleEncrypter.encrypt(String.valueOf(newLeader.getId()), passcode);

		data.put("userIdKey", userIdKey);
		data.put("passcode", passcode);
		data.put("newLeader", newLeader);
		data.put("scout", scout);
		data.put("oldLeader", oldLeader);

		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
				"mails/scouttransferwithregistration.vm", data);

		sendEmail(oldLeader.getEmail(), newLeader.getEmail(), scout.getFullName() + "'s scout record access.", text,
				true);
	}

	public void sendTransfer(Leader newLeader, Scout scout)
	{
		User oldLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("newLeader", newLeader);
		data.put("scout", scout);
		data.put("oldLeader", oldLeader);

		String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "mails/scouttransfer.vm", data);

		sendEmail(oldLeader.getEmail(), newLeader.getEmail(), scout.getFullName()
				+ "'s scout record is now in your unit.", text, true);
	}
}