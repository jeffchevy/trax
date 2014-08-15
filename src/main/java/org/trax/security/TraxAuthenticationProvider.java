package org.trax.security;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.stereotype.Component;
import org.trax.dao.UserDao;
import org.trax.model.User;


@Component("traxAuthenticationProvider")
public class TraxAuthenticationProvider implements AuthenticationProvider
{
	private static final Logger logger = Logger.getLogger(TraxAuthenticationProvider.class);
	
	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;
	
    @Autowired
    private TraxPasswordEncoder passwordEncoder;
    
	//@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		String username = (String)authentication.getPrincipal();
		logger.debug("Found username: " + username);
		
		String password = (String)authentication.getCredentials();
		String encodedPassword = passwordEncoder.encodePassword(password, username);
		
		try
		{
			User user = userDao.findUser(username, encodedPassword);
			if (user == null)
			{
				throw new Exception("Not Found");
			}
			// We pass an instance of user to Spring security so that we dont have to store the user in session. Spring Security will do that for free.
			Authentication token = new UsernamePasswordAuthenticationToken(user, encodedPassword, getAuthorities(user));

			return token;
		}
		catch(Exception e)
		{
			logger.error("Error finding user with username: " + username, e);
			throw new BadCredentialsException("Invalid username or password");
		}
	}

	private Collection<GrantedAuthority> getAuthorities(User user)
	{
	    Collection<GrantedAuthority> authorities = user.getAuthorities();
		authorities.add(new GrantedAuthorityImpl("ROLE_USER"));

		return authorities;
	}
	
	//@Override
	public boolean supports(Class<? extends Object> clazz)
	{
		if(clazz.equals(UsernamePasswordAuthenticationToken.class))
		{
			return true;
		}
		
		return false;
	}

}
