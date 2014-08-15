package org.trax.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.trax.model.CampLogEntry;
import org.trax.model.LeadershipLogEntry;
import org.trax.model.Scout;
import org.trax.model.Scout.ScoutPosition;
import org.trax.model.ServiceLogEntry;
import org.trax.model.User;
import org.trax.service.TraxService;

@Controller
public class AbstractController
{
	protected static final String SCOUT = "scout";

	@Autowired TraxService traxService;
	
	protected static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * make these accessible to the model
	 * @return
	 */
	@ModelAttribute("campEntry")
	public CampLogEntry getCampLogEntry()
	{
		return new CampLogEntry();
	}

	@ModelAttribute("service")
	public ServiceLogEntry getServiceLogEntry()
	{
		return new ServiceLogEntry();
	}

	@ModelAttribute("leadershipEntry")
	public LeadershipLogEntry getLeadershipLogEntry()
	{
		return new LeadershipLogEntry();
	}

	@ModelAttribute("campLog")
	public Set<CampLogEntry> getScoutCampingLog(HttpServletRequest request)
	{
		//this does several things, it refreshes the scout, and makes sure his entries are loaded
		Scout scout = getScout(request);//todo should get for all scouts, if logged in as a leader
		return scout.getCampEntries();
	}

	@ModelAttribute("serviceLog")
	public Set<ServiceLogEntry> getScoutServiceLog(HttpServletRequest request)
	{
		//this does several things, it refreshes the scout, and makes sure his entries are loaded
		Scout scout = getScout(request);//todo should get for all scouts, if logged in as a leader
		return scout.getServiceEntries();
	}

	@ModelAttribute("leadershipLog")
	public Set<LeadershipLogEntry> getScoutLeadershipLog(HttpServletRequest request)
	{
		//this does several things, it refreshes the scout, and makes sure his entries are loaded
		Scout scout = getScout(request);//todo should get for all scouts, if logged in as a leader
		return scout.getLeadershipEntries();
	}

	@ModelAttribute("positions")
	public List<ScoutPosition> getScoutPositions()
	{
		return traxService.getScoutPositions();
	}

	public AbstractController()
	{
		super();
	}

	@InitBinder
	public void allowEmptyDateBinding(WebDataBinder binder)
	{
		// Allow for null values in date fields.
	    binder.registerCustomEditor( Date.class, new CustomDateEditor( dateFormat, true ));
	    // tell spring to set empty values as null instead of empty string.
	    binder.registerCustomEditor( String.class, new StringTrimmerEditor( true ));
	}
	
	protected List<Scout> getScouts(HttpServletRequest request)
	{
		List<Scout> scouts = (List<Scout>)request.getSession().getAttribute("scouts");
		if (scouts == null)
		{
			 scouts = new ArrayList<Scout>();
			 scouts.add((Scout)request.getSession().getAttribute("scout"));
		}
		return scouts;
	}	
	
	protected Scout getScout(HttpServletRequest request)
	{
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Scout scout;
		if (user instanceof Scout)
    	{
    		scout = (Scout)user;
    	}
    	else
    	{
    		scout = (Scout)request.getSession().getAttribute(SCOUT);
    	}
    	try
		{
			scout = traxService.refreshScout(scout);
		}
		catch (Exception e)
		{
			// model.put("errorMessage", "Having trouble changing this record, ask your leader to change it for you."); 
			//@TODO when as scout tries to modify his own record this blows up, for now just ignore it
		}
		return scout;
	}
}