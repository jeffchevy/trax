package org.trax.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.trax.model.User;
import org.trax.service.MailService;
import org.trax.service.TraxService;

@Controller
public class LogoutController extends SimpleUrlLogoutSuccessHandler
{

	@Autowired
    private MailService mailService;
	@Autowired
	private TraxService traxService;
	
	@RequestMapping("/logout.html")
	public String onLogoutSuccess(HttpSession session)

	{
		User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		try
		{
			//need to refresh because sometimes this user can get stale
			traxService.updateLoginDate(loggedInUser.getId());
		}
		catch (Exception e)
		{
			// if it can't save, thats ok, just continue
			e.printStackTrace();
		}
		mailService.sendUpdateNotification();
		session.invalidate();
		
		return "redirect:login.html";
	}
}
