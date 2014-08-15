package org.trax.controller;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.Badge;
import org.trax.model.BadgeConfig;
import org.trax.model.DutyToGod;
import org.trax.model.DutyToGodConfig;
import org.trax.model.RankConfig;
import org.trax.model.Requirement;
import org.trax.model.RequirementConfig;
import org.trax.model.Scout;
import org.trax.model.cub.ActivityBadgeConfig;
import org.trax.model.cub.BeltLoopConfig;
import org.trax.model.cub.CubAwardConfig;
import org.trax.model.cub.CubDutyToGodConfig;
import org.trax.model.cub.PinConfig;
import org.trax.service.TraxService;

@Controller
public class BadgeController
{
	protected static final Format formatter = new SimpleDateFormat("MM/dd/yyyy");
    @Autowired
    private TraxService traxService;

    @RequestMapping(value="/badge.html", method=RequestMethod.GET)
    public String showBadges(Map<String, Object> model)
    {
    	Collection<BadgeConfig> badges = traxService.getAllBadges();
        model.put("badgeConfigs", badges);
        return "showBadges";
    }
    
    @RequestMapping(value="/dtg.html", method=RequestMethod.GET)
    public String showDTG(Map<String, Object> model)
    {
    	Collection<DutyToGodConfig> dtgs = traxService.getScoutDtgs();
        model.put("badgeConfigs", dtgs);
        return "showDtgs";
    }
    		
    @RequestMapping(value="/award.html", method=RequestMethod.GET)
    public String showAwards(Map<String, Object> model)
    {
    	Collection<AwardConfig> awards = traxService.getAllAwards();
        model.put("badgeConfigs", awards);
        return "showBadges";
    }
    
    @RequestMapping(value="/palms.html", method=RequestMethod.GET)
    public String showPalms(Map<String, Object> model)
    {
    	Collection<RankConfig> palms = traxService.getAllPalms();
        model.put("badgeConfigs", palms);
        return "showBadges";
    }
    
    @RequestMapping(value="/cubBadges.html", method=RequestMethod.GET)
    public String showCubBadges(Map<String, Object> model, @RequestParam(value="type", required=true)String type)
    {
    	if("Belt Loops".equals(type))
    	{
    		List<BeltLoopConfig> awardConfigs = traxService.getAllBeltLoops();
    		model.put("badgeConfigs", awardConfigs);
            return "showCubBeltLoops";
    	}
    	if("Activity Badges".equals(type))
    	{
    		List<ActivityBadgeConfig> awardConfigs = traxService.getAllActivityBadges();
    		model.put("badgeConfigs", awardConfigs);
            return "showCubPins";
    	}
    	else if("Pins".equals(type))
    	{
    		List<PinConfig> awardConfigs = traxService.getAllPins();
    		model.put("badgeConfigs", awardConfigs);
            return "showCubPins";    		
    	}
    	else if("Faith".equals(type))
    	{
    		Collection<CubDutyToGodConfig> cubDtgs = traxService.getCubDtgs();
        	model.put("badgeConfigs", cubDtgs);
	        return "showCubPins";
    	}
    	else if("CubAwards".equals(type))
    	{
    		Collection<CubAwardConfig> awardConfigs = traxService.getCubAwards();
    		model.put("badgeConfigs", awardConfigs);
            return "showCubPins";    		
    	}
    	Collection<RankConfig> palms = traxService.getAllPalms();
        model.put("badgeConfigs", palms);
        return "showBadges";
    }
    
	@RequestMapping("/selectBadge.html")
	public String selectBadge(HttpServletRequest request, Map<String, Object> model, @RequestParam(value="badgeConfigId", required=true) long badgeConfigId)
	{
		Scout scout = (Scout)request.getSession().getAttribute("scout");
		scout = traxService.refreshScout(scout);
		Award foundAward = null;
		for (Award award : scout.getAwards())
		{
			if(award.getAwardConfig().getId() == badgeConfigId)
			{
				foundAward = award;
				break;
			}
		}
		if (foundAward == null)
		{
			foundAward = new Badge();//TODO should not be creating a new model object here!!
			AwardConfig config = traxService.getAwardConfig(badgeConfigId);
			if (config == null)
			{
				config = traxService.getAwardConfig(1); //start at the beginning
			}
			foundAward.setAwardConfig(config);
			foundAward.setRequirements(new HashSet<Requirement>());
		}
		request.getSession().setAttribute("award", foundAward);
		String returnValue = "advancement";
		if ( scout.getUnit().isCub())
		{
			returnValue = "cubAdvancement";
		}
		return returnValue;
	}
    
		/**
	 * Select a pin based on the name of the belt loop
	 * @param request
	 * @param model
	 * @param awardName
	 * @return
	 */
	@RequestMapping("/selectPin.html")
	public String selectBadge(HttpServletRequest request, Map<String, Object> model, 
			@RequestParam(value="awardName", required=true) String awardName)
	{
		Scout scout = (Scout)request.getSession().getAttribute("scout");
		scout = traxService.refreshScout(scout);
		Award foundAward = null;
		for (Award award : scout.getAwards())
		{
			//see if the scout has already started to work on this award
			if((awardName!=null && award.getAwardConfig().getName().equals(awardName) && award.getAwardConfig() instanceof PinConfig))//in the case of moving from a loop to a pin, we only have the name
			//@TODO (awardName!=null && award.getAwardConfig().getName().equals(awardName) && award.getAwardConfig() instanceof PinConfig))//in the case of moving from a loop to a pin, we only have the name
			{
				foundAward = award;
				break;
			}
		}
		if (foundAward == null)
		{
			PinConfig config = traxService.getPinConfig(awardName);
			foundAward = new Award();//TODO should not be creating a new model object here!!
			foundAward.setAwardConfig(config);
			foundAward.setRequirements(new HashSet<Requirement>());
		}
		request.getSession().setAttribute("award", foundAward);
		return "cubAdvancement";
	}

	/**
	 * Select a electives based on the name of the cub award
	 * @param request
	 * @param model
	 * @param awardName
	 * @return
	 */
	@RequestMapping("/selectElective.html")
	public String selectElective(HttpServletRequest request, Map<String, Object> model, 
			@RequestParam(value="awardName", required=true) String awardName)
	{
		Scout scout = (Scout)request.getSession().getAttribute("scout");
		scout = traxService.refreshScout(scout);
		Award foundAward = null;
		for (Award award : scout.getAwards())
		{
			//see if the scout has already started to work on this award
			if(awardName!=null && award.getAwardConfig().getName().equals(awardName))//in the case of moving from a loop to a pin, we only have the name
			{
				foundAward = award;
				if("Wolf Electives".equals(awardName) || "Bear Electives".equals(awardName)) //do this so they get loaded in earned 
				{
					request.getSession().setAttribute("awards", scout.getAwards());
				}
				break;
			}
		}
		if (foundAward == null)
		{
			AwardConfig config = traxService.getAwardConfig(awardName);
			foundAward = new Award();//TODO should not be creating a new model object here!!
			foundAward.setAwardConfig(config);
			foundAward.setRequirements(new HashSet<Requirement>());
		}
		request.getSession().setAttribute("award", foundAward);
		return "cubAdvancement";
	}

	//@TODO combine this with select badges above
	@RequestMapping("/selectDtg.html")
	public String selectDtg(HttpServletRequest request, Map<String, Object> model, @RequestParam(value="badgeConfigId", required=true) long dtgConfigId)
	{
		Scout scout = (Scout)request.getSession().getAttribute("scout");
		scout = traxService.refreshScout(scout);
		Award foundAward = null;
		for (Award award : scout.getAwards())
		{
			if(award.getAwardConfig().getId() == dtgConfigId)
			{
				foundAward = award;
				break;
			}
		}
		if (foundAward == null)
		{
			foundAward = new DutyToGod();//TODO should not be creating a new model object here!!
			AwardConfig config = traxService.getAwardConfig(dtgConfigId);
			if (config == null)
			{
				config = traxService.getAwardConfig(1); //start at the beginning
			}
			foundAward.setAwardConfig(config);
			foundAward.setRequirements(new HashSet<Requirement>());
		}
		request.getSession().setAttribute("award", foundAward);
		
		return "advancement";
	}
	
	@RequestMapping("/prevAward.html")
	public String previousAward(HttpServletRequest request,  Map<String, Object> model, @RequestParam(value="awardConfigId", required=true)long awardConfigId)
	{
		long previousAwardConfigId = awardConfigId==0?0:awardConfigId-1;
		return "redirect:selectBadge.html?badgeConfigId="+previousAwardConfigId;
	}
	
	@RequestMapping("/nextAward.html")
	public String nextAward(HttpServletRequest request, Map<String, Object> model, @RequestParam(value="awardConfigId", required=true)long awardConfigId)
	{
		return "redirect:selectBadge.html?badgeConfigId="+ ++awardConfigId;
	}
	

}

