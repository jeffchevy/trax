package org.trax.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Home page for the logged in members
 *
 */
@Controller
@RequestMapping("/member.html")
public class MemberController
{
	@RequestMapping(method=RequestMethod.GET)
	public String showPage()
	{
		return "redirect:advancement.html";
	}
	
}
