package org.trax.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.jasperreports.engine.JRException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.trax.model.CampLogEntry;
import org.trax.model.LeadershipLogEntry;
import org.trax.model.Scout;
import org.trax.model.ServiceLogEntry;
import org.trax.model.User;
import org.trax.service.LogService;

@Controller
public class LogController extends AbstractController
{
	@Autowired
	private LogService logService;

	@RequestMapping("/camplog.html")
	public String camplog(HttpServletRequest request, Map<String, Object> model)
	{
		//navigationItem used in scoutscontroller to determine which tab to show
		request.getSession().setAttribute("navigationItem", "redirect:camplog.html"); 
	
		return "showCampLog";
	}	
	
	@RequestMapping("/servicelog.html")
	public String serviceLog(HttpServletRequest request, 
			Map<String, Object> model)
	{
		//navigationItem used in scoutscontroller to determine which tab to show
		request.getSession().setAttribute("navigationItem", "redirect:servicelog.html");
    	return "showServiceLog";
	}
	
	@RequestMapping(value = "/leadershiplog.html", method = RequestMethod.GET)
	public String leadershipLog(HttpServletRequest request, 
			Map<String, Object> model)
	{
		//navigationItem used in scoutscontroller to determine which tab to show
		request.getSession().setAttribute("navigationItem", "redirect:leadershiplog.html");
    	return "showLeadershipLog";
	}
	
	@RequestMapping("/saveleadershiplog.html")
	public String saveLeadershipLog(HttpServletRequest request, 
			Map<String, Object> model, 
			@Valid LeadershipLogEntry logEntry, 
			BindingResult result)
	{
		String returnVal = "showLeadershipLog";
		if (result.hasErrors())
		{
			model.put("leadershipEntry", logEntry);//make sure the old one is loaded
			for (ObjectError error : result.getAllErrors())
			{
				model.put("errorMessage", error.getDefaultMessage());
			}
			return returnVal;
		}
		
		try
		{
			User signOffLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			logEntry.setSignOffLeader(signOffLeader);
	    	traxService.saveLeadershipLog(getScouts(request), logEntry);
		} 
		catch (Exception e)
		{
			model.put("errorMessage","Failed to save leadership log entry "+e.getMessage());
		}
		
    	return returnVal;
	}	
	@RequestMapping("/savecamplog.html")
	public String saveCampLog(HttpServletRequest request, 
			Map<String, Object> model,
			@Valid CampLogEntry logEntry, 
			BindingResult result)
	{
		String returnVal = "showCampLog";
		if (result.hasErrors())
		{
			model.put("campEntry", logEntry);//make sure the old one is loaded
			for (ObjectError error : result.getAllErrors())
			{
				model.put("errorMessage", error.getDefaultMessage());
				break;
			}
			return returnVal;
		}
		
		try
		{
			User signOffLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			logEntry.setSignOffLeader(signOffLeader);
	    	traxService.saveCampLog(getScouts(request), logEntry);
		} 
		catch (Exception e)
		{
			model.put("errorMessage","Failed to save camp log entry "+e.getMessage());
		}
		Scout scout = getScout(request);//todo should get for all scouts, if logged in as a leader
		model.put("campLog", scout.getCampEntries());
    	return returnVal;
	}	
	
	@RequestMapping("/saveservicelog.html")
	public String saveServiceLog(HttpServletRequest request, 
			Map<String, Object> model,  
			@Valid ServiceLogEntry logEntry,
			BindingResult result) throws Exception
	{
		String returnVal = "showServiceLog";
		try
		{
			if (result.hasErrors())
			{
				model.put("service", logEntry);//make sure the old one is loaded
				for (ObjectError error : result.getAllErrors())
				{
					model.put("errorMessage", error.getDefaultMessage());
				}
				return returnVal;
			}
			User signOffLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		        
			logEntry.setSignOffLeader(signOffLeader);
	    	traxService.saveServiceLog(getScouts(request), logEntry);
		} 
		catch (Exception e)
		{
			model.put("errorMessage","Failed to save service log entry "+e.getMessage());
			return returnVal;
		}
		return returnVal;
	}
	
	// removes
	@RequestMapping("/removeCampEntry.html")
	public String removeCampEntry(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "logEntryId", required = true) long logEntryId) throws IOException, JRException
	{
		logService.removeCampEntry(logEntryId);
		return "showCampLog";
	}
	@RequestMapping("/removeLeadershipEntry.html")
	public String removeLeadershipEntry(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "logEntryId", required = true) long logEntryId) throws IOException, JRException
	{
		logService.removeLeadershipEntry(logEntryId);
		return "showLeadershipLog";
	}
	@RequestMapping("/removeServiceEntry.html")
	public String removeServiceEntry(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "logEntryId", required = true) long logEntryId) throws IOException, JRException
	{
		logService.removeServiceEntry(logEntryId);
		return "showServiceLog";
	}
	
	//loads
	@RequestMapping("/loadCampEntry.html")
	public String loadCampEntry(HttpServletRequest request,  
			Map<String, Object> model,
			@RequestParam(value = "logEntryId", required = true) long logEntryId) throws IOException, JRException
	{
		String returnVal = "campLogEntry";
		CampLogEntry campLogEntry = logService.getCampEntry(logEntryId);
		model.put("logEntry", campLogEntry);
		//model.put("scout", getScout(request));

		return returnVal;
	}
	@RequestMapping("/loadLeadershipEntry.html")
	public String loadLeadershipEntry(HttpServletRequest request,  
			Map<String, Object> model,
			@RequestParam(value = "logEntryId", required = true) long logEntryId) throws IOException, JRException
	{
		String returnVal = "leadershipLogEntry";
		LeadershipLogEntry leadershipLogEntry = logService.getLeadershipEntry(logEntryId);
		model.put("logEntry", leadershipLogEntry);
		//Scout scout = getScout(request);
		//model.put("scout", scout);
		
		return returnVal;
	}
	@RequestMapping("/loadServiceEntry.html")
	public String loadServiceEntry(HttpServletRequest request,  
			Map<String, Object> model,
			@RequestParam(value = "logEntryId", required = true) long logEntryId) throws IOException, JRException
	{
		String returnVal = "serviceLogEntry";
		ServiceLogEntry serviceLogEntry = logService.getServiceEntry(logEntryId);
		model.put("logEntry", serviceLogEntry);
		//Scout scout = getScout(request);
		//model.put("scout", scout);

		return returnVal;
	}
	
	//updates
	@RequestMapping("/updateCampEntry.html")
	public String updateCampEntry(HttpServletRequest request, 
			Map<String, Object> model,
			@Valid CampLogEntry logEntry, 
			BindingResult result) throws IOException, JRException
	{
		String returnVal = "showCampLog";
		//make sure it is signed by this user
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logEntry.setSignOffLeader(user);
		CampLogEntry campLogEntry = logService.updateCampEntry(logEntry);
		Scout scout = getScout(request);
		model.put("campLog", scout.getCampEntries());
		model.put("logEntry", campLogEntry);
		return returnVal;
	}
	@RequestMapping("/updateLeadershipEntry.html")
	public String updateLeadershipEntry(HttpServletRequest request, 
			Map<String, Object> model,
			@Valid LeadershipLogEntry logEntry, 
			BindingResult result) throws IOException, JRException
	{
		String returnVal = "showLeadershipLog";
		//make sure it is signed by this user
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logEntry.setSignOffLeader(user);
		LeadershipLogEntry leadershipLogEntry = logService.updateLeadershipEntry(logEntry);
		
		return returnVal;
	}
	@RequestMapping("/updateServiceEntry.html")
	public String updateServiceEntry(HttpServletRequest request,  
			Map<String, Object> model,
			@Valid ServiceLogEntry logEntry,
			BindingResult result) throws IOException, JRException
	{
		String returnVal = "showServiceLog";
		//make sure it is signed by this user
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logEntry.setSignOffLeader(user);
		ServiceLogEntry serviceLogEntry = logService.updateServiceEntry(logEntry);
		
		
		return returnVal;
	}
	
	@RequestMapping("/saveCampEntry.html")
	public String saveCampEntry(HttpServletRequest request,  
			Map<String, Object> model,
			@Valid CampLogEntry logEntry,
			BindingResult result) throws IOException, JRException
	{
		String returnVal = "campLogEntry";
		CampLogEntry campLogEntry = logService.getCampEntry(logEntry.getId());
		
		return returnVal;
	}
}
