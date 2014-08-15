package org.trax.form;

import java.util.Collection;

import javax.validation.constraints.NotNull;

public class EmailGroupForm extends EmailForm
{
	@NotNull
	private Collection<Long> selections;
	@NotNull
	private String subject;

	public Collection<Long> getSelections()
	{
		return selections;
	}

	public void setSelections(Collection<Long> selections)
	{
		this.selections = selections;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getSubject()
	{
		return subject;
	}

}
