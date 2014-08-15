package org.trax.security;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;

@Component("traxPasswordEncoder")
public class TraxPasswordEncoder
{
	private ShaPasswordEncoder passwordEncoder;
	
	public TraxPasswordEncoder()
	{
		passwordEncoder = new ShaPasswordEncoder(256);
	}
	
	public String encodePassword(String password, String username)
	{
		return passwordEncoder.encodePassword(password, username);
	}
}
