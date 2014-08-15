package org.trax.form;


import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;


public class EmailForm
{
	@NotNull(message = "Must Provide message content")
	private String message;
	private String priority;
	@Email
	private String toEmail;
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getPriority()
	{
		return priority;
	}
	public void setPriority(String priority)
	{
		this.priority = priority;
	}
	
	public String getToEmail()
	{
		return toEmail;
	}
	public void setToEmail(String toEmail)
	{
		this.toEmail = toEmail;
	}
}
