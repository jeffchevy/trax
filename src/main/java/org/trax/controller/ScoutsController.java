package org.trax.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.trax.dao.ScoutUnitTypeDao;
import org.trax.dao.cub.CubUnitTypeDao;
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
import org.trax.model.cub.CubRank;
import org.trax.model.cub.CubRankConfig;
import org.trax.model.cub.CubRankElectiveConfig;
import org.trax.service.ImportService;

@Controller
public class ScoutsController extends AbstractScoutController
{
	@Autowired
	private ImportService importService;
	@Autowired
	private CubUnitTypeDao cubUnitTypeDao;
	@Autowired
	private ScoutUnitTypeDao unitTypeDao;
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
	
    /*@ModelAttribute("unitTypes")
    public List<? extends BaseUnitType> getUnitTypes(Map<String, Object> model)
    {
    	//@TODO should not have to put it in the model?
	   model.put("unitTypes", traxService.getUnitTypes());
	   return traxService.getUnitTypes();
    }*/
   
	@RequestMapping("/advancement.html")
	public String showAdvancement(HttpServletRequest request, Map<String, Object> model) throws Exception
	{
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try
		{
			//update as soon as they log in TODO this should probably be put in a seperate call. 
			traxService.updateLoginDate(user.getId());
		}
		catch (Exception e)
		{
			// if it can't save, thats ok, just continue
		}
		boolean isCub = user.getUnit().isCub();
		//request.getSession().setAttribute("isCub", isCub);
		request.getSession().setAttribute("unitTypes", traxService.getUnitTypes());
		String returnType = isCub?"cubAdvancement":"advancement";
		request.getSession().setAttribute("navigationItem", returnType);

		Scout scout = (Scout)request.getSession().getAttribute(SCOUT);
		Award award = (Award)request.getSession().getAttribute(AWARD);
		
		if (user instanceof Leader)
		{
			request.getSession().setAttribute(UserController.ORGANIZATION, user.getOrganization());
			//save the unit type so we display the users for this leader
			String unitTypeName = isCub?"Pack":"All";
			if(user.getOrganization().getUnits().size()>1)
			{
				unitTypeName = user.getUnit().getTypeOfUnit().getName();
			}
			Log.info("@@@getting unitTypeName of "+unitTypeName);
			request.getSession().setAttribute("unitTypeName", unitTypeName);
			
			List<Scout> scouts = getScouts(request);
			if (scouts == null || scouts.size()==0)
			{
				//only get them, if they are not there, otherwise we lose the selected scouts
				scouts = traxService.getScouts(user.getOrganization().getId(), unitTypeName);
				if(scouts.size() == 0)
				{
					//could not find by leaders unittypename, try to get all
					unitTypeName = isCub?"Pack":"All";
					scouts = traxService.getScouts(user.getOrganization().getId(), unitTypeName);
					request.getSession().setAttribute("unitTypeName", unitTypeName);
				}
				request.getSession().setAttribute(SCOUTS, scouts);
				
			}
	 		
			if (scouts.size() == 0)
			{
				//no scouts go to manage the troop to add troop members
				return "redirect:troopManage.html";
			}
			
			if(scout==null)
			{
				//set defaults
				for (Scout theScout : scouts)
				{
					if (theScout.isSelected() || theScout.isChecked())
					{
						scout = theScout;
						break;
					}
				}
				if (scout==null)
				{
					//none selected make it the first one
					scout = scouts.iterator().next();
				}
			}
			scout = traxService.refreshScout(scout); //refresh
		}
		else if(user instanceof Scout)
		{
			((Scout) user).setSelected(true);
			scout = traxService.refreshScout((Scout)user);
		}
		
		request.getSession().setAttribute("awards", scout.getAwards());
		if(scout.getAwards()==null)
		{
			throw new Exception("Award configuration data has not been loaded.");
		}
		while(award==null)
		{
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
				request.getSession().setAttribute("awards", scout.getAwards());
			}
		}
		model.put("unit", user.getOrganization());
		request.getSession().setAttribute(AWARD, award);
		if(award!=null)
		{
			//AWARD SHOULD NEVER BE NULL, BUT THERE IS A BUG WHERE CUBS UNIT TYPE WAS CHANGED TO SCOUTS, SO SKIP THIS TO GET IT GOING MAY 15, 2014
			Map<Long, Long> requirementConfigIdCount = traxService.getAggregateCount(award.getAwardConfig().getId(), getScouts(request));
			model.put("requirementConfigIdAndCount", requirementConfigIdCount);
		}
		request.getSession().setAttribute(SCOUT, scout);
		
		return returnType;
	}

	@RequestMapping("/changerank.html")
	public String changeRank(HttpServletRequest request, Map<String, Object> model, @RequestParam(value="rankoption", required=true)String rankOption)
	{
    	List<Scout> scouts = getScouts(request);
    	User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Scout scout = null;
    	if (user instanceof Scout)
    	{
    		scout = (Scout)user;
    	}
    	else
		{
			scout = (Scout)request.getSession().getAttribute(SCOUT);
		}

    	scout = traxService.refreshScout(scout);
    	boolean foundRank=false;
		for (Award award : scout.getAwards())
		{
			if(award.getAwardConfig().getName().equals(rankOption))
			{
				// rank name has a matching tab
				request.getSession().setAttribute(AWARD, award);
				foundRank=true;
				break;
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
			request.getSession().setAttribute(AWARD, rank);
		}
		//add the awards here so each time we switch ranks the earned ranks will update 
		request.getSession().setAttribute("awards",scout.getAwards());
		Award award = (Award)request.getSession().getAttribute(AWARD);
		Map<Long, Long> requirementConfigIdCount = traxService.getAggregateCount(award.getAwardConfig().getId(), scouts);
		model.put("requirementConfigIdAndCount", requirementConfigIdCount);

		boolean isCub = user.getUnit().isCub();
		String returnType = isCub?"cubAdvancement":"advancement";
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
	public ModelAndView changeScout(HttpServletRequest request, Map<String, Object> model, 
			@RequestParam(value="id", required=true)long scoutId)
	{
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Scout scout = (Scout)traxService.getUserById(scoutId);
		scout.setSelected(true);
		// get the list of scouts(this clears out any that were previously selected)
		String unitTypeName = (String)request.getSession().getAttribute("unitTypeName");
		List<Scout> scouts = traxService.getScouts(user.getOrganization().getId(), unitTypeName);
		for (Scout theScout : scouts)
		{
			if (scout.equals(theScout))
			{
				scout.setSelected(true);
			}
		}
		
		request.getSession().setAttribute(SCOUTS, scouts);
		request.getSession().setAttribute(SCOUT, scout);
		request.getSession().setAttribute("awards", scout.getAwards());
		Award award = null;
		try
		{
			Award prevScoutAward = (Award) request.getSession().getAttribute(AWARD);
			award = traxService.findOrCreateNewScoutAward(scout.getId(), prevScoutAward.getAwardConfig().getId());
			request.getSession().setAttribute(AWARD, award);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			model.put(ERROR_MESSAGE,"Failed to find or create the award "+e.getMessage());
			e.printStackTrace();
		}
		String navigationItem = (String)request.getSession().getAttribute("navigationItem");
		return new ModelAndView(navigationItem, model);
	}

	@RequestMapping("/updateawardinprogress.html")
	public String updateAwardInProgress(HttpServletRequest request, Map<String, Object> model, 
			@RequestParam(value = "ischecked", required = true) boolean isAwardInprogress)
	{
		Award award = (Award)request.getSession().getAttribute(AWARD);
		List<Scout> scouts = getScouts(request);
		
		try
		{
			Award a = traxService.updateAwardInprogress(scouts, award.getAwardConfig(), isAwardInprogress);
			request.getSession().setAttribute(AWARD, a);
		}
		catch (Exception e)
		{
			model.put(ERROR_MESSAGE, "Failed to change the award to inprogress: "+e.getMessage());
			e.printStackTrace();
		}
		return "redirect:advancement.html";
	}

	@RequestMapping("/updateawardearned.html")
	public String updateAwardEarned(HttpServletRequest request, Map<String, Object> model, 
			@RequestParam(value = "ischecked", required = true) boolean isAwardEarned)
	{
		Award award = (Award)request.getSession().getAttribute(AWARD);
		List<Scout> scouts = getScouts(request);
		
		try
		{
			Award a = traxService.updateAwardEarned(scouts, award.getAwardConfig(), isAwardEarned);
			request.getSession().setAttribute(AWARD, a);
		}
		catch (Exception e)
		{
			model.put(ERROR_MESSAGE, "Failed to change the award to earned: "+e.getMessage());
			e.printStackTrace();
		}
		return "redirect:advancement.html";
	}
	
	@RequestMapping("/updateaward.html")
	public String updateAward(HttpServletRequest request, Map<String, Object> model, 
			@RequestParam(value = "ischecked", required = true) boolean isChecked, 
			@RequestParam(value = "newdate", required = false) String newDate)
	{
		User signOffLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Award award = (Award)request.getSession().getAttribute(AWARD);
		List<Scout> scouts = getScouts(request);
		try
		{
			Date awardDate = newDate ==null? new Date() : new Date(newDate);
			Award a = traxService.updateAward(signOffLeader, scouts,  award.getAwardConfig(), awardDate, isChecked);
			if(a==null)
			{
				//this award was just removed, provide a new blank one
				AwardConfig config = traxService.getAwardConfig(a.getAwardConfig().getId());
				a.setAwardConfig(config);
				a.setRequirements(new HashSet<Requirement>());
			}
			request.getSession().setAttribute(AWARD, a);
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
	 * ajax call when a requirement checkbox is checked, or unchecked
	 */
	@RequestMapping(value = "/requirementpassedoff.html", method = RequestMethod.GET)
    public void requirementPassedOff(HttpServletRequest request,
    		HttpServletResponse response, 
    		@RequestParam(value = "requirementConfigId", required = true) Long requirementConfigId,
    		@RequestParam(value = "ischecked", required = true) boolean isChecked, 
			@RequestParam(value = "newDate", required = false) String passedOffDateString) //if the date has been reset
	throws Exception
    {
		
		Award award = (Award)request.getSession().getAttribute(AWARD);
        PrintWriter writer = response.getWriter();
        List<Scout> scouts = getScouts(request);
		User signOffLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Date dateCompleted = isChecked?new Date():null;
		award = traxService.updateRequirement(requirementConfigId, isChecked, signOffLeader,  award.getAwardConfig(), scouts, passedOffDateString);
		request.getSession().setAttribute(AWARD, award);
		
        if (signOffLeader instanceof Scout && award.getRequirements().size()==1)//just added this one, reload this scout in the session,  with new award
		{
			Scout scout = scouts.iterator().next();
			request.getSession().setAttribute("awards", scout.getAwards());//reload, read here to lazy load
			request.getSession().setAttribute("scout", scout);
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
	public String manyScouts(Model model, HttpServletRequest request, @ModelAttribute ScoutForm scoutForm)
			throws IOException
	{
		List<Scout> checkedScouts = new ArrayList<Scout>();
		List<Scout> scouts = (List<Scout>) request.getSession().getAttribute(SCOUTS);
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
			request.getSession().setAttribute(SCOUTS, scouts);
			checkedScoutNumber = checkedScouts.size();
			Award prevScoutAward = (Award) request.getSession().getAttribute(AWARD);
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
				request.getSession().setAttribute(AWARD, award);
				request.getSession().setAttribute(SCOUT, scout2);
				request.getSession().setAttribute("awards", scout2.getAwards());
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
		
		
		String navigationItem = (String)request.getSession().getAttribute("navigationItem");
		return navigationItem;
	}

	@RequestMapping(value="/showscoutsbyage.html", method = RequestMethod.GET)
	public String showScoutsByUnit(Model model, HttpServletRequest request, @ModelAttribute UnitForm unitForm) throws IOException
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
		request.getSession().setAttribute(SCOUTS, scouts); 
		request.getSession().setAttribute("unitTypeName", unitTypeName);
		//deselect any scout
		Scout scout = (Scout)request.getSession().getAttribute(SCOUT);
		if (scout==null)
		{
			scout.setSelected(false);
			request.getSession().setAttribute(SCOUT, scout);
		}

		String navigationItem = (String)request.getSession().getAttribute("navigationItem");
		return navigationItem;
	}
}
