package org.trax.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/login.html")
public class LoginController
{
	@RequestMapping(method=RequestMethod.GET)
	public String showPage()
	{
		return "login";
	}
}
