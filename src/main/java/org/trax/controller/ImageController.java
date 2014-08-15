package org.trax.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.trax.model.Scout;
import org.trax.service.TraxService;

@Controller
public class ImageController
{

	@Autowired
	private TraxService traxService;

	@RequestMapping("/scoutimage.html")
	public void getImage(@RequestParam Long id, HttpServletRequest request, HttpServletResponse response)
			throws IOException
	{
		response.setContentType("image/png"); // "image/jpeg" ... you may need
												// to present the right content
												// type here
		if(id==0) return; //TODO kludge don't do anything, should not be getting here
		Scout scout = (Scout) traxService.getUserById(id);

		byte[] profileImage = scout.getProfileImage();
		if (profileImage != null)
		{
			response.getOutputStream().write(profileImage, 0, profileImage.length);
			response.getOutputStream().flush();
		}
	}
}
