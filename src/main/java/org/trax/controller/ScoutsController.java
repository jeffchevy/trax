package org.trax.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.trax.form.ScoutForm;
import org.trax.form.UnitForm;
import org.trax.form.UploadItem;
import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.Leader;
import org.trax.model.Rank;
import org.trax.model.RankConfig;
import org.trax.model.Requirement;
import org.trax.model.Scout;
import org.trax.model.User;
import org.trax.model.cub.CubRankConfig;
import org.trax.model.cub.CubRankElectiveConfig;
import org.trax.model.cub.pu2015.ChildAward;
import org.trax.model.cub.pu2015.ChildAwardConfig;
import org.trax.model.cub.pu2015.Cub2015RankConfig;
import org.trax.model.cub.pu2015.Cub2015RankElectiveConfig;

@Controller
public class ScoutsController extends AbstractScoutController
{
	private static final String UNIT = "unit";

	/*
	 * get all boys and leaders
	 */
	@ModelAttribute("users")
	public Collection<User> getUsers()
	{
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return traxService.getUsers(user.getOrganization().getId(), user.getUnit().isCub());
	}
	
	/**
	 * make this accessible to the model
	 * @return
	 */
	@ModelAttribute("uploadItem")
	public UploadItem getUploadItem()
	{
		return new UploadItem();
	}
	
	@RequestMapping("/switchTo2015Cubs.html")
	public String switchTo2015Cubs(HttpSession session, Map<String, Object> model) throws Exception
	{
		Boolean isCub2015 = (Boolean) session.getAttribute(CUB2015);
		if ( isCub2015 == null || !isCub2015 )
		{
			//assume it is classic to start with
			session.setAttribute(CUB2015, new Boolean(true));
		}
		else
		{
			session.setAttribute(CUB2015, new Boolean(false));
		}
		
		return "redirect:advancement.html";
	}
	
	@RequestMapping("/advancement.html")
	public String showAdvancement(HttpSession session, Map<String, Object> model) throws Exception
	{
		String returnType = "advancement";
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//update as soon as they log in TODO this should probably be put in a seperate call. 
		traxService.updateLoginDate(user.getId());
		boolean isCub = user.getUnit().isCub();
		String unitTypeName = isCub?"Pack":"All";
		
		try
		{
			List<Scout> scouts = getScouts(session);
			
			if(scouts.size()==0)
			{
				//no scouts go to manage the troop to add troop members
				returnType = "redirect:troopManage.html";
			}
			else
			if(scouts!=null && scouts.size()>0)
			{
				Scout scout = getScout(session);
				
				Award award = (Award)session.getAttribute(AWARD);
				while(award==null)
				{
					//its not populated yet
					for (Award scoutAward : scout.getAwards())
					{
						if (isCub && scoutAward.getAwardConfig() instanceof CubRankConfig)
						{
							award = scoutAward;
							break;
						}
						else if (!isCub && scoutAward.getAwardConfig() instanceof RankConfig)
						{
							award = scoutAward;
							break;
						}
					}
					
					if (award==null && isCub) {
						//somehow they have a cub with scout ranks, that ok, but add cub ranks
						traxService.addRanks(scout);
						session.setAttribute(AWARDS, scout.getAwards());
					}
				}
				session.setAttribute(AWARD, award);
				if(award!=null)
				{
					//AWARD SHOULD NEVER BE NULL, BUT THERE IS A BUG WHERE CUBS UNIT TYPE WAS CHANGED TO SCOUTS, SO SKIP THIS TO GET IT GOING MAY 15, 2014
					Map<Long, Long> requirementConfigIdCount = traxService.getAggregateCount(award.getAwardConfig().getId(), getScouts(session));
					model.put("requirementConfigIdAndCount", requirementConfigIdCount);
				}
				
				session.setAttribute(AWARDS, scout.getAwards());
				if(scout.getAwards()==null)
				{
					throw new Exception("Award configuration data has not been loaded.");
				}
				
				if(isCub)
				{
					returnType = updateCubAward(session, scout, award, null);
				}
				else //troop, team, crew or ship
				{
					returnType = "advancement";
				}
				
				session.setAttribute("unitTypes", traxService.getUnitTypes());
				session.setAttribute("navigationItem", returnType);
	
				if (user instanceof Leader)
				{
					session.setAttribute(UserController.ORGANIZATION, user.getOrganization());
					//save the unit type so we display the users for this leader
					
					if(user.getOrganization().getUnits().size()>1)
					{
						unitTypeName = user.getUnit().getTypeOfUnit().getName();
					}
					session.setAttribute("unitTypeName", unitTypeName);
					
					if (scouts.size() == 0)
					{
						//no scouts go to manage the troop to add troop members
						return "redirect:troopManage.html";
					}
				}
				else if(user instanceof Scout)
				{
					((Scout) user).setSelected(true);
					scout = traxService.refreshScout((Scout)user);
				}
				
				
				model.put(UNIT, user.getOrganization());
				session.setAttribute(SCOUT, scout);
			}
		}
		catch (Exception e)
		{
			model.put(ERROR_MESSAGE,"Error when trying to show Award: "+e.getMessage());
			e.printStackTrace();
		}
		
 		return returnType;
	}

	/*
	 * If the rank is a classic cub and they just switched to 2015, get the 2015 rank 
	 */
	private String updateCubAward(HttpSession session, Scout scout, Award award, String rankName)
	{
		Object cub2015 = (Boolean)session.getAttribute(CUB2015);
		Boolean isCub2015 = (Boolean) (cub2015!=null ? cub2015 : false);
		

		if(isCub2015)
		{
			//make sure the scout has ranks
			traxService.addRanks(scout);
		}
		
		//if (award.getAwardConfig() instanceof CubRankConfig || award.getAwardConfig() instanceof CubRankElectiveConfig
		//	|| award.getAwardConfig() instanceof Cub2015RankConfig || award.getAwardConfig() instanceof Cub2015RankElectiveConfig)
		{
				//get the rank up to the space, so we can find it for this boy
			if (rankName!=null && rankName.contains(" "))
			{
				//"Tiger Cubs" fouls this up, so strip off everything past the space
				rankName = rankName.substring(0, rankName.indexOf(" "));
			}
			else if (rankName==null && award!=null)
			{
				String awardName = award.getAwardConfig().getName();
				if(awardName.contains(" "))
				{
					if (awardName.contains("Elective"))
					{
						if (isCub2015 )
						{
							//the scout never earns this so just load the award
							session.setAttribute(AWARD, award);
							return "cub2015Advancement";
						}
					}
					else
					{
						//"Tiger Cubs" fouls this up, so strip off everything past the space
						rankName = awardName.substring(0, awardName.indexOf(" "));
					}
				}
				else
				{
					rankName = awardName;
				}
			}
			
			//now get the ward and load it in the session
			Award foundAward = null;
			
			for (Award scoutAward : scout.getAwards())
			{
				if(isCub2015)
				{
					if (scoutAward.getAwardConfig() instanceof Cub2015RankConfig || scoutAward.getAwardConfig() instanceof Cub2015RankElectiveConfig)
					{
						//default to the first one, so we if we don't find it, we at least get one
						foundAward = scoutAward;
						if (award!=null) 
						{
							//there was already a cub award, find a 2015 that matches
							if (scoutAward.getAwardConfig().getName().startsWith(rankName))
							{
								foundAward=scoutAward;
								break;
							}
						}
					}
				}
				else
				{
					if (scoutAward.getAwardConfig() instanceof CubRankConfig)
					{
						//default to the first one, so we if we don't find it, we at least get one
						foundAward = scoutAward;
						if (award!=null) 
						{
							//there was already a cub award, find a 2015 that matches
							if (scoutAward.getAwardConfig().getName().startsWith(rankName))
							{
								foundAward=scoutAward;
								break;
							}
						}
					}
					
				}
			}
			award = foundAward;
			session.setAttribute(AWARD, award);
		}
		
		return isCub2015?"cub2015Advancement":"cubAdvancement"; //default
	}

	@RequestMapping("/changerank.html")
	public String changeRank(HttpSession session, Map<String, Object> model, @RequestParam(value="rankoption", required=true)String rankOption)
	{
		String returnType = null;
		Award award = (Award)session.getAttribute(AWARD);
		boolean foundRank=false;
		
    	List<Scout> scouts = getScouts(session);
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Scout scout = null;
    	if (user instanceof Scout)
    	{
    		scout = (Scout)user;
    	}
    	else
		{
			scout = (Scout)session.getAttribute(SCOUT);
		}

    	scout = traxService.refreshScout(scout);
    	boolean isCub = user.getUnit().isCub();
		if(isCub)
		{
			returnType = updateCubAward(session, scout, award, rankOption);
			foundRank=true;
		}
		else //boyscouts
		{
			returnType = "advancement";
			for (Award scoutAward : scout.getAwards())
			{
				if(scoutAward.getAwardConfig().getName().equals(rankOption))
				{
					// rank name has a matching tab
					session.setAttribute(AWARD, scoutAward);
					foundRank=true;
					break;
				}
			}
		}
    	
		if (!foundRank)
		{
			//need to add the rank to the scout, this can happen in the case of Tiger Cub, if the organization did not include tigers to start with...
			AwardConfig awardConfig = traxService.getAwardConfig(rankOption);
			Rank rank = null;
			if (awardConfig instanceof CubRankConfig || "Tiger Cub".equals(awardConfig.getName()))
			{
				// provide a new blank one
				rank = new Rank();
				rank.setAwardConfig(awardConfig);
				rank.setRequirements(new HashSet<Requirement>());
			}
			else if (awardConfig instanceof RankConfig)
			{
				//this can happen when a cub is transferred to a boy scout.
				rank = new Rank();
				rank.setAwardConfig(awardConfig);
				rank.setRequirements(new HashSet<Requirement>());
			}
			session.setAttribute(AWARD, rank);
		}
		//add the awards here so each time we switch ranks the earned ranks will update 
		session.setAttribute(AWARDS,scout.getAwards());
		
		Map<Long, Long> requirementConfigIdCount = traxService.getAggregateCount(award.getAwardConfig().getId(), scouts);
		model.put("requirementConfigIdAndCount", requirementConfigIdCount);

		return returnType;
	}

	/**
	 * when the username is clicked
	 * @param request
	 * @param model
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	@RequestMapping("/changescout.html")
	public ModelAndView changeScout(HttpSession session, Map<String, Object> model, 
			@RequestParam(value="id", required=true)long scoutId)
	{
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Scout scout = (Scout)traxService.getUserById(scoutId);
		scout.setSelected(true);
		// get the list of scouts(this clears out any that were previously selected)
		String unitTypeName = (String)session.getAttribute("unitTypeName");
		List<Scout> scouts = traxService.getScouts(user.getOrganization().getId(), unitTypeName);
		for (Scout theScout : scouts)
		{
			if (scout.equals(theScout))
			{
				scout.setSelected(true);
			}
		}
		
		session.setAttribute(SCOUTS, scouts);
		session.setAttribute(SCOUT, scout);
		session.setAttribute(AWARDS, scout.getAwards());
		Award award = null;
		try
		{
			Award prevScoutAward = (Award) session.getAttribute(AWARD);
			award = traxService.findOrCreateNewScoutAward(scout.getId(), prevScoutAward.getAwardConfig().getId());
			session.setAttribute(AWARD, award);
		}
		catch (Exception e)
		{
			model.put(ERROR_MESSAGE,"Failed to find or create the award "+e.getMessage());
			e.printStackTrace();
		}
		String navigationItem = (String)session.getAttribute("navigationItem");
		return new ModelAndView(navigationItem, model);
	}

	@RequestMapping("/updateawardinprogress.html")
	public String updateAwardInProgress(HttpSession session, Map<String, Object> model, 
			@RequestParam(value = "ischecked", required = true) boolean isAwardInprogress)
	{
		Award award = (Award)session.getAttribute(AWARD);
		List<Scout> scouts = getScouts(session);
		
		try
		{
			Award a = traxService.updateAwardInprogress(scouts, award.getAwardConfig().getId(), isAwardInprogress);
			session.setAttribute(AWARD, a);
		}
		catch (Exception e)
		{
			model.put(ERROR_MESSAGE, "Failed to change the award to inprogress: "+e.getMessage());
			e.printStackTrace();
		}
		return "redirect:advancement.html";
	}

	@RequestMapping("/updateawardearned.html")
	public String updateAwardEarned(HttpSession session, Map<String, Object> model, 
			@RequestParam(value = "ischecked", required = true) boolean isAwardEarned,
			@RequestParam(value = "awardConfigId", required = false) Long awardConfigId)
	{
		Award award = (Award)session.getAttribute(AWARD);
		List<Scout> scouts = getScouts(session);
		try
		{
			Award a = traxService.updateAwardEarned(scouts,  (awardConfigId != null ? awardConfigId: award.getAwardConfig().getId()), isAwardEarned);
			if ( a.getAwardConfig() instanceof ChildAwardConfig)
			{
				System.out.println("Just updated a childAwardconfig");
			}
			else
			{
				session.setAttribute(AWARD, a);
			}
		}
		catch (Exception e)
		{
			model.put(ERROR_MESSAGE, "Failed to change the award to earned: "+e.getMessage());
			e.printStackTrace();
		}
		return "redirect:advancement.html";
	}
	
	@RequestMapping("/updateaward.html")
	public String updateAward(HttpSession session, Map<String, Object> model, 
			@RequestParam(value = "ischecked", required = true) boolean isChecked, 
			@RequestParam(value = "newdate", required = false) String newDate,
			@RequestParam(value = "awardConfigId", required = false) Long awardConfigId)
	{
		Award award = (Award)session.getAttribute(AWARD);
		List<Scout> scouts = getScouts(session);
		try
		{
			Date awardDate = newDate ==null? new Date() : new Date(newDate);
			Award a = traxService.updateAward(scouts,  
							(awardConfigId != null ? awardConfigId: award.getAwardConfig().getId()), awardDate, isChecked);
			if(a==null)
			{
				//this award was just removed, provide a new blank one
				AwardConfig config = traxService.getAwardConfig(a.getAwardConfig().getId());
				a.setAwardConfig(config);
				a.setRequirements(new HashSet<Requirement>());
			}
			if (a instanceof ChildAward)
			{
				//don't display just the child, display the parent
				
			}
			else
			{
				session.setAttribute(AWARD, a);
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			model.put(ERROR_MESSAGE,"Failed to update the award "+e.getMessage());
			e.printStackTrace();
		}
		return "redirect:advancement.html";
	}
	/*
	 * ajax call when a award Complete checkbox is checked, or unchecked
	 */
	@RequestMapping(value = "/awardpurchased.html", method = RequestMethod.GET)
    public void awardPurchased(HttpSession session,
    		HttpServletResponse response, 
    		@RequestParam(value = "ischecked", required = true) boolean isChecked) 
	throws Exception
    {
		Award award = (Award)session.getAttribute(AWARD);
		traxService.updateAwardPurchased(getScouts(session), award, isChecked);
    }
	/*
	 * ajax call when a award Complete checkbox is checked, or unchecked
	 */
	@RequestMapping(value = "/awardawarded.html", method = RequestMethod.GET)
    public void awardAwarded(HttpSession session,
    		HttpServletResponse response, 
    		@RequestParam(value = "ischecked", required = true) boolean isChecked) 
	throws Exception
    {
		Award award = (Award)session.getAttribute(AWARD);
		traxService.updateAwardAwarded(getScouts(session), award, isChecked);
    }
	/*
	 * ajax call when a requirement checkbox is checked, or unchecked
	 */
	@RequestMapping(value = "/requirementpassedoff.html", method = RequestMethod.GET)
    public void requirementPassedOff(HttpSession session,
    		HttpServletResponse response, 
    		@RequestParam(value = "requirementConfigId", required = true) Long requirementConfigId,
    		@RequestParam(value = "ischecked", required = true) boolean isChecked, 
			@RequestParam(value = "newDate", required = false) String passedOffDateString) //if the date has been reset
	throws Exception
    {
		
		Award award = (Award)session.getAttribute(AWARD);
        PrintWriter writer = response.getWriter();
        List<Scout> scouts = getScouts(session);
		User signOffLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Date dateCompleted = isChecked?new Date():null;
		award = traxService.updateRequirement(requirementConfigId, isChecked, signOffLeader,  award.getAwardConfig(), scouts, passedOffDateString);
		session.setAttribute(AWARD, award);
		
        if (signOffLeader instanceof Scout && award.getRequirements().size()==1)//just added this one, reload this scout in the session,  with new award
		{
			Scout scout = scouts.iterator().next();
			session.setAttribute(AWARDS, scout.getAwards());//reload, read here to lazy load
			session.setAttribute("scout", scout);
		}
        
    	JSONObject object=new JSONObject();
//    	String dateToShow = isChecked==false ? null:(passedOffDateString==null?formatter.format(today):passedOffDateString);
//	    object.put("dateCompleted",dateToShow);
//	    object.put("count", isChecked?checkedScoutNumber:0);//should return from the db
	    object.put("dateCompleted",dateCompleted==null?null:formatter.format(dateCompleted));
	    object.put("count", new Integer(isChecked?checkedScoutNumber:0));//should return from the db

	    object.put("awardComplete", award.getDateCompleted()!=null);
	    writer.print(object);
    }

	@RequestMapping(value = "/manyscouts.html", method = RequestMethod.GET)
	public String manyScouts(Model model, HttpSession session, @ModelAttribute ScoutForm scoutForm)
			throws IOException
	{
		List<Scout> checkedScouts = new ArrayList<Scout>();
		List<Scout> scouts = (List<Scout>) session.getAttribute(SCOUTS);
		if (scoutForm.getScoutIds() != null)
		{
			for (Scout scout : scouts)
			{
				boolean isChecked = false;
				for (Long scoutId : scoutForm.getScoutIds())
				{
					if (scout.getId() == scoutId)
					{
						checkedScouts.add(scout);
						scout.setChecked(true);
						isChecked = true;
						break;
					}
				}
				if (!isChecked)
				{ // scouts were just unchecked, but this one is not -- clear selected scouts
					scout.setChecked(false);
					scout.setSelected(false);
				}
			}
			// load all scouts, but now with the selected flag set or unset
			session.setAttribute(SCOUTS, scouts);
			checkedScoutNumber = checkedScouts.size();
			Award prevScoutAward = (Award) session.getAttribute(AWARD);
			Map<Long, Long> requirementConfigIdCount = traxService.getAggregateCount(prevScoutAward.getAwardConfig().getId(), scouts);
			model.addAttribute("requirementConfigIdAndCount", requirementConfigIdCount);
			
			if (checkedScouts.size() > 0)
			{
				Scout scout2 = traxService.refreshScout(checkedScouts.get(0));
				//change the scout but stay on the same award
				Award award = null;
				try
				{
					award = traxService.findOrCreateNewScoutAward(scout2.getId(), prevScoutAward.getAwardConfig().getId());
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					model.addAttribute(ERROR_MESSAGE,"Failed to find or create the award "+e.getMessage());
					e.printStackTrace();
				}
				session.setAttribute(AWARD, award);
				session.setAttribute(SCOUT, scout2);
				session.setAttribute(AWARDS, scout2.getAwards());
			}
		}
		else
		{
			// none checked, uncheck all
			for (Scout scout : scouts)
			{
				scout.setChecked(false);
				scout.setSelected(false);
			}
		}
		
		
		String navigationItem = (String)session.getAttribute("navigationItem");
		return navigationItem;
	}

	@RequestMapping(value="/showscoutsbyage.html", method = RequestMethod.GET)
	public String showScoutsByUnit(Model model, HttpSession session, @ModelAttribute UnitForm unitForm) throws IOException
	{
		String unitTypeName = unitForm.getUnitTypeName();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//refresh scouts - deselecting all
 		List<Scout> scouts = traxService.getScouts(user.getOrganization().getId(), unitTypeName);
		if(scouts.size()==0)
		{
			boolean isCub = user.getUnit().isCub();
			String msg = isCub? "There are no "+unitTypeName+"'s in this pack choose another unit type or change the unit in pack manage":
				"There are no scouts in the "+unitTypeName+" choose another unit type or assign some in the troop manage.";
			model.addAttribute("errorMessage", msg);
			return isCub?"cubAdvancement":"advancement";
		}
		session.setAttribute(SCOUTS, scouts); 
		session.setAttribute("unitTypeName", unitTypeName);
		//deselect any scout
		Scout scout = (Scout)session.getAttribute(SCOUT);
		if (scout==null)
		{
			scout.setSelected(false);
			session.setAttribute(SCOUT, scout);
		}

		String navigationItem = (String)session.getAttribute("navigationItem");
		return navigationItem;
	}
}
