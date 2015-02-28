package org.trax.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.trax.dao.BaseUnitTypeDao;
import org.trax.form.OrgUnit;
import org.trax.form.ScoutTransfer;
import org.trax.form.SearchForm;
import org.trax.model.BaseUnitType;
import org.trax.model.Council;
import org.trax.model.Leader;
import org.trax.model.Organization;
import org.trax.model.Scout;
import org.trax.model.Unit;
import org.trax.model.User;
import org.trax.model.cub.CubUnitType;

@Controller
public class OrganizationController extends AbstractScoutController
{
	private static final String ERROR_MESSAGE = "errorMessage";
	@Autowired
	private BaseUnitTypeDao baseUnitTypeDao;
    
    private static String[] states = {"Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","District of Columbia","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania", "Puerto Rico","Rhode Island","South Carolina","South Dakota","Tennessee","Texas","Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming","Europe", "Asia"};
    String[] councils = {"Great Salt Lake","Great Southwest","Snake River","Trapper Trails","Utah National Parks"};
    
    @ModelAttribute("councils")
    public String[] getCouncils()
    {
    	return councils;
    }
    
    @ModelAttribute("states")
    public static String[] getStates()
    {
    	return states;
    }
    
    @ModelAttribute("unitTypes")
    public List<? extends BaseUnitType> getUnitTypes()//Map<String, Object> model)
    {
	   //model.put("unitTypes", traxService.getUnitTypes());
	   return traxService.getUnitTypes();
    }
	@RequestMapping(value = "/organization.html", method = RequestMethod.GET)
	public String showForm(Map<String, Object> model)
	{
		model.put("orgUnit", new OrgUnit());
		return "unit";
	}

	@RequestMapping(value = "/showtransfer.html", method = RequestMethod.GET)
	public String showTransfer(Map<String, Object> model, @RequestParam long scoutId, @RequestParam String fullName, @RequestParam String state, @RequestParam String council)
	{
		model.put("fullName", fullName);
		SearchForm searchForm = new SearchForm();
		searchForm.setScoutId(scoutId);
		searchForm.setCouncilName(council);
		searchForm.setStateName(state);
		
		Scout scout = (Scout)traxService.refreshUser(scoutId);
		searchForm.setTypeOfUnit(scout.getUnit().getTypeOfUnit());
		
		//need to determine if this boy is moving or just becoming a boyScout
		Collection<BaseUnitType> unitTypes = null;
		if ( scout.getUnit().isCub() )
		{
			//don't really know if its to another pack or a troop, give them all the options
			unitTypes = (Collection<BaseUnitType>) traxService.getUnitTypes(false);
			unitTypes.addAll(traxService.getUnitTypes(true));
		}
		else
		{
			unitTypes = (Collection<BaseUnitType>) traxService.getUnitTypes(false);
		}
		
		model.put("unitTypes", unitTypes);
		model.put("searchForm", searchForm);
		
		return "scouttransfer";
	}
	
	/**
	 * When getting ready to transfer the user will enter search criteria. If we find it, show it, othewise
	 * @param model
	 * @param searchForm
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/orgsearch.html", method = RequestMethod.POST)
	public String searchForOrganization(Map<String, Object> model, @Valid SearchForm searchForm, BindingResult result) throws Exception
	{
		ScoutTransfer scoutTransfer = new ScoutTransfer();
		scoutTransfer.setScoutId(searchForm.getScoutId());
		Organization org = traxService.findOrganization(searchForm.getUnitNumber(), searchForm.getCouncilName(), searchForm.getTypeOfUnit()); //todo jeff start here
		Scout scout = (Scout)traxService.getUserById(searchForm.getScoutId());
		Leader leader = null;
		if (org == null)
		{
			org = new Organization();
			BaseUnitType unitType = searchForm.getTypeOfUnit();
			if (unitType instanceof CubUnitType)
			{
				unitType = baseUnitTypeDao.find("Pack");
			}
			else 
			{
				//default to troop, this is our best guess
				unitType = baseUnitTypeDao.find("Troop");
			}
			Unit unit = new Unit(unitType, searchForm.getUnitNumber(), org);
			 
			org.setCouncil(searchForm.getCouncilName());
			org.setState(searchForm.getStateName());
			Set<Unit> units = new HashSet<Unit>();
			units.add(unit);
			org.setUnits(units);

			leader = new Leader();
			leader.setUnit(unit);
			leader.setState(searchForm.getStateName());
			leader.setOrganization(org);

			model.put("searchForm", searchForm);
			model.put("successMessage", "Troop "+searchForm.getUnitNumber()+" was not found.\nEnter the new leader's information.\nThe record for "
					+scout.getFullName()+" will be sent for the leader to view.");
		}
		else
		{
			leader = traxService.getActiveSeniorLeader(org.getId(), searchForm.getUnitNumber());
			if (leader == null)
			{
				throw new Exception("Cannot transfer this scout Leader not found!");
			}
			model.put("successMessage", leader.getUnit().getTypeOfUnit().getName()+" "
					+leader.getUnit().getNumber()+" was found." +
					"\nVerify the information before clicking transfer." +
					"\nThe record for "	+scout.getFullName()+" will be sent for the leader to view.");
		}
		scoutTransfer.setLeader(leader);
		scoutTransfer.setUnitNumber(leader.getUnit().getNumber());
		scoutTransfer.setTypeOfUnit(leader.getUnit().getTypeOfUnit());
		scoutTransfer.setStateName(searchForm.getStateName());
		scoutTransfer.setCouncilName(searchForm.getCouncilName());
		model.put("fullName", scout.getFullName());
		model.put("scoutTransfer", scoutTransfer);
		
		return "scouttransfer";
	}
	
	@RequestMapping(value = "/cubInvite.html", method = RequestMethod.GET)
	public String cubInvite(Map<String, Object> model)
	{
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Leader leader = new Leader();
		leader.setCity(user.getCity());
		leader.setState(user.getState());
		leader.setZip(user.getZip());
		// if the logged in user is a Boy Scout/leader we know they are inviting cubs, and if it is cubscout leader they are inviting boys
		boolean oppositeScoutType = !(user.getUnit().isCub());
		model.put("unitTypes", traxService.getUnitTypes(oppositeScoutType));
		model.put("positions", traxService.getLeaderPositions(oppositeScoutType));
		model.put("leader", leader);
		model.put("isCub", user.getUnit().isCub());
		
		return "invitecub";
	}
	@RequestMapping(value = "/cubInvite.html", method = RequestMethod.POST)
	public String sendCubInvite(Map<String, Object> model, @Valid Leader leader, BindingResult result)
	{
		if (result.hasErrors())
		{
			for (ObjectError error : result.getAllErrors())
			{
				model.put(ERROR_MESSAGE, error.getDefaultMessage());
			}
			return "invitecub";
		}
		String message="Invititation sent, if not received have him/her check her email spam folder";
		try
		{
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			leader.setOrganization(user.getOrganization());
			traxService.saveAndRegisterUser(leader);
		}
		catch (Exception e)
		{
			message = "Error: Failed to send leader invitation message.";
			model.put(ERROR_MESSAGE, message);
			return "invitecub";
		}

		return "redirect:troopManage.html?message="+message;
	}
	
	@RequestMapping(value = "/transferScout.html", method = RequestMethod.GET)
	public String transferScout(HttpServletRequest request, Map<String, Object> model, @Valid ScoutTransfer scoutTransfer)
	{
		String message;
		try
		{
			Unit unit = scoutTransfer.getLeader().getUnit();
			if (unit==null)
			{
				Organization org = scoutTransfer.getLeader().getOrganization();
				if (org==null)
				{
					org = scoutTransfer.getOrg();
					if(org==null)
					{
						Log.error("*** Failed to populate unit with an organization ***");
					}
				}
				unit = new Unit(scoutTransfer.getTypeOfUnit(), scoutTransfer.getUnitNumber(), org);
				scoutTransfer.getLeader().setUnit(unit);
				scoutTransfer.getLeader().setState(scoutTransfer.getStateName());
			}
			
			message = traxService.transferScout(scoutTransfer.getScoutId(), scoutTransfer.getLeader(), scoutTransfer.getCouncilName());
			//take the transferred scout out of memory
			List<Scout> scouts = getScouts(request);
			List<Scout> newScouts = new ArrayList<Scout>();
			
			for (Scout scout : scouts)
			{
				if (scout.getId() != scoutTransfer.getScoutId())
				{
					newScouts.add(scout);
				}
			}
			request.getSession().setAttribute(SCOUTS, newScouts);
		}
		catch (Exception e)
		{
			SearchForm searchForm = new SearchForm();
			searchForm.setScoutId(scoutTransfer.getScoutId());
			model.put("searchForm", searchForm);
			message = "Error: Failed to send transfer message."+e.getLocalizedMessage();
			model.put("successMessage", message);
			return "scouttransfer";
		}

		return "redirect:troopManage.html?message="+message;
	}

	@RequestMapping(value = "/saveorganization.html", method = RequestMethod.POST)
	public String showResult(HttpServletRequest request,
			Map<String, Object> model, @Valid OrgUnit orgUnit, BindingResult result)
			throws Exception
	{
		if (result.hasErrors())
		{
			for (ObjectError error : result.getAllErrors())
			{
				model.put("errorMessage", error.getDefaultMessage());
			}
			return "unit";
		}

		try
		{
			Organization org = traxService.saveOrganization(orgUnit);
			request.getSession().setAttribute("organization", org);
		} 
		catch (Exception e)
		{
			model.put(ERROR_MESSAGE, e.getMessage());
			return "unit";
		}
		return "redirect:user.html";
	}
	
	@RequestMapping(value = "/loadstatecouncils.html", method = RequestMethod.GET)
	public void getStateCouncils(HttpServletResponse response, @RequestParam(value = "state", required = true) String stateName) throws IOException
	{
		PrintWriter writer = response.getWriter();
		List<Council> councils = traxService.getCouncilsByState(stateName);
		if (councils != null) //for now some states are not loaded
		{
			for (Council council : councils)
			{
				writer.print("<option value='" + council.getName() + "'>" + council.getName() + "</option>");
			}
		}
	}
	
	@RequestMapping(value = "/loadUnitTypes.html", method = RequestMethod.GET)
	public void getUnitTypes(HttpServletResponse response, @RequestParam(value = "type", required = true) String type) throws IOException
	{
		PrintWriter writer = response.getWriter();
		List<? extends BaseUnitType> unitTypes = traxService.getUnitTypes("Cub".equals(type));
		if (unitTypes != null)
		{
			for (BaseUnitType unitType : unitTypes)
			{
				
				writer.print("<option value='" + unitType.getId() +"'"+ (unitType.getName().equals("Pack")?" selected=true ":"")+"'>" + unitType.getName() + "</option>");
			}
		}
	}
}
