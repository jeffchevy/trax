package org.trax.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController
{
	@RequestMapping(value="districthome.html")
	public String showDistrictHome(Map<String, Object> model)
	{
		return "districthome";
	}

	@RequestMapping(value="home.html")
	public String showPage(Map<String, Object> model)
	{
//		model.put("movie", "mario.flv");
//		model.put("title", "Super Mario Brothers Lego Edition");
//		model.put("autoplay", "on");	// on and off options
		
		return "home";
	}
	
	
	@RequestMapping(value="scoutTraxUserGuide.html")
	public String showScoutTraxUserGuide(Map<String, Object> model)
	{
		return "scoutTraxUserGuide";
	}
	
	@RequestMapping(value="userGuideForCubs.html")
	public String showUserGuideForCubs(Map<String, Object> model)
	{
		return "userGuideForCubs";
	}
	
	
}
