package org.trax.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trax.dao.UserDao;
import org.trax.model.User;

@Service("userDetailsService")
@Transactional
public class HibernateUserDetailsService implements UserDetailsService
{
    @Autowired
    @Qualifier("userDao")
    private UserDao userDao;
    
    // This takes precedence over the class level setting
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException
    {
        System.out.println("loadUserByUsername: "+username);
        User myUser = userDao.getUserByUsername(username);
        
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(myUser.getUsername(), myUser.getPassword(), myUser.getAccountEnabled(), true, true, true, AuthorityUtils.createAuthorityList("ROLE_USER"));
        return user;
    }   
}