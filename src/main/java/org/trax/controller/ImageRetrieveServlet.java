package org.trax.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.trax.model.Scout;

public class ImageRetrieveServlet extends HttpServlet
{
	private static final long serialVersionUID = 6290659385134794998L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Scout scout = (Scout)request.getSession().getAttribute("scout");
		response.reset();
		response.setContentType("image/jpg");
		byte[] profileImage = scout.getProfileImage();
		response.getOutputStream().write(profileImage, 0, profileImage.length);
		response.getOutputStream().flush();
	}
}
