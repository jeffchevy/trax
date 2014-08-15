package org.trax.dao;

import java.util.List;

import org.trax.model.Council;

public interface CouncilDao
{
	public Council getByName(String name);
	
	public List<Council> getByState(String state);
}
