package org.trax.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShirtController
{
	@RequestMapping("/shirt.html")
	public String shirt()
	{
		return "shirt";
	}
}
