package org.trax.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController
{
	@RequestMapping(value="/login.html",method=RequestMethod.GET)
	public String showPage()
	{
		return "login";
	}
    @RequestMapping(value="/fblogin.html",method=RequestMethod.GET)
    public String showfbLogin()
    {
        return "fblogin";
    }
}
