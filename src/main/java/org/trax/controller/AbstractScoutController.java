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
	protected int checkedScoutNumber = 0;
	@Autowired
	protected TraxService traxService;

	public AbstractScoutController()
	{
		super();
	}
	
	/**
	 * get the selected scout if there is one, otherwise just get the first one. 
	 * Return null if there isn't one
	 * @param session
	 * @param scouts
	 * @return
	 */
	protected Scout getScout(HttpSession session)
	{
		Scout scout = (Scout)session.getAttribute(SCOUT);
		List<Scout> scouts = getScouts(session);

		if(scout==null)
		{
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
				if (scouts == null || scouts.size()==0)
				{
					//none selected make it the first one
					scouts = getScouts(session);
				}
				
				if (scouts != null && scouts.size()!=0)
				{
					//might not be any scouts yet, wait for them to add one. otherwise get it here
					scout = scouts.get(0);
				}
			}
		}
		
		scout = scout==null ? null : traxService.refreshScout(scout); //refresh
		return scout;
	}

	protected List<Scout> getScouts(HttpSession session)
	{
		User signOffLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		List<Scout> scouts = (List<Scout>)session.getAttribute(SCOUTS);
	    if (signOffLeader instanceof Scout)
		{
			Scout scout = (Scout)session.getAttribute(SCOUT); 
			if (scout==null)
			{
				session.setAttribute(SCOUT, signOffLeader);
				scout = (Scout)signOffLeader;
				scout.setSelected(true);//make sure
			}
			
		    if (scouts == null || scouts.size()==0)
			{
				scouts = new ArrayList<Scout>();
		    	scouts.add(scout);
				session.setAttribute(SCOUTS, scouts);
			}
		}
	    else
	    {
		    if (scouts == null || scouts.size()==0)
			{
		    	boolean isCub = signOffLeader.getUnit().isCub();
				String unitTypeName = isCub?"Pack":"All";
				
				//only get them, if they are not there, otherwise we lose the selected scouts
				scouts = traxService.getScouts(signOffLeader.getOrganization().getId(), unitTypeName);
				if(scouts.size() == 0)
				{
					scouts = traxService.getScouts(signOffLeader.getOrganization().getId(), unitTypeName);
					session.setAttribute("unitTypeName", unitTypeName);
				}
				session.setAttribute(SCOUTS, scouts);
			}
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