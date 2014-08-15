package org.trax.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserCredentialsForm
{
    private long userId;
    
    @NotNull(message="Username cannot be null")
    @Size(min=1, max=64, message="Username cannot be null") 
    private String username;

    @NotNull(message="Password cannot be null")
    @Size(min=1, max=64, message="Password cannot be null") 
    private String password;

    @NotNull(message="Password cannot be null")
    @Size(min=1, max=64) 
    private String confirmPassword;

    public UserCredentialsForm()
	{
	}
    
    public UserCredentialsForm(long id, String username)
	{
		this.setUserId(id);
		this.setUsername(username);
	}
    
	public long getUserId()
    {
        return userId;
    }
    public void setUserId(long userId)
    {
        this.userId = userId;
    }
    public String getUsername()
    {
        return username;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }
    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public String getConfirmPassword()
    {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword)
    {
        this.confirmPassword = confirmPassword;
    }
}
