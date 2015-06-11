package org.trax.controller;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.trax.model.Scout;
import org.trax.model.User;
import org.trax.service.TraxService;

public class AbstractScoutController
{
	protected static final String ERROR_MESSAGE = "errorMessage";
	protected static final String SCOUTS = "scouts";
	protected static final String AWARD = "award";
	protected static final String SCOUT = "scout";
	protected static final String CUB2015 = "Cub2015";
	protected static final Format formatter = new SimpleDateFormat("MM/dd/yyyy");
	protected static final String AWARDS = "awards";
	protected int checkedScoutNumber = 0;
	@Autowired
	protected TraxService traxService;

	public AbstractScoutController()
	{
		super();
	}

	protected List<Scout> getScouts(HttpSession session)
	{
		User signOffLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		boolean isCub = signOffLeader.getUnit().isCub();
		String unitTypeName = isCub?"Pack":"All";
		
		List<Scout> scouts = null;
	    if (signOffLeader instanceof Scout)
		{
			scouts = new ArrayList<Scout>();
			Scout scout = (Scout)session.getAttribute("scout"); 
			scouts.add(scout);
		}
	    else
	    {
	    	scouts = (List<Scout>)session.getAttribute(SCOUTS);
	    }
	    
	    if (scouts == null || scouts.size()==0)
		{
			//only get them, if they are not there, otherwise we lose the selected scouts
			scouts = traxService.getScouts(signOffLeader.getOrganization().getId(), unitTypeName);
			if(scouts.size() == 0)
			{
				//could not find by leaders unittypename, try to get all
				unitTypeName = isCub?"Pack":"All";
				scouts = traxService.getScouts(signOffLeader.getOrganization().getId(), unitTypeName);
				session.setAttribute("unitTypeName", unitTypeName);
			}
			session.setAttribute(SCOUTS, scouts);
		}
		return scouts;
	}

	protected Scout getSelectedScout(HttpSession session)
	{
		List<Scout> scouts = getScouts(session);
		Scout selectedScout = null;
		for (Scout scout : scouts)
		{
			if (scout.isChecked() || scout.isSelected())
			{
				selectedScout = traxService.refreshScout(scouts.get(0));
				
				break;
			}
		}
		return selectedScout;
	}
}