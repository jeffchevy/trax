package org.trax.controller;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.trax.form.EmailForm;
import org.trax.form.EmailGroupForm;
import org.trax.form.SelectionForm;
import org.trax.model.Leader;
import org.trax.model.Organization;
import org.trax.model.Scout;
import org.trax.model.User;
import org.trax.service.MailService;
import org.trax.service.SchedulerService;
import org.trax.service.TraxService;

@Controller
public class MailController
{
    @Autowired
    private MailService mailService;
    @Autowired
    private TraxService traxService;
    @Autowired
    private SchedulerService schedulerService;
    
    //for testing
    @RequestMapping(value="/sendReport.html", method=RequestMethod.GET)
    public String sendUsersAddedReport()
    {
        //schedulerService.sendDailyReminders();
        //schedulerService.sendUsersAddedReport();
    	mailService.sendUpdateNotification();
    	return "advancement";
    }
    @RequestMapping(value="/email.html", method=RequestMethod.GET)
    public String showEmail(Map<String, Object> model)
    {
//    	Collection<BadgeConfig> badges = traxService.getAllBadges();
        return "email";
    }
    
    @RequestMapping(value="/showfeedback.html", method=RequestMethod.GET)
    public String showFeedback(Map<String, Object> model)
    {
    	EmailForm emailForm = new EmailForm();
		model.put("emailForm", emailForm);
        return "showfeedback";
    }
    
    @RequestMapping(value="/sendfeedback.html")
    public String sendFeedback(@Valid EmailForm emailForm, BindingResult result,  Map<String, Object> model) throws Exception
    {
    	String message = "Feedback has been sent!";
    	mailService.sendFeedback("High", emailForm.getMessage());
    	model.put("successMessage", "Your message has been sent.");
        return "redirect:showfeedback.html?message="+message;
    }
    
    /**
     * this can only be called if a user is logged in
     * @param request
     * @param model
     * @param message
     * @return
     */
    @RequestMapping(value="/createEmail.html", method=RequestMethod.GET)
    public String createEmail(HttpServletRequest request, Map<String, Object> model, @RequestParam(value = "message", required = false) String message)
    {
    	request.getSession().setAttribute("navigationItem", "showemail.html");
    	//request.getSession().removeAttribute("scout");no longer need to remove because we don't display in tiles anyway
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Organization organization = user.getOrganization();
        Collection<Leader> leaders = traxService.getLeaders(organization.getId(), user.getUnit().isCub());
        Collection<Scout> scouts = traxService.getScouts(organization.getId(), "All");
        model.put("leaders", leaders);
        model.put("scouts", scouts);
        model.put("successMessage", message);
		model.put("emailGroupForm", new EmailGroupForm());
		boolean isCub = user.getUnit().isCub();
		String returnType = isCub?"showcubgroupemail":"showgroupemail";
		
        return returnType;
    }
	
    @RequestMapping(value="/sendgroupemail.html")
    public String sendgroupemail(@Valid EmailGroupForm emailGroupForm, BindingResult result,  Map<String, Object> model) throws Exception
    {
    	String message = "Email has been sent!";
    	mailService.sendGroup(emailGroupForm.getSubject(), emailGroupForm.getMessage(), emailGroupForm.getSelections());
    	model.put("successMessage", "Your message has been sent.");
        return "redirect:createEmail.html?message="+message;
    }
    
	@RequestMapping(value = "/invite.html", method = RequestMethod.POST)
	public String invite(HttpServletRequest request, 
								 SelectionForm selection,
								 BindingResult result, Map<String, Object> model)
	{
		String message = "Invitations have been sent!";
		try
		{
			if (selection.getSelections() != null)
			{
				mailService.sendInvitations(selection.getSelections());
			}
		} 
		catch (Exception e)
		{
			message = "Failed to invite "+e.getMessage();
			model.put(org.trax.controller.TroopManageController.ERROR_MESSAGE , message);
			return "showTroop";
		}
		return "redirect:troopManage.html?message="+message;
	}

/*	
	@RequestMapping(value = "/email.html", method = RequestMethod.POST)
	public String email(HttpServletRequest request, 
								 SelectionForm selection,
								 BindingResult result, Map<String, Object> model)
	{
		String message = "Emails have been sent!";
		try
		{
			if (selection.getSelections() != null)
			{
				traxService.sendInvitations(selection.getSelections());
			}
		} 
		catch (Exception e)
		{
			message = "Failed to email "+e.getMessage();
			model.put(ERROR_MESSAGE, message);
			return "showemail";
		}
		return "redirect:troopManage.html?message="+message);
	}
	*/
}

