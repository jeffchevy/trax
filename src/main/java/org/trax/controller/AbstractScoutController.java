package org.trax.controller;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
	protected static final Format formatter = new SimpleDateFormat("MM/dd/yyyy");
	protected int checkedScoutNumber = 0;
	@Autowired
	protected TraxService traxService;

	public AbstractScoutController()
	{
		super();
	}

	protected List<Scout> getScouts(HttpServletRequest request)
	{
		User signOffLeader = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Scout> scouts = null;
	    if (signOffLeader instanceof Scout)
		{
			scouts = new ArrayList<Scout>();
			Scout scout = (Scout)request.getSession().getAttribute("scout"); 
			scouts.add(scout);
		}
	    else
	    {
	    	scouts = (List<Scout>)request.getSession().getAttribute(SCOUTS);
	    }
		return scouts;
	}
}